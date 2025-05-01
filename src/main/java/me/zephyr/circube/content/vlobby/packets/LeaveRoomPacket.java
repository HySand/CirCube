package me.zephyr.circube.content.vlobby.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static me.zephyr.circube.util.DataManager.removePlayerFromGame;

public class LeaveRoomPacket {
    String entryName;
    public LeaveRoomPacket(String name) {
        entryName = name;
    }

    public static void encode(LeaveRoomPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.entryName);
    }

    public static LeaveRoomPacket decode(FriendlyByteBuf buffer) {
        return new LeaveRoomPacket(buffer.readUtf());
    }

    public static void handle(LeaveRoomPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                removePlayerFromGame(player, packet.entryName);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
