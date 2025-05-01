package me.zephyr.circube.content.vlobby.packets;

import me.zephyr.circube.content.beacon.PositionControl;
import me.zephyr.circube.content.stabilizer.StabilizerEntry;
import me.zephyr.circube.content.stabilizer.StabilizerScreen;
import me.zephyr.circube.content.vlobby.LobbyScreen;
import me.zephyr.circube.content.vlobby.RoomEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class RoomDataPacket {
    private final List<RoomEntry> entries;

    public RoomDataPacket(List<RoomEntry> entries) {
        this.entries = entries;
    }

    public static void encode(RoomDataPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entries.size());
        for (RoomEntry entry : packet.entries) {
            buffer.writeUtf(entry.getName());
            buffer.writeInt(entry.getDifficulty());
            buffer.writeInt(entry.getMaxPlayers());
            buffer.writeCollection(entry.getPlayers(), FriendlyByteBuf::writeUUID);
            buffer.writeBoolean(entry.isStarted());
        }
    }

    public static RoomDataPacket decode(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        List<RoomEntry> entries = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String roomName = buffer.readUtf();
            int difficulty = buffer.readInt();
            int maxPlayers = buffer.readInt();
            List<UUID> players = buffer.readList(FriendlyByteBuf::readUUID);
            boolean started = buffer.readBoolean();
            entries.add(new RoomEntry(roomName, difficulty, maxPlayers, players, started));
        }
        return new RoomDataPacket(entries);
    }

    public static void handle(RoomDataPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Minecraft client = Minecraft.getInstance();
            if (client.screen instanceof LobbyScreen screen) {
                screen.updateRoomEntries(packet.getEntries());
            }
        });
        context.get().setPacketHandled(true);
    }

    public List<RoomEntry> getEntries() {
        return entries;
    }
}