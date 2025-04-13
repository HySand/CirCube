package me.zephyr.circube.event;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static me.zephyr.circube.CirCube.LOGGER;
import static me.zephyr.circube.CirCube.MOD_ID;

public class AntiCheat {
    private static final List<String> BANNED_MODS = Arrays.asList("xray", "atianxray", "forgeautofish", "tweakerge", "tweakeroo");

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public class ModChecker {
        @SubscribeEvent
        public static void onLoadComplete(FMLLoadCompleteEvent event) {
            for (String modId : BANNED_MODS) {
                if (ModList.get().isLoaded(modId)) {
                    Minecraft.getInstance().execute(() -> Minecraft.getInstance().stop());
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public class ResourcePackChecker {
        @SubscribeEvent
        public static void onResourceReload(RegisterClientReloadListenersEvent event) {
            Minecraft minecraft = Minecraft.getInstance();
            Collection<Pack> pack = minecraft.getResourcePackRepository().getSelectedPacks();
            LOGGER.info("PacksID: " + pack);
            pack.forEach(p -> {
                LOGGER.info("PackID: " + p.getId());
                if (p.getId().toLowerCase().contains("ray")) {
                    Minecraft.getInstance().execute(() -> Minecraft.getInstance().stop());
                }
            });
        }
    }


}
