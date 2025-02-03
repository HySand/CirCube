package me.zephyr.circube;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import me.zephyr.circube.config.CCConfigs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CirCube.MODID)
public class CirCube {
    public static final String MODID = "circube";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static IEventBus modEventBus;
    public static IEventBus forgeEventBus;
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public CirCube() {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        forgeEventBus = MinecraftForge.EVENT_BUS;

        forgeEventBus.register(this);

        REGISTRATE.registerEventListeners(modEventBus);
        CirCubePackets.register();
        CirCubeBlocks.register();
        CirCubeItems.register();
        CirCubeFluids.register();
        CirCubeCreativeTabs.register(modEventBus);
        CCConfigs.register(ModLoadingContext.get());
        modEventBus.addListener(EventPriority.LOWEST, Registration::gatherData);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CirCubeClient.onCirCubeClient(modEventBus, forgeEventBus));
    }

    public static CreateRegistrate getRegistrate() {
        return REGISTRATE;
    }
}
