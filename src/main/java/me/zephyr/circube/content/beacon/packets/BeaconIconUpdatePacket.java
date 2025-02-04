package me.zephyr.circube.content.beacon.packets;

import me.zephyr.circube.util.DataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BeaconIconUpdatePacket {
    private final String beaconId;
    private final String newIcon;

    public BeaconIconUpdatePacket(String beaconId, String newIcon) {
        this.beaconId = beaconId;
        this.newIcon = newIcon;
    }

    public static void encode(BeaconIconUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.beaconId);
        buffer.writeUtf(packet.newIcon);
    }

    public static BeaconIconUpdatePacket decode(FriendlyByteBuf buffer) {
        return new BeaconIconUpdatePacket(
                buffer.readUtf(), // beaconId
                buffer.readUtf()  // newIcon（例如 "minecraft:stone"）
        );
    }

    public static void handle(BeaconIconUpdatePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = context.get().getSender();
            if (serverPlayer != null) {
                DataManager.updateBeaconIcon(serverPlayer, packet.beaconId, packet.newIcon);
            }
        });
        context.get().setPacketHandled(true);
    }
}
