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

import static me.zephyr.circube.CirCube.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class AntiCheat {
    private static final List<String> BANNED_MODS = Arrays.asList("xray", "atianxray", "forgeautofish", "tweakerge", "tweakeroo", "litematica", "baritone");

    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        for (String modId : BANNED_MODS) {
            if (ModList.get().isLoaded(modId)) {
                Minecraft.getInstance().execute(() -> Minecraft.getInstance().stop());
            }
        }
    }

    @SubscribeEvent
    public static void onResourceReload(RegisterClientReloadListenersEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Collection<Pack> pack = minecraft.getResourcePackRepository().getSelectedPacks();
        pack.forEach(p -> {
            if (p.getId().toLowerCase().contains("ray")) {
                Minecraft.getInstance().execute(() -> Minecraft.getInstance().stop());
            }
        });
    }


}
