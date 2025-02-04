package me.zephyr.circube.content.beacon.packets;

import me.zephyr.circube.CirCube;
import me.zephyr.circube.content.beacon.MechanicalBeaconBlockEntity;
import me.zephyr.circube.util.DataManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BeaconNameUpdatePacket {
    private final BlockPos pos;
    private final String newName;

    public BeaconNameUpdatePacket(BlockPos pos, String newName) {
        this.pos = pos;
        this.newName = newName;
    }

    public static void encode(BeaconNameUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeUtf(packet.newName);
    }

    public static BeaconNameUpdatePacket decode(FriendlyByteBuf buffer) {
        return new BeaconNameUpdatePacket(
                buffer.readBlockPos(), //pos
                buffer.readUtf()  // newName
        );
    }

    public static void handle(BeaconNameUpdatePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = context.get().getSender();
            if (serverPlayer != null) {
                BlockEntity blockEntity = serverPlayer.serverLevel().getBlockEntity(packet.pos);
                MechanicalBeaconBlockEntity beacon = (MechanicalBeaconBlockEntity) blockEntity;
                beacon.setBeaconName(packet.newName);
            }
        });
        context.get().setPacketHandled(true);
    }
}
