package me.zephyr.circube.event;

import me.zephyr.circube.CirCube;
import me.zephyr.circube.content.vlobby.RoomEntry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

import static me.zephyr.circube.CirCube.MOD_ID;
import static me.zephyr.circube.util.DataManager.addGameToList;

public class LobbyEvents {
    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.DEDICATED_SERVER)
    public class QuitQueueEvent {
        @SubscribeEvent
        public static void onJoinServer(PlayerEvent.PlayerLoggedOutEvent event) {

        }
    }
}
