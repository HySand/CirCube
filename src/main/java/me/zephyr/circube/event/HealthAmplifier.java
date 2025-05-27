package me.zephyr.circube.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static me.zephyr.circube.CirCube.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class HealthAmplifier {

    @SubscribeEvent
    public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (!event.getLevel().isClientSide() && !event.isCanceled()) {
            LivingEntity entity = event.getEntity();
            AttributeInstance healthAttr = entity.getAttribute(Attributes.MAX_HEALTH);
            if (healthAttr != null) {
                double newHealth = healthAttr.getBaseValue() * 1.5;
                healthAttr.setBaseValue(newHealth);
                entity.setHealth((float) newHealth);
            }

            if (entity.getMobType() == MobType.UNDEAD) {
                AttributeInstance armorAttr = entity.getAttribute(Attributes.ARMOR);
                double newArmor = armorAttr.getBaseValue() * 3 + 12;
                armorAttr.setBaseValue(newArmor);
            } else if (entity.getMobType() == MobType.ARTHROPOD) {
                AttributeInstance damage = entity.getAttribute(Attributes.ATTACK_DAMAGE);
                double newDamage = damage.getBaseValue() * 2;
                damage.setBaseValue(newDamage);

                AttributeInstance speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
                double newSpeed = speed.getBaseValue() * 1.25;
                speed.setBaseValue(newSpeed);
            }
        }
    }
}
