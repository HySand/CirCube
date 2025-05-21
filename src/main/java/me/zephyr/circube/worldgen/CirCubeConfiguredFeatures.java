package me.zephyr.circube.worldgen;

import me.zephyr.circube.CirCube;
import me.zephyr.circube.CirCubeBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

import static net.minecraft.data.worldgen.features.FeatureUtils.register;

public class CirCubeConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>>
            GEYSER = key("geyser");

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, CirCube.asResource(name));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
        RuleTest stoneOreReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        List<OreConfiguration.TargetBlockState> geyserTargetStates = List.of(
                OreConfiguration.target(stoneOreReplaceables, CirCubeBlocks.GEYSER.get()
                        .defaultBlockState()),
                OreConfiguration.target(deepslateOreReplaceables, CirCubeBlocks.GEYSER.get()
                        .defaultBlockState())
        );


        register(ctx, GEYSER, CirCubeFeatures.GEYSER.get(), new OreConfiguration(geyserTargetStates, 1, 1));
    }
}
