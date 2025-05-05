package me.zephyr.circube.content.vlobby.triggers;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static me.zephyr.circube.CirCube.forgeEventBus;

public class PotionExpireTrigger extends Trigger {
    private final ServerPlayer player;
    private final MobEffect effect;
    private final Runnable action;
    private boolean active;

    public PotionExpireTrigger(ServerPlayer player, MobEffect effect, boolean repeat, Runnable action) {
        super(repeat);
        this.player = player;
        this.effect = effect;
        this.action = action;
    }

    @SubscribeEvent
    public void onEffectRemoved(MobEffectEvent.Remove event) {
        if (!active) return;
        if (event.getEntity() == player && event.getEffect() != null && event.getEffect() == effect) {
            action.run();
            if (!repeat) stop();
        }
    }

    @Override
    public void start() {
        active = true;
        forgeEventBus.register(this);
    }

    @Override
    public void stop() {
        active = false;
        forgeEventBus.unregister(this);
    }
}
