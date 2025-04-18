package me.zephyr.circube.content.stabilizer.packets;

import me.zephyr.circube.CirCubePackets;
import me.zephyr.circube.content.stabilizer.StabilizerEntry;
import me.zephyr.circube.util.DataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Supplier;

public class EntriesRequestPacket {
    public EntriesRequestPacket() {
    }

    public static void encode(EntriesRequestPacket packet, FriendlyByteBuf buffer) {
    }

    public static EntriesRequestPacket decode(FriendlyByteBuf buffer) {
        return new EntriesRequestPacket();
    }

    public static void handle(EntriesRequestPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = context.get().getSender();
            if (serverPlayer != null) {
                List<StabilizerEntry> entries = DataManager.loadBeaconEntries(serverPlayer);
                CirCubePackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new StabilizerDataPacket(entries));
            }
        });
        context.get().setPacketHandled(true);
    }
}