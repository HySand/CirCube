package me.zephyr.circube.compact;

import com.simibubi.create.compat.jei.GhostIngredientHandler;
import me.zephyr.circube.CirCube;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import me.zephyr.circube.content.beacon.MechanicalBeaconScreen;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class CirCubeJEI implements IModPlugin {
    private static final ResourceLocation ID = CirCube.asResource("jei_plugin");
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(MechanicalBeaconScreen.class, new GhostIngredientHandler());
    }
}
