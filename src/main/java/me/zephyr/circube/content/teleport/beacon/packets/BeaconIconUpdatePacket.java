package me.zephyr.circube.content.teleport.beacon.packets;

import me.zephyr.circube.content.teleport.beacon.MechanicalBeaconBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BeaconIconUpdatePacket {
    private final BlockPos pos;
    private final ItemStack newIcon;

    public BeaconIconUpdatePacket(BlockPos pos, ItemStack newIcon) {
        this.pos = pos;
        this.newIcon = newIcon;
    }

    public static void encode(BeaconIconUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeItem(packet.newIcon);
    }

    public static BeaconIconUpdatePacket decode(FriendlyByteBuf buffer) {
        return new BeaconIconUpdatePacket(
                buffer.readBlockPos(),
                buffer.readItem()
        );
    }

    public static void handle(BeaconIconUpdatePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = context.get().getSender();
            if (serverPlayer != null) {
                BlockEntity blockEntity = serverPlayer.serverLevel().getBlockEntity(packet.pos);
                if (blockEntity instanceof MechanicalBeaconBlockEntity beacon) {
                    beacon.setIcon(packet.newIcon);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
