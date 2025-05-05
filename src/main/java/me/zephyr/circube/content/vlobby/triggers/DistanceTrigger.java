package me.zephyr.circube.content.vlobby.triggers;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static me.zephyr.circube.CirCube.forgeEventBus;

public class DistanceTrigger extends Trigger {
    private final Entity targetEntity;
    private final BlockPos targetPos;
    private final ServerLevel targetLevel;
    private final double range;
    private final Runnable action;
    private boolean active;

    public DistanceTrigger(Entity target, double range, boolean repeat, Runnable action) {
        super(repeat);
        this.targetPos = null;
        this.targetEntity = target;
        this.targetLevel = (ServerLevel) target.level();
        this.range = range;
        this.action = action;
    }

    public DistanceTrigger(BlockPos pos, ServerLevel level, double range, boolean repeat, Runnable action) {
        super(repeat);
        this.targetEntity = null;
        this.targetPos = pos;
        this.targetLevel = level;
        this.range = range;
        this.action = action;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event) {
        if (!active || event.phase != TickEvent.Phase.END) return;

        for (ServerPlayer player : targetLevel.players()) {
            if (targetEntity == null) {
                if (player.distanceToSqr(targetPos.getX(), targetPos.getY(), targetPos.getZ()) <= range) {
                    action.run();
                    if (!repeat) stop();
                    break;
                }
            } else {
                if (targetEntity.isRemoved()) return;
                if (player.distanceTo(targetEntity) <= range) {
                    action.run();
                    if (!repeat) stop();
                    break;
                }
            }
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
