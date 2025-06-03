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

    public static boolean canNodeGrowAtState(BlockState p_152735_) {
        return p_152735_.isAir() || p_152735_.is(Blocks.WATER) && p_152735_.getFluidState().getAmount() == 8;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(5) == 0) {
            Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            BlockPos relativePos = pos.relative(direction);
            BlockState relativeState = level.getBlockState(relativePos);
            Block block = null;

            if (canNodeGrowAtState(relativeState)) {
                if (state.getValue(FACING).getClockWise() == direction) {
                    block = ACBlockRegistry.SCARLET_NEODYMIUM_NODE.get();
                } else if (state.getValue(FACING).getClockWise() == direction.getOpposite()) {
                    block = ACBlockRegistry.AZURE_NEODYMIUM_NODE.get();
                } else {
                    block = CirCubeBlocks.PALE_NEODYMIUM_NODE.get();
                }

            }
            BlockState nodeState = block.defaultBlockState().setValue(NeodymiumNodeBlock.FACING, direction).setValue(NeodymiumNodeBlock.WATERLOGGED, relativeState.getFluidState().getType() == Fluids.WATER);
            level.setBlockAndUpdate(relativePos, nodeState);
        }
    }
}
