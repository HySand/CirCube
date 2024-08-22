package me.zephyr.circube.content.beacon;

import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MechanicalBeaconBlockEntity extends KineticBlockEntity {
    public MechanicalBeaconBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        KineticNetwork kineticNetwork = getOrCreateNetwork();
        if (kineticNetwork != null && capacity >= stress && lastStressApplied > 0) {
            level.setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, true), 2);
        } else {
            level.setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, false), 2);
        }
    }
}
