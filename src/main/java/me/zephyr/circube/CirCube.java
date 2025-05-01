package me.zephyr.circube;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import me.zephyr.circube.config.CirCubeConfigs;
import me.zephyr.circube.content.vlobby.RoomEntry;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

import static me.zephyr.circube.util.DataManager.addGameToList;

@Mod(CirCube.MOD_ID)
public class CirCube {
    public static final String MOD_ID = "circube";
    public static final String MOD_NAME = "CirCube";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static IEventBus modEventBus;
    public static IEventBus forgeEventBus;

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public CirCube(FMLJavaModLoadingContext context) {
        modEventBus = context.getModEventBus();
        forgeEventBus = MinecraftForge.EVENT_BUS;

        forgeEventBus.register(this);
        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER)
            modEventBus.addListener(this::onDedicatedServerSetup);

        REGISTRATE.registerEventListeners(modEventBus);

        CirCubePackets.register();
        CirCubeBlocks.register();
        CirCubeItems.register();
        CirCubeFluids.register();
        CirCubeMenuTypes.register();
        CirCubeCreativeTabs.register(modEventBus);
        CirCubeConfigs.register(context);
        modEventBus.addListener(EventPriority.LOWEST, Registration::gatherData);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CirCubeClient.onCirCubeClient(modEventBus, forgeEventBus));
    }

    private void onDedicatedServerSetup(FMLDedicatedServerSetupEvent event) {
        RoomEntry entry1 = new RoomEntry("1", 0, 4);
        RoomEntry entry2 = new RoomEntry("2", 1, 8);
        RoomEntry entry3 = new RoomEntry("3", 2, 1);
        RoomEntry entry4 = new RoomEntry("4", 3, 2);
        addGameToList(entry1);
        addGameToList(entry2);
        addGameToList(entry3);
        addGameToList(entry4);
        CirCube.LOGGER.info("Adding games to list");
    }

    public static CreateRegistrate getRegistrate() {
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
