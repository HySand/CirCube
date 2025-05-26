package me.zephyr.circube.content.geyser;

import me.zephyr.circube.CirCubeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.function.Function;

public class GeyserFeature extends Feature<OreConfiguration> {
    public GeyserFeature() {
        super(OreConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<OreConfiguration> ctx) {
        WorldGenLevel level = ctx.level();
        BlockPos pos = ctx.origin();
        RandomSource random = ctx.random();
        OreConfiguration config = ctx.config();
        BlockState state = level.getBlockState(pos);

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (OreConfiguration.TargetBlockState target : config.targetStates) {
            if (!canPlace(state, level::getBlockState, random, config, target, mutablePos.set(pos))) {
                continue;
            }

            level.setBlock(pos, CirCubeBlocks.GEYSER.get().defaultBlockState(), 2);
            return true;
        }

        return false;
    }

    public boolean canPlace(BlockState pState, Function<BlockPos, BlockState> pAdjacentStateAccessor,
                            RandomSource pRandom, OreConfiguration pConfig, OreConfiguration.TargetBlockState pTargetState,
                            BlockPos.MutableBlockPos pMatablePos) {
        if (!pTargetState.target.test(pState, pRandom))
            return false;
        if (!pTargetState.state.getFluidState().isEmpty())
            return false;
        if (shouldSkipAirCheck(pRandom, pConfig.discardChanceOnAirExposure))
            return true;

        return !isAdjacentToAir(pAdjacentStateAccessor, pMatablePos);
    }

    protected boolean shouldSkipAirCheck(RandomSource pRandom, float pChance) {
        return pChance <= 0 || !(pChance >= 1) && pRandom.nextFloat() >= pChance;
    }
}

