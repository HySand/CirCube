package me.zephyr.circube.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import java.util.Arrays;
import java.util.List;

import static me.zephyr.circube.Circube.MODID;

public class AntiCheat {
    private static final List<String> BANNED_MODS = Arrays.asList("xray", "atianxray", "forgeautofish", "tweakerge", "tweakeroo");

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEventSubscriber {
        @SubscribeEvent
        public static void onLoadComplete(FMLLoadCompleteEvent event) {
            for (String modId : BANNED_MODS) {
                if (ModList.get().isLoaded(modId)) {
                    Minecraft.getInstance().execute(() -> {
                        Minecraft.getInstance().stop();
                    });
                }
            }
        }
    }


}
