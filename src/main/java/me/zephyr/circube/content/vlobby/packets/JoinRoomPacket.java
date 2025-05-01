package me.zephyr.circube.content.vlobby.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static me.zephyr.circube.util.DataManager.addPlayerToGame;

public class JoinRoomPacket {
    String entryName;
    public JoinRoomPacket(String name) {
        entryName = name;
    }

    public static void encode(JoinRoomPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.entryName);
    }

    public static JoinRoomPacket decode(FriendlyByteBuf buffer) {
        return new JoinRoomPacket(buffer.readUtf());
    }

    public static void handle(JoinRoomPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                addPlayerToGame(player, packet.entryName);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
