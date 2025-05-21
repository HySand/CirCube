package me.zephyr.circube.worldgen;

import me.zephyr.circube.CirCube;
import me.zephyr.circube.content.geyser.GeyserFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CirCubeFeatures {
    private static final DeferredRegister<Feature<?>> REGISTER = DeferredRegister.create(ForgeRegistries.FEATURES, CirCube.MOD_ID);

    public static final RegistryObject<GeyserFeature> GEYSER = REGISTER.register("geyser", GeyserFeature::new);

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
