package me.zephyr.circube.content.beacon.packets;

import me.zephyr.circube.CirCubePackets;
import me.zephyr.circube.util.DataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Supplier;

public class BeaconLoadPacket {
    public BeaconLoadPacket() {
    }

    public static void encode(BeaconLoadPacket packet, FriendlyByteBuf buffer) {
    }

    public static BeaconLoadPacket decode(FriendlyByteBuf buffer) {
        return new BeaconLoadPacket();
    }

    public static void handle(BeaconLoadPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                DataManager.loadBeaconsToMemory(player);
                List<String> beaconList = DataManager.getBeaconIdsInMemory(player);
                BeaconSyncPacket beaconSyncPacket = new BeaconSyncPacket(beaconList);
                CirCubePackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), beaconSyncPacket);
            }
        });
        context.get().setPacketHandled(true);
    }
}
