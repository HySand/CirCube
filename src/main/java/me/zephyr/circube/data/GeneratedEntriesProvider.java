package me.zephyr.circube.data;

import me.zephyr.circube.CirCube;
import me.zephyr.circube.worldgen.CirCubeBiomeModifiers;
import me.zephyr.circube.worldgen.CirCubeConfiguredFeatures;
import me.zephyr.circube.worldgen.CirCubePlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GeneratedEntriesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, CirCubeConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, CirCubePlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, CirCubeBiomeModifiers::bootstrap);

    public GeneratedEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(CirCube.MOD_ID));
    }

    @Override
    public String getName() {
        return "CirCube's Generated Registry Entries";
    }
}
