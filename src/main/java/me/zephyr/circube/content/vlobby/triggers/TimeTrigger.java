package me.zephyr.circube.content.vlobby.triggers;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static me.zephyr.circube.CirCube.forgeEventBus;

public class TimeTrigger extends Trigger {
    private final Runnable action;
    private final long intervalTicks;
    private int ticks;
    private boolean active;

    public TimeTrigger(long intervalTicks, boolean repeat, Runnable action) {
        super(repeat);
        this.intervalTicks = intervalTicks;
        this.action = action;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event) {
        if (!active || event.phase != TickEvent.Phase.END) return;
        ticks++;
        if (ticks >= intervalTicks) {
            action.run();
            if (repeat) {
                ticks = 0;
            } else {
                stop();
            }
        }
    }

    @Override
    public void start() {
        ticks = 0;
        active = true;
        forgeEventBus.register(this);
    }

    @Override
    public void stop() {
        active = false;
        forgeEventBus.unregister(this);
    }
}
