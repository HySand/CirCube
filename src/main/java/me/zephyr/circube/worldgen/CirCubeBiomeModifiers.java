package me.zephyr.circube.worldgen;

import me.zephyr.circube.CirCube;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class CirCubeBiomeModifiers {
    public static final ResourceKey<BiomeModifier>
            GEYSER = key("geyser");

    private static ResourceKey<BiomeModifier> key(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, CirCube.asResource(name));
    }

    public static void bootstrap(BootstapContext<BiomeModifier> ctx) {
        HolderGetter<Biome> biomeLookup = ctx.lookup(Registries.BIOME);
        HolderSet<Biome> isOverworld = biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD);
        HolderSet<Biome> isNether = biomeLookup.getOrThrow(BiomeTags.IS_NETHER);

        HolderGetter<PlacedFeature> featureLookup = ctx.lookup(Registries.PLACED_FEATURE);
        Holder<PlacedFeature> geyser = featureLookup.getOrThrow(CirCubePlacedFeatures.GEYSER);

        ctx.register(GEYSER, addDecoration(isOverworld, geyser));
    }

    private static ForgeBiomeModifiers.AddFeaturesBiomeModifier addDecoration(HolderSet<Biome> biomes, Holder<PlacedFeature> feature) {
        return new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.UNDERGROUND_ORES);
    }
}
