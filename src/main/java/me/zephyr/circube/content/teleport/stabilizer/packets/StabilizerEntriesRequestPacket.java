package me.zephyr.circube.content.teleport.stabilizer.packets;

import me.zephyr.circube.CirCubePackets;
import me.zephyr.circube.content.teleport.stabilizer.StabilizerEntry;
import me.zephyr.circube.util.DataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Supplier;

public class StabilizerEntriesRequestPacket {
    public StabilizerEntriesRequestPacket() {
    }

    public static void encode(StabilizerEntriesRequestPacket packet, FriendlyByteBuf buffer) {
    }

    public static StabilizerEntriesRequestPacket decode(FriendlyByteBuf buffer) {
        return new StabilizerEntriesRequestPacket();
    }

    public static void handle(StabilizerEntriesRequestPacket packet, Supplier<NetworkEvent.Context> context) {
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