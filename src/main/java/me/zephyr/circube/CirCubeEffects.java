package me.zephyr.circube;

import me.zephyr.circube.content.teleport.item.PureLightEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static me.zephyr.circube.CirCube.MOD_ID;

public class CirCubeEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, MOD_ID);

    public static final RegistryObject<MobEffect> PURE_LIGHT =
            EFFECTS.register("pure_light", PureLightEffect::new);

    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
    }
}
