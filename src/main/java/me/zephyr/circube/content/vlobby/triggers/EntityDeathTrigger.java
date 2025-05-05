package me.zephyr.circube.content.vlobby.triggers;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static me.zephyr.circube.CirCube.forgeEventBus;

public class EntityDeathTrigger extends Trigger {
    private final Entity target;
    private final Runnable action;

    public EntityDeathTrigger(Entity target, boolean repeat, Runnable action) {
        super(repeat);
        this.target = target;
        this.action = action;
        if (repeat) throw new IllegalArgumentException("实体死亡触发器不应重复");
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntity() == target) {
            action.run();
            stop();
        }
    }

    @Override
    public void start() {
        forgeEventBus.register(this);
    }

    @Override
    public void stop() {
        forgeEventBus.unregister(this);
    }
}
