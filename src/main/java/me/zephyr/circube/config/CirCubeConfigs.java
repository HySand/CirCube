package me.zephyr.circube.config;


import com.simibubi.create.api.stress.BlockStressValues;
import net.createmod.catnip.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CirCubeConfigs {

    private static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

    private static CServer server;

    public static CServer server() {
        return server;
    }

    public static <T> Supplier<T> safeGetter(Supplier<T> getter, T defaultValue) {
        return () -> {
            try {
                return getter.get();
            } catch (IllegalStateException | NullPointerException ex) {
                // the config is accessed too early (before registration or before config load)
                return defaultValue;
            }
        };
    }

    public static ConfigBase byType(ModConfig.Type type) {
        return CONFIGS.get(type);
    }

    private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> {
            T config = factory.get();
            config.registerAll(builder);
            return config;
        });

        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }

    public static void register(ModLoadingContext context) {
        server = register(CServer::new, ModConfig.Type.SERVER);

        for (Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
            context.registerConfig(pair.getKey(), pair.getValue().specification);

        CStress stress = server().stressValues;
        BlockStressValues.IMPACTS.registerProvider(stress::getImpact);
        BlockStressValues.CAPACITIES.registerProvider(stress::getCapacity);
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == event.getConfig()
                    .getSpec())
                config.onLoad();
    }

    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == event.getConfig()
                    .getSpec())
                config.onReload();
    }

}
