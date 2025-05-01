package me.zephyr.circube.content.vlobby.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static me.zephyr.circube.content.vlobby.DungeonManager.instantiateDimension;

public class StartGamePacket {
    String entryName;
    public StartGamePacket(String name) {
        entryName = name;
    }

    public static void encode(StartGamePacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.entryName);
    }

    public static StartGamePacket decode(FriendlyByteBuf buffer) {
        return new StartGamePacket(buffer.readUtf());
    }

    public static void handle(StartGamePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            instantiateDimension(ctx.get().getSender().getServer(), "lost_city");
        });
        ctx.get().setPacketHandled(true);
    }
}
