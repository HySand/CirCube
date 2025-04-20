// TeleportPacket.java
package me.zephyr.circube.content.stabilizer.packets;

import me.zephyr.circube.content.beacon.PositionControl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;

public class TeleportPacket {
    private String levelName;
    private final BlockPos targetPos;
    private final PositionControl positionMode;

    public TeleportPacket(String levelName, BlockPos pos, PositionControl positionMode) {
        this.levelName = levelName;
        this.targetPos = pos;
        this.positionMode = positionMode;
    }

    public static void encode(TeleportPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.levelName);
        buffer.writeBlockPos(packet.targetPos);
        buffer.writeEnum(packet.positionMode);
    }

    public static TeleportPacket decode(FriendlyByteBuf buffer) {
        return new TeleportPacket(
                buffer.readUtf(),
                buffer.readBlockPos(),
                buffer.readEnum(PositionControl.class)
        );
    }

    public static void handle(TeleportPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ServerLevel level = player.getServer().getLevel((ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(packet.levelName))));
                double x = packet.targetPos.getX() + 0.5;
                double z = packet.targetPos.getZ() + 0.5;
                float yaw = 0;

                switch (packet.positionMode) {
                    case NORTH -> {
                        z -= 1.75;
                        yaw = 180;
                    }
                    case SOUTH -> {
                        z += 1.75;
                        yaw = 0;
                    }
                    case EAST -> {
                        x += 1.75;
                        yaw = 270;
                    }
                    case WEST -> {
                        x -= 1.75;
                        yaw = 90;
                    }
                    case NORTH_EAST -> {
                        x += 1.2;
                        z -= 1.2;
                        yaw = 225;
                    }
                    case NORTH_WEST -> {
                        x -= 1.2;
                        z -= 1.2;
                        yaw = 135;
                    }
                    case SOUTH_EAST -> {
                        x += 1.2;
                        z += 1.2;
                        yaw = 315;
                    }
                    case SOUTH_WEST -> {
                        x -= 1.2;
                        z += 1.2;
                        yaw = 45;
                    }
                }

                player.teleportTo(
                        level,
                        x,
                        packet.targetPos.getY() + 0.2,
                        z,
                        yaw,
                        0
                );
            }
        });
        ctx.get().setPacketHandled(true);
    }
}