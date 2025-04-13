package me.zephyr.circube.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static me.zephyr.circube.CirCube.MOD_ID;

public class HealthAmplifier {
    @Mod.EventBusSubscriber(modid = MOD_ID)
    public class AmplifyHealth {
        @SubscribeEvent
        public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
            if (!event.getLevel().isClientSide() && !event.isCanceled()) {
                LivingEntity entity = event.getEntity();
                AttributeInstance healthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
                if (healthAttr != null) {
                    double newHealth = healthAttr.getBaseValue() * 1.75;
                    healthAttr.setBaseValue(newHealth);
                    entity.setHealth((float) newHealth);
                }
                AttributeInstance armorAttr = entity.getAttribute(Attributes.ARMOR);
                if (armorAttr != null) {
                    double newHealth = armorAttr.getBaseValue() * 1.75;
                    armorAttr.setBaseValue(newHealth);
                    entity.getAttribute(Attributes.ARMOR).setBaseValue(newHealth);
                }
            }
        }
    }
}
