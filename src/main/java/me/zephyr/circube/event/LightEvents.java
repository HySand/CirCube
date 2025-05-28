package me.zephyr.circube.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static me.zephyr.circube.CirCube.MOD_ID;
import static me.zephyr.circube.content.light.AndesiteLightBlockEntity.activatedAndesiteLight;
import static me.zephyr.circube.content.light.BrassLightBlockEntity.activatedBrassLight;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class LightEvents {

    @SubscribeEvent
    public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (!event.isCanceled() && event.getEntity() instanceof Enemy && event.getSpawnType() == MobSpawnType.NATURAL) {
            Entity entity = event.getEntity();
            if (isNearLight(entity)) {
                event.setResult(Event.Result.DENY);
                event.setSpawnCancelled(true);
                event.setCanceled(true);
            }
        }
    }

    private static boolean isNearLight(Entity entity) {
        for (BlockPos pos : activatedAndesiteLight.keySet()) {
            if (pos.distSqr(entity.getOnPos()) < activatedAndesiteLight.get(pos) / 2) {
                return true;
            }
        }
        for (BlockPos pos : activatedBrassLight.keySet()) {
            if (pos.distSqr(entity.getOnPos()) < activatedBrassLight.get(pos) / 2) {
                return true;
            }
        }
        return false;
    }
}
