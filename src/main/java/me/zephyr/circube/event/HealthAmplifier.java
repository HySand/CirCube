package me.zephyr.circube.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static me.zephyr.circube.CirCube.MODID;

public class HealthAmplifier {
    @Mod.EventBusSubscriber(modid = MODID)
    public class AmplifyHealth {
        @SubscribeEvent
        public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
            if (!event.getLevel().isClientSide() && !event.isCanceled()) {
                LivingEntity entity = event.getEntity();
                AttributeInstance healthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
                if (healthAttr != null) {
                    double newHealth = healthAttr.getBaseValue() * 2.5;
                    healthAttr.setBaseValue(newHealth);
                    entity.setHealth((float) newHealth);
                }
            }
        }
    }
}
