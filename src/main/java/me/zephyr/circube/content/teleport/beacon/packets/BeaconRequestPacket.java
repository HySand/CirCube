package me.zephyr.circube.content.teleport.beacon.packets;

import me.zephyr.circube.CirCubePackets;
import me.zephyr.circube.util.DataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Supplier;

public class BeaconRequestPacket {
    public BeaconRequestPacket() {
    }

    public static void encode(BeaconRequestPacket packet, FriendlyByteBuf buffer) {
    }

    public static BeaconRequestPacket decode(FriendlyByteBuf buffer) {
        return new BeaconRequestPacket();
    }

    public static void handle(BeaconRequestPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                DataManager.loadBeaconsToMemory(player);
                List<String> beaconList = DataManager.getBeaconIdsInMemory(player);
                BeaconDataPacket beaconSyncPacket = new BeaconDataPacket(beaconList);
                CirCubePackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), beaconSyncPacket);
            }
        });
        context.get().setPacketHandled(true);
    }
}
