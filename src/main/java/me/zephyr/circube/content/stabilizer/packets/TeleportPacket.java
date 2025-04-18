// TeleportPacket.java
package me.zephyr.circube.content.stabilizer.packets;

import me.zephyr.circube.content.beacon.PositionControl;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TeleportPacket {
    private final BlockPos targetPos;
    private final PositionControl positionMode;

    public TeleportPacket(BlockPos pos, PositionControl positionMode) {
        this.targetPos = pos;
        this.positionMode = positionMode;
    }

    public static void encode(TeleportPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.targetPos);
        buffer.writeEnum(packet.positionMode);
    }

    public static TeleportPacket decode(FriendlyByteBuf buffer) {
        return new TeleportPacket(buffer.readBlockPos(), buffer.readEnum(PositionControl.class));
    }

    public static void handle(TeleportPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                double x = packet.targetPos.getX() + 0.5;
                double z = packet.targetPos.getZ() + 0.5;
                switch (packet.positionMode) {
                    case NORTH -> z -= 1.75;
                    case EAST -> x += 1.75;
                    case SOUTH -> z += 1.75;
                    case WEST -> x -= 1.75;
                }
                player.teleportTo(
                        player.serverLevel(),
                        x,
                        packet.targetPos.getY() + 0.2,
                        z,
                        player.getYRot(),
                        player.getXRot()
                );
            }
        });
        ctx.get().setPacketHandled(true);
    }
}