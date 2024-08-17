package me.zephyr.circube.spring;

import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import me.zephyr.circube.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class SpringBlockEntity extends GeneratingKineticBlockEntity {

    private static final float MAX_POWER = 307200F;
    private float stressInside = 0F;
    public int signal = 0;

    public SpringBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public float getGeneratedSpeed() {
        if (stressInside > 0F) {
            return 8F;
        } else if (stressInside < 0F) {
            return -8F;
        } else {
            return 0F;
        }
    }

    @Override
    public void tick() {

        //充能
        if (speed > 8F) {
            signal = 8;
            stressInside = Math.min(stressInside + (16 * speed), MAX_POWER);
        } else if (speed < -8F) {
            signal = 8;
            stressInside = Math.max(stressInside + (16 * speed), -MAX_POWER);
        } else {
            if (stressInside == 0F) signal = 4;
            else signal = 0;
        }
        level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
        //消耗
        if (stressInside > 0F) {
            stressInside = Math.max(stressInside - stress, 0F);
        } else if (stressInside < 0F) {
            stressInside = Math.min(stressInside + stress, 0F);
        }
        super.tick();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        Lang.number((int) Math.abs(stressInside /20))
                .translate("generic.unit.stress")
                .style(ChatFormatting.AQUA)
                .space()
                .add(Lang.translate("gui.goggles.stress_inside")
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);
        return added;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putFloat("Power", stressInside);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        float power = compound.getFloat("Power");
        super.read(compound, clientPacket);
    }
}
