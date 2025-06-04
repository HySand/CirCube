package me.zephyr.circube.content.neodymium;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.NeodymiumNodeBlock;
import me.zephyr.circube.CirCubeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;

public class BuddingNeodymiumBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final Direction[] DIRECTIONS = Direction.values();

    public BuddingNeodymiumBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP));
    }

    public static boolean canNodeGrowAtState(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clicked = context.getClickedFace();
        Direction facing = clicked.getAxis().isHorizontal() ? clicked : context.getHorizontalDirection().getOpposite();
        return defaultBlockState().setValue(FACING, facing);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(8) == 0) {
            Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            BlockPos relativePos = pos.relative(direction);
            BlockState relativeState = level.getBlockState(relativePos);
            Block block = null;
            Direction face = state.getValue(FACING);
            if (canNodeGrowAtState(relativeState)) {
                if (face.getClockWise() == direction) {
                    block = ACBlockRegistry.SCARLET_NEODYMIUM_NODE.get();
                } else if (face.getClockWise() == direction.getOpposite()) {
                    block = ACBlockRegistry.AZURE_NEODYMIUM_NODE.get();
                } else {
                    block = CirCubeBlocks.PALE_NEODYMIUM_NODE.get();
                }
            }

            if (block != null) {
                BlockState nodeState = block.defaultBlockState().setValue(NeodymiumNodeBlock.FACING, direction).setValue(NeodymiumNodeBlock.WATERLOGGED, relativeState.getFluidState().getType() == Fluids.WATER);
                level.setBlockAndUpdate(relativePos, nodeState);
            }
        }
    }
}
