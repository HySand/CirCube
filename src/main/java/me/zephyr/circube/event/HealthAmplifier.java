package me.zephyr.circube.event;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static me.zephyr.circube.CirCube.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class HealthAmplifier {
    private static final List<Supplier<EntityType<?>>> BOSS_TYPES = new ArrayList<>();

    public static void initBoss() {
        BOSS_TYPES.add(() -> EntityType.ENDER_DRAGON);
        BOSS_TYPES.add(() -> EntityType.WITHER);
        BOSS_TYPES.add(() -> EntityType.WARDEN);

        BOSS_TYPES.add(ACEntityRegistry.HULLBREAKER::get);
        BOSS_TYPES.add(ACEntityRegistry.FORSAKEN::get);
        BOSS_TYPES.add(ACEntityRegistry.LUXTRUCTOSAURUS::get);
    }

    private static boolean isBoss(EntityType<?> type) {
        return BOSS_TYPES.stream().anyMatch(s -> s.get() == type);
    }

    @SubscribeEvent
    public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (!event.getLevel().isClientSide() && !event.isCanceled()) {
            LivingEntity entity = event.getEntity();
            AttributeInstance healthAttr = entity.getAttribute(Attributes.MAX_HEALTH);

            if (isBoss(entity.getType())) {
                double newHealth = healthAttr.getBaseValue() * 4;
                healthAttr.setBaseValue(newHealth);
                entity.setHealth((float) newHealth);

                AttributeInstance armorAttr = entity.getAttribute(Attributes.ARMOR);
                double newArmor = armorAttr.getBaseValue() * 2;
                armorAttr.setBaseValue(newArmor);
            } else {
                double newHealth = healthAttr.getBaseValue() * 1.5;
                healthAttr.setBaseValue(newHealth);
                entity.setHealth((float) newHealth);

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
}
