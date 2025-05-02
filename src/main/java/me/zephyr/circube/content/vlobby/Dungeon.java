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
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static me.zephyr.circube.CirCube.MOD_ID;
import static me.zephyr.circube.content.vlobby.DungeonManager.*;
import static me.zephyr.circube.util.DataManager.getDungeonList;

public abstract class Dungeon {
    private final int dungeonId;
    private final String dungeonName;
    private final int difficulty;
    private final int maxPlayers;
    private final List<UUID> players;
    private boolean started;

    private static final Set<ServerPlayer> trackedPlayers = new HashSet<>();
    private static final Set<LivingEntity> trackedMonsters = new HashSet<>();

    public Dungeon(int dungeonId, String dungeonName, int difficulty, int maxPlayers) {
        this.dungeonId = dungeonId;
        this.dungeonName = dungeonName;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>(maxPlayers);

    }

    public void setGameStatus(boolean start, MinecraftServer server) throws IOException {
        if (start && isGameReadyToStart()) {
            ServerLevel level = instantiateDimension(server, dungeonName);
            started = true;
            for (UUID uuid : players) {
                ServerPlayer player = server.getPlayerList().getPlayer(uuid);
                player.teleportTo(
                        level,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        0,
                        0
                );
                track(player);
            }
        } else {
            unloadDimension(server, dungeonnameMap.get(dungeonName));
            started = false;
        }

    }

    public void addPlayerToGame(ServerPlayer player) {
        if (!started && !isGameReadyToStart()) {
            removePlayerFromOtherGames(player.getUUID());
            players.add(player.getUUID());
        }
    }

    public void removePlayerFromGame(ServerPlayer player) {
        if (!started) {
            players.remove(player.getUUID());
        }
    }

    private void removePlayerFromOtherGames(UUID uuid) {
        for (Dungeon dungeon : getDungeonList()) {
            dungeon.players.remove(uuid);
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

    public List<UUID> getPlayers() {
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

    private void track(ServerPlayer player) {
        trackedPlayers.add(player);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID)
    public class Tracker {
        @SubscribeEvent
        public static void onPlayerDead(LivingDeathEvent event) {
            if (event.getEntity() instanceof ServerPlayer player && !event.isCanceled()) {
                if (!trackedPlayers.contains(player)) return;
                for (ServerPlayer trackedPlayer : trackedPlayers) {
                    startCountdown(trackedPlayer, 5, "因有玩家死亡，将在%d秒后结束游戏", (p) -> {
                        trackedPlayers.remove(trackedPlayer);
                        trackedPlayer.respawn();
                        removeDungeonWorld(event.getEntity().getServer(), player);
                    });
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                if (!trackedPlayers.contains(player)) return;
                for (ServerPlayer trackedPlayer : trackedPlayers) {
                    startCountdown(trackedPlayer, 5, "因有玩家退出，将在%d秒后结束游戏", (p) -> {
                        trackedPlayers.remove(trackedPlayer);
                        trackedPlayer.respawn();
                        removeDungeonWorld(event.getEntity().getServer(), player);
                    });
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerPotionAdded(MobEffectEvent event) {
            if (event.getEntity() instanceof ServerPlayer player && trackedPlayers.contains(player)) {
                MobEffectInstance effect = event.getEffectInstance();
            }
        }

        @SubscribeEvent
        public static void onKillEntity(LivingDeathEvent event) {
            Entity source = event.getSource().getEntity();
            if (source instanceof ServerPlayer player && trackedPlayers.contains(player)) {

            }
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

    private static void removeDungeonWorld(MinecraftServer server, ServerPlayer player) {
        String name = "";
        for (int i = 0; i < getDungeonList().size(); i ++) {
            if (getDungeonList().get(i).getPlayers().contains(player.getUUID())) {
                name = getDungeonList().get(i).getName();
            }
        }
        unloadDimension(server, dungeonnameMap.get(name));
    }
}
