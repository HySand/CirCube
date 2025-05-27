package me.zephyr.circube.content.teleport.beacon.packets;

import me.zephyr.circube.content.teleport.beacon.MechanicalBeaconBlockEntity;
import me.zephyr.circube.content.teleport.beacon.PositionControl;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BeaconPositionUpdatePacket {
    private final BlockPos pos;
    private final PositionControl positionControl;

    public BeaconPositionUpdatePacket(BlockPos pos, PositionControl positionControl) {
        this.pos = pos;
        this.positionControl = positionControl;
    }

    public static void encode(BeaconPositionUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeEnum(packet.positionControl);
    }

    public static BeaconPositionUpdatePacket decode(FriendlyByteBuf buffer) {
        return new BeaconPositionUpdatePacket(
                buffer.readBlockPos(), //pos
                buffer.readEnum(PositionControl.class)  // positionControl
        );
    }

    public static void handle(BeaconPositionUpdatePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = context.get().getSender();
            if (serverPlayer != null) {
                BlockEntity blockEntity = serverPlayer.serverLevel().getBlockEntity(packet.pos);
                MechanicalBeaconBlockEntity beacon = (MechanicalBeaconBlockEntity) blockEntity;
                beacon.setPositionMode(packet.positionControl);
            }
        });
        context.get().setPacketHandled(true);
    }
}
