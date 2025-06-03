// TeleportPacket.java
package me.zephyr.circube.content.teleport.stabilizer.packets;

import me.zephyr.circube.content.teleport.beacon.PositionControl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class TeleportPacket {
    private final String levelName;
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
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 80, 0));

                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

                executor.schedule(() -> {
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
                    player.playNotifySound(SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                }, 2, TimeUnit.SECONDS);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}