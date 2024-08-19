package me.zephyr.circube;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class CirCubeClient {
    public static void onCirCubeClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(CirCubeClient::init);
    }

    public static void init(final FMLClientSetupEvent event) {
        CirCubePonders.register();
    }
}
