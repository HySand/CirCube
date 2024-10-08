package me.zephyr.circube.content.spring;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.worldWrappers.WrappedWorld;
import me.zephyr.circube.CirCubeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class SpringBlock extends DirectionalKineticBlock implements IBE<SpringBlockEntity> {

    public SpringBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState stateIn, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor worldIn, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (worldIn instanceof WrappedWorld)
            return stateIn;
        if (worldIn.isClientSide())
            return stateIn;
        if (!worldIn.getBlockTicks()
                .hasScheduledTick(currentPos, this))
            worldIn.scheduleTick(currentPos, this, 1);
        return stateIn;
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, worldIn, pos, oldState, isMoving);
        if (worldIn.isClientSide())
            return;
        if (!worldIn.getBlockTicks()
                .hasScheduledTick(pos, this))
            worldIn.scheduleTick(pos, this, 1);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        state.setValue(FACING, Direction.get(Direction.AxisDirection.POSITIVE, state.getValue(FACING)
                .getAxis()));
        return state;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return state.getValue(FACING)
                .getAxis() == face.getAxis();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING)
                .getAxis();
    }

    @Override
    public int getSignal(@NotNull BlockState blockState, @NotNull BlockGetter blockAccess, @NotNull BlockPos pos, @NotNull Direction side) {
        return getBlockEntityOptional(blockAccess, pos).map(al -> al.signal).orElse(0);
    }

    @Override
    public boolean isSignalSource(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getDirectSignal(@NotNull BlockState blockState, @NotNull BlockGetter blockAccess, @NotNull BlockPos pos, @NotNull Direction side) {
        return getSignal(blockState, blockAccess, pos, side);
    }

    @Override
    public Class<SpringBlockEntity> getBlockEntityClass() {
        return SpringBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SpringBlockEntity> getBlockEntityType() {
        return CirCubeBlocks.SPRING_ENTITY.get();
    }

    @Override
    public boolean hideStressImpact() {
        return true;
    }

    public static Couple<Integer> getSpeedRange() {
        return Couple.create(8, 8);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }
}
