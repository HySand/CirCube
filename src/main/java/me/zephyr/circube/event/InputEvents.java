package me.zephyr.circube.event;

import me.zephyr.circube.CirCubeKeys;
import me.zephyr.circube.content.vlobby.LobbyScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class InputEvents {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (Minecraft.getInstance().screen != null)
            return;
        if (CirCubeKeys.VLOBBY_MENU.isPressed() && !Minecraft.getInstance().isSingleplayer()) {
            Minecraft.getInstance().setScreen(new LobbyScreen());
        }
    }
}
