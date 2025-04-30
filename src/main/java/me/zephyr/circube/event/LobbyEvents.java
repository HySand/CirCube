package me.zephyr.circube.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static me.zephyr.circube.CirCube.MOD_ID;

public class LobbyEvents {
    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.DEDICATED_SERVER)
    public class QuitQueueEvent {
        @SubscribeEvent
        public static void onJoinServer(PlayerEvent.PlayerLoggedOutEvent event) {

        }
    }
}
