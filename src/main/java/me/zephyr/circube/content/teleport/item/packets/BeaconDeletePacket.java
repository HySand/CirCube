package me.zephyr.circube.content.teleport.item.packets;

import me.zephyr.circube.util.DataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BeaconDeletePacket {
    private final String beaconId;

    public BeaconDeletePacket(String beaconId) {
        this.beaconId = beaconId;
    }

    public static void encode(BeaconDeletePacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.beaconId);
    }

    public static BeaconDeletePacket decode(FriendlyByteBuf buffer) {
        return new BeaconDeletePacket(buffer.readUtf());
    }

    public static void handle(BeaconDeletePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = context.get().getSender();
            if (serverPlayer != null) {
                DataManager.removeBeaconFromPlayer(serverPlayer, packet.beaconId);
            }
        });
        context.get().setPacketHandled(true);
    }
}
