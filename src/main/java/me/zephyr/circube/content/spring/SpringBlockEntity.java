package me.zephyr.circube.content.spring;

import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class SpringBlockEntity extends GeneratingKineticBlockEntity {

    private static final float MAX_POWER = 7680;
    private float power = 0;


    public SpringBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public float getGeneratedSpeed() {
        if (power > 0) {
            return 8;
        } else if (power < 0) {
            return -8;
        } else return 0;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        //自然流逝
        if (power > 0) {
            power = Math.max(power - 8, 0);
        } else if (power < 0) {
            power = Math.min(power + 8, 0);
        }
        //充能
        if (speed > 8.0) {
            power = Math.min(power + (8 * (speed - 8)), MAX_POWER);
        } else if (speed < -8.0) {
            power = Math.max(power + (8 * (speed + 8)), -MAX_POWER);
        }
        //消耗
        if (stress != 0) {
            if (power > 0) {
                power = Math.max(power - stress, 0);
            } else if (power < 0) {
                power = Math.min(power + stress, 0);
            }
        }
        updateGeneratedRotation();
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putFloat("Power", power);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        power = compound.getFloat("Power");
        super.read(compound, clientPacket);
    }
}
