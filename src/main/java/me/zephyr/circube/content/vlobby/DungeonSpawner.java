package me.zephyr.circube.content.vlobby;

import me.zephyr.circube.content.vlobby.triggers.EntityDeathTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DungeonSpawner {

    public DungeonSpawner(ServerLevel level, BlockPos pos,
                          EntityType<?> type,
                          Runnable onFinish) {
        this(level, List.of(pos), type, 0, 1, onFinish, true);
    }

    public DungeonSpawner(ServerLevel level, BlockPos pos,
                          EntityType<?> type,
                          Runnable onFinish, boolean allMustDie) {
        this(level, List.of(pos), type, 0, 1, onFinish, allMustDie);
    }

    public DungeonSpawner(ServerLevel level, Collection<BlockPos> poses,
                          EntityType<?> type,
                          Runnable onFinish, boolean allMustDie) {
        this(level, poses, type, 0, 1, onFinish, allMustDie);
    }

    public DungeonSpawner(ServerLevel level, BlockPos pos,
                          EntityType<?> type,
                          int intervalTicks) {
        this(level, List.of(pos), type, intervalTicks, 1, null, false);
    }

    public DungeonSpawner(ServerLevel level, BlockPos pos,
                          EntityType<?> type,
                          int intervalTicks, int maxPerPos) {
        this(level, List.of(pos), type, intervalTicks, maxPerPos, null, false);
    }

    public DungeonSpawner(ServerLevel level, Collection<BlockPos> poses,
                          EntityType<?> type,
                          int intervalTicks, int maxPerPos) {
        this(level, poses, type, intervalTicks, maxPerPos, null, false);
    }

    private final ServerLevel level;
    private final List<BlockPos> spawnPos;
    private final EntityType<?> entityType;
    private final int interval;
    private final int maxPerPos;
    private final Runnable onFinish;
    private final boolean allMustDie;

    private final Map<BlockPos, List<Entity>> spawned = new HashMap<>();
    private boolean running;

    private DungeonSpawner(ServerLevel level,
                           Collection<BlockPos> poses,
                           EntityType<?> type,
                           int intervalTicks, int maxPerPos,
                           Runnable onFinish, boolean allMustDie) {
        this.level = level;
        this.spawnPos = poses.stream().map(BlockPos::immutable).toList();
        this.entityType = type;
        this.interval = intervalTicks;
        this.maxPerPos = maxPerPos;
        this.onFinish = onFinish;
        this.allMustDie = allMustDie;
        poses.forEach(p -> spawned.put(p, new ArrayList<>()));
    }

    public void start() {
        if (running) return;
        running = true;

        if (interval > 0) runLoop();
        else spawnOnce();
    }

    public void cancel() {
        running = false;
        spawned.clear();
    }

    private void spawnOnce() {
        AtomicInteger aliveCounter = new AtomicInteger(0);

        for (BlockPos pos : spawnPos) {
            Entity e = spawnOne(pos);
            if (e == null) continue;
            spawned.get(pos).add(e);
            aliveCounter.incrementAndGet();

            if (onFinish != null) {
                if (allMustDie) {
                    EntityDeathTrigger t = new EntityDeathTrigger(e, false, () -> {
                        if (aliveCounter.decrementAndGet() == 0) {
                            onFinish.run();
                            cancel();
                        }
                    });
                    t.start();
                } else {
                    EntityDeathTrigger t = new EntityDeathTrigger(e, false, () -> {
                        onFinish.run();
                        cancel();
                    });
                    t.start();
                    break;
                }
            }
        }
    }

    private void runLoop() {
        level.getServer().execute(() -> {
            if (!running) return;

            for (BlockPos pos : spawnPos) {
                spawned.get(pos).removeIf(e -> !e.isAlive());

                if (spawned.get(pos).size() < maxPerPos) {
                    Entity e = spawnOne(pos);
                    if (e != null) spawned.get(pos).add(e);
                }
            }
            level.scheduleTick(BlockPos.ZERO,
                    level.getBlockState(BlockPos.ZERO).getBlock(), interval);
            runLoop();
        });
    }

    @Nullable
    private Entity spawnOne(BlockPos pos) {
        Entity mob = entityType.create(level);
        if (mob == null) return null;
        mob.moveTo(pos.getCenter());
        level.addFreshEntity(mob);
        return mob;
    }
}
