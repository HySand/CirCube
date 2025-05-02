package me.zephyr.circube.content.vlobby.packets;

import me.zephyr.circube.CirCubePackets;
import me.zephyr.circube.content.vlobby.Dungeon;
import me.zephyr.circube.content.vlobby.RoomEntry;
import me.zephyr.circube.util.DataManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RoomEntriesRequestPacket {
    public RoomEntriesRequestPacket() {
    }

    public static void encode(RoomEntriesRequestPacket packet, FriendlyByteBuf buffer) {
    }

    public static RoomEntriesRequestPacket decode(FriendlyByteBuf buffer) {
        return new RoomEntriesRequestPacket();
    }

    public static void handle(RoomEntriesRequestPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = context.get().getSender();
            if (serverPlayer != null) {
                List<Dungeon> dungeons = DataManager.getDungeonList();
                List<RoomEntry> entries = new ArrayList<>();
                for (Dungeon dungeon : dungeons) {
                    RoomEntry entry = new RoomEntry(dungeon.getId(), dungeon.getName(), dungeon.getDifficulty(), dungeon.getMaxPlayers(), dungeon.getPlayers(), dungeon.isStarted());
                    entries.add(entry);
                }
                CirCubePackets.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new RoomDataPacket(entries));
            }
        });
        context.get().setPacketHandled(true);
    }
}