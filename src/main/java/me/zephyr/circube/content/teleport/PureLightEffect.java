package me.zephyr.circube.content.teleport;

import me.zephyr.circube.CirCubeEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class PureLightEffect extends MobEffect {
    public PureLightEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x98D982);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }

    @Mod.EventBusSubscriber()
    public class PersistEffect {
        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            if (event.isWasDeath()) {
                Player oldPlayer = event.getOriginal();
                Player newPlayer = event.getEntity();

                MobEffectInstance effect = oldPlayer.getEffect(CirCubeEffects.PURE_LIGHT.get());
                if (effect != null) {
                    newPlayer.addEffect(new MobEffectInstance(
                            CirCubeEffects.PURE_LIGHT.get(),
                            effect.getDuration(),
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.isVisible(),
                            effect.showIcon()
                    ));
                }
            }
        }
    }

}

