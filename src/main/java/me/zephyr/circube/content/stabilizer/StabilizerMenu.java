package me.zephyr.circube.content.stabilizer;

import me.zephyr.circube.content.beacon.MechanicalBeacon;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StabilizerMenu extends AbstractContainerMenu {
    private MechanicalBeacon beacon;

    protected StabilizerMenu(@Nullable MenuType<?> type, int windowId, MechanicalBeacon beacon) {
        super(type, windowId);
        this.beacon = beacon;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (beacon != null) {
            BlockPos pos = beacon.getPos();
            return player.distanceToSqr((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5) <= 64;
        }
        return true;
    }
}
