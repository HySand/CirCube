package me.zephyr.circube.event;

import me.zephyr.circube.CirCubeEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber()
public class AccountEvents {
    private static final Map<String, Set<UUID>> ipLoginHistory = new HashMap<>();
    private static final Map<String, UUID> ipOnlineNow = new HashMap<>();

    @SubscribeEvent
    public static void onFirstJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        CompoundTag data = player.getPersistentData();

        if (!data.getBoolean("HasJoinedBefore")) {
            data.putBoolean("HasJoinedBefore", true);
            player.addEffect(new MobEffectInstance(CirCubeEffects.PURE_LIGHT.get(), 360000, 0, true, false));
        }
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        UUID uuid = player.getUUID();
        String ip = getIP(player);

        ipLoginHistory.putIfAbsent(ip, new HashSet<>());
        Set<UUID> history = ipLoginHistory.get(ip);

        if (!history.contains(uuid)) {
            if (history.size() >= 2) {
                player.connection.disconnect(Component.literal("最多只能登录两个账号"));
                return;
            }
            history.add(uuid);
        }

        if (ipOnlineNow.containsKey(ip) && !ipOnlineNow.get(ip).equals(uuid)) {
            player.connection.disconnect(Component.literal("不能同时登录多个账号"));
            return;
        }

        ipOnlineNow.put(ip, uuid);
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        UUID uuid = player.getUUID();
        String ip = getIP(player);

        if (ipOnlineNow.containsKey(ip) && ipOnlineNow.get(ip).equals(uuid)) {
            ipOnlineNow.remove(ip);
        }
    }

    private static String getIP(ServerPlayer player) {
        return player.connection.connection.getRemoteAddress().toString().split(":")[0];
    }


}
