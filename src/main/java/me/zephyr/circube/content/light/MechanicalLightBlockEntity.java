package me.zephyr.circube.content.light;

import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MechanicalLightBlockEntity extends SplitShaftBlockEntity {
    public MechanicalLightBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public float getRotationSpeedModifier(Direction face) {
        return 1;
    }

    @Override
    public void tick() {
        super.tick();
        if (!getLevel().isClientSide()) {
            KineticNetwork kineticNetwork = getOrCreateNetwork();
            if (kineticNetwork != null && capacity >= stress) {
                level.setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, true), 2);
            } else {
                level.setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, false), 2);
            }
        }
    }
}
