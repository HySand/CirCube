package me.zephyr.circube.event;

import me.zephyr.circube.CirCubePackets;
import me.zephyr.circube.content.teleport.beacon.packets.BeaconRequestPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static me.zephyr.circube.CirCube.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class BeaconEvents {

    @SubscribeEvent
    public static void onJoinServer(ClientPlayerNetworkEvent.LoggingIn event) {
        CirCubePackets.CHANNEL.sendToServer(new BeaconRequestPacket());
    }
}
