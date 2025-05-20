package me.zephyr.circube.content.vlobby;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static me.zephyr.circube.CirCube.forgeEventBus;
import static me.zephyr.circube.content.vlobby.DungeonManager.instantiateDimension;
import static me.zephyr.circube.content.vlobby.DungeonManager.unloadDimension;
import static me.zephyr.circube.util.DataManager.getDungeonList;

public abstract class Dungeon {
    protected final int dungeonId;
    protected final String dungeonName;
    protected final int difficulty;
    protected final int maxPlayers;
    protected final List<ServerPlayer> players;
    protected boolean started;
    protected ServerLevel level;
    protected Tracker tracker;
    protected Protect protect;
    protected BlockPos spawnPosition;
    protected long startTime;
    private final Map<UUID, Integer> playerKillCount = new HashMap<>();

    protected static final Set<LivingEntity> trackedMonsters = new HashSet<>();

    public Dungeon(int dungeonId, String dungeonName, int difficulty, int maxPlayers, BlockPos spawnPosition) {
        this.dungeonId = dungeonId;
        this.dungeonName = dungeonName;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>(maxPlayers);
        this.tracker = new Tracker();
        this.protect = new Protect();
        this.spawnPosition = spawnPosition;
    }

    public void setGameStatus(boolean start, MinecraftServer server) throws IOException {
        if (start) {
            if (isGameReadyToStart() && !started) {
                level = instantiateDimension(server, dungeonName);
                level.getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).set(true, level.getServer());
                level.getGameRules().getRule(GameRules.RULE_DOMOBLOOT).set(false, level.getServer());
                tracker.register();
                protect.register();
                initDungeon();
                started = true;
                if (spawnPosition == null) return;
                for (ServerPlayer player : players) {
                    player.teleportTo(
                            level,
                            spawnPosition.getX(),
                            spawnPosition.getY(),
                            spawnPosition.getZ(),
                            0,
                            0
                    );
                }
                startTime = System.currentTimeMillis();
            }
        } else {
            unloadDimension(server, level.dimension().location().getPath());
            players.clear();
            started = false;
            tracker.unregister();
        }

    }

    public void addPlayerToGame(ServerPlayer player) {
        if (!started && !isGameReadyToStart()) {
            removePlayerFromOtherGames(player);
            players.add(player);
        }
    }

    public void removePlayerFromGame(ServerPlayer player) {
        if (!started) {
            players.remove(player);
        }
    }

    private void removePlayerFromOtherGames(ServerPlayer player) {
        for (Dungeon dungeon : getDungeonList()) {
            dungeon.players.remove(player);
        }
    }

    public boolean isGameReadyToStart() {
        return players.size() == maxPlayers;
    }

    public int getId() {
        return dungeonId;
    }

    public String getName() {
        return dungeonName;
    }

    public List<ServerPlayer> getPlayers() {
        return players;
    }

    public boolean isStarted() {
        return started;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public class Tracker {
        @SubscribeEvent
        public void onPlayerDead(LivingDeathEvent event) {
            if (event.isCanceled()) return;
            if (event.getEntity() instanceof ServerPlayer player) {
                if (!players.contains(player)) return;
                for (ServerPlayer trackedPlayer : players) {
                    startCountdown(trackedPlayer, 5, "因有玩家死亡，将在%d秒后结束游戏", (p) -> {
                        trackedPlayer.respawn();
                        try {
                            setGameStatus(false, event.getEntity().getServer());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            } else if (event.getEntity() instanceof Monster && event.getSource().getEntity() instanceof ServerPlayer player && players.contains(player)) {
                UUID id = player.getUUID();
                playerKillCount.put(id, playerKillCount.getOrDefault(id, 0) + 1);
            }
        }

        @SubscribeEvent
        public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                if (!players.contains(player)) return;
                for (ServerPlayer trackedPlayer : players) {
                    startCountdown(trackedPlayer, 5, "因有玩家退出，将在%d秒后结束游戏", (p) -> {
                        trackedPlayer.respawn();
                        protect.unregister();
                        try {
                            setGameStatus(false, event.getEntity().getServer());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        }

        public void register() {
            forgeEventBus.register(this);
        }

        public void unregister() {
            forgeEventBus.unregister(this);
        }
    }

    public class Protect {
        @SubscribeEvent
        public void onBreak(BlockEvent.BreakEvent event) {
            if (players.contains(event.getPlayer())) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public void onPlace(BlockEvent.EntityPlaceEvent event) {
            Entity source = event.getEntity();
            if (source instanceof ServerPlayer player && players.contains(player)) {
                event.setCanceled(true);
            }
        }

        public void register() {
            forgeEventBus.register(this);
        }

        public void unregister() {
            forgeEventBus.unregister(this);
        }
    }

    public static void startCountdown(ServerPlayer player, int seconds, String messageFormat, Consumer<ServerPlayer> afterCountdown) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        for (int i = seconds; i > 0; i--) {
            final int remainingSeconds = i;
            executor.schedule(() -> {
                String message = String.format(messageFormat, remainingSeconds);
                player.sendSystemMessage(Component.literal(message).withStyle(ChatFormatting.YELLOW), true);
            }, seconds - i, TimeUnit.SECONDS);
        }
        executor.schedule(() -> {
            afterCountdown.accept(player);
            executor.shutdown();
        }, seconds, TimeUnit.SECONDS);
    }

    protected abstract void initDungeon();

    protected void missionComplete() throws IOException {
        tracker.unregister();
        long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
        for (ServerPlayer player : players) {
            player.sendSystemMessage(Component.literal("§6=== 地牢结算 ==="));
            player.sendSystemMessage(Component.literal("§e用时：§f" + formatDuration(elapsedSeconds)));
            player.sendSystemMessage(Component.literal("§e击杀统计："));

            for (ServerPlayer p : players) {
                UUID id = p.getUUID();
                int kills = playerKillCount.getOrDefault(id, 0);
                player.sendSystemMessage(Component.literal("§f - " + p.getGameProfile().getName() + "：§b" + kills + " 次"));
            }

            player.sendSystemMessage(Component.literal("§6================"));

            startCountdown(player, 60, "任务完成，将在%d秒后结束游戏", (p) -> {
                try {
                    setGameStatus(false, level.getServer());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private static String formatDuration(long elapsedSeconds) {
        int minutes = (int) (elapsedSeconds / 60);
        int seconds = (int) (elapsedSeconds % 60);
        if (minutes > 0) {
            return String.format("%d分%d秒", minutes, seconds);
        } else {
            return String.format("%d秒", seconds);
        }
    }
}
