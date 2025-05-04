package me.zephyr.circube.content.vlobby;

import me.zephyr.circube.CirCube;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static me.zephyr.circube.CirCube.forgeEventBus;
import static me.zephyr.circube.content.vlobby.DungeonManager.instantiateDimension;
import static me.zephyr.circube.content.vlobby.DungeonManager.unloadDimension;
import static me.zephyr.circube.util.DataManager.getDungeonList;

public abstract class Dungeon {
    private final int dungeonId;
    private final String dungeonName;
    private final int difficulty;
    private final int maxPlayers;
    private final List<ServerPlayer> players;
    private boolean started;
    private ServerLevel level;

    private static final Set<LivingEntity> trackedMonsters = new HashSet<>();

    public Dungeon(int dungeonId, String dungeonName, int difficulty, int maxPlayers) {
        this.dungeonId = dungeonId;
        this.dungeonName = dungeonName;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>(maxPlayers);

    }

    public void setGameStatus(boolean start, MinecraftServer server) throws IOException {
        if (start) {
            if (isGameReadyToStart() && !started) {
                level = instantiateDimension(server, dungeonName);
                started = true;
                for (ServerPlayer player : players) {
                    player.teleportTo(
                            level,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            0,
                            0
                    );
                    new Tracker().register();
                }
            }
        } else {
            CirCube.LOGGER.error(level.dimension().location().getPath());
            unloadDimension(server, level.dimension().location().getPath());
            players.clear();
            started = false;
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
            if (event.getEntity() instanceof ServerPlayer player && !event.isCanceled()) {
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
            }
        }

        @SubscribeEvent
        public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                if (!players.contains(player)) return;
                for (ServerPlayer trackedPlayer : players) {
                    startCountdown(trackedPlayer, 5, "因有玩家退出，将在%d秒后结束游戏", (p) -> {
                        trackedPlayer.respawn();
                        try {
                            setGameStatus(false, event.getEntity().getServer());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        }

        @SubscribeEvent
        public void onPlayerPotionAdded(MobEffectEvent event) {
            if (event.getEntity() instanceof ServerPlayer player && players.contains(player)) {
                MobEffectInstance effect = event.getEffectInstance();
            }
        }

        @SubscribeEvent
        public void onKillEntity(LivingDeathEvent event) {
            Entity source = event.getSource().getEntity();
            if (source instanceof ServerPlayer player && players.contains(player)) {

            }
        }

        @SubscribeEvent
        public void onBreak(BlockEvent.BreakEvent event) {
            if (players.contains(event.getPlayer())) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public void onBreak(BlockEvent.EntityPlaceEvent event) {
            Entity source = event.getEntity();
            if (source instanceof ServerPlayer player && players.contains(player)) {
                event.setCanceled(true);
            }
        }

        public void register() {
            forgeEventBus.register(this);
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
}
