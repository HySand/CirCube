package me.zephyr.circube.content.neodymium;

import com.github.alexmodguy.alexscaves.server.block.NeodymiumNodeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PaleNeodymiumNodeBlock extends NeodymiumNodeBlock {
    public PaleNeodymiumNodeBlock(boolean azure) {
        super(azure);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
    }
}
