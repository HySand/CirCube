package me.zephyr.circube.content.light;

import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BrassLightBlockEntity extends SplitShaftBlockEntity {
    public static final Map<BlockPos, Float> activatedBrassLight = new HashMap<>();

    public BrassLightBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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
                activatedBrassLight.put(getBlockPos(), lastStressApplied);
                level.setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, true), 2);
            } else {
                activatedBrassLight.remove(getBlockPos());
                level.setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, false), 2);
            }
        }
    }
}
