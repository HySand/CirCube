package me.zephyr.circube.event;

import me.zephyr.circube.CirCubePackets;
import me.zephyr.circube.content.beacon.packets.BeaconRequestPacket;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static me.zephyr.circube.CirCube.MOD_ID;

public class BeaconEvents {
    @Mod.EventBusSubscriber(modid = MOD_ID)
    public class SyncBeaconEvent {
        @SubscribeEvent
        public static void onJoinServer(ClientPlayerNetworkEvent.LoggingIn event) {
            CirCubePackets.CHANNEL.sendToServer(new BeaconRequestPacket());
        }
    }
}
