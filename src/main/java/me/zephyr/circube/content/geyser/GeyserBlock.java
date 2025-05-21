package me.zephyr.circube.content.geyser;

import me.zephyr.circube.CirCubeShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class GeyserBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty ERUPTING = BooleanProperty.create("erupting");

    public GeyserBlock(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false).setValue(ERUPTING, false));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 30);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ERUPTING, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER)
                .setValue(ERUPTING, false);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        boolean erupting = state.getValue(ERUPTING);

        if (erupting) {
            if (random.nextFloat() < 0.05f) {
                level.setBlockAndUpdate(pos, state.setValue(ERUPTING, false));
            } else {
                eruptOnce(level, pos);
            }
        } else {
            if (random.nextFloat() < 0.003f) {
                level.setBlockAndUpdate(pos, state.setValue(ERUPTING, true).setValue(WATERLOGGED, true));
            }
        }

        level.scheduleTick(pos, this, 30);
    }

    private void eruptOnce(ServerLevel level, BlockPos pos) {
        RandomSource random = level.getRandom();
        for (int dy = 1; dy <= 1 + random.nextInt(2); dy++) {
            BlockPos up = pos.above(dy);
            if (level.isEmptyBlock(up)) {
                level.setBlockAndUpdate(up, Blocks.WATER.defaultBlockState());
            } else {
                break;
            }
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return CirCubeShapes.GEYSER;
    }
}
