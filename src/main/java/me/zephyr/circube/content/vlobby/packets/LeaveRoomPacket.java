package me.zephyr.circube.content.vlobby.packets;

import me.zephyr.circube.content.vlobby.Dungeon;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static me.zephyr.circube.util.DataManager.getDungeonList;

public class LeaveRoomPacket {
    int entryId;

    public LeaveRoomPacket(int id) {
        entryId = id;
    }

    public static void encode(LeaveRoomPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entryId);
    }

    public static LeaveRoomPacket decode(FriendlyByteBuf buffer) {
        return new LeaveRoomPacket(buffer.readInt());
    }

    public static void handle(LeaveRoomPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                Dungeon dungeon = getDungeonList().get(packet.entryId);
                dungeon.removePlayerFromGame(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
