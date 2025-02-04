// TeleportPacket.java
package me.zephyr.circube.content.stabilizer.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TeleportPacket {
    private final BlockPos targetPos;

    public TeleportPacket(BlockPos pos) {
        this.targetPos = pos;
    }

    public static void encode(TeleportPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.targetPos);
    }

    public static TeleportPacket decode(FriendlyByteBuf buffer) {
        return new TeleportPacket(buffer.readBlockPos());
    }

    public static void handle(TeleportPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                player.teleportTo(
                        player.serverLevel(),
                        packet.targetPos.getX() + 1.75,
                        packet.targetPos.getY(),
                        packet.targetPos.getZ() + 0.5,
                        player.getYRot(),
                        player.getXRot()
                );
            }
        });
        ctx.get().setPacketHandled(true);
    }
}