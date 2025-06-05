package me.zephyr.circube.content.vlobby.dungeons;

import me.zephyr.circube.content.vlobby.Dungeon;
import me.zephyr.circube.content.vlobby.DungeonSpawner;
import me.zephyr.circube.content.vlobby.DungeonStage;
import me.zephyr.circube.content.vlobby.DungeonStageChain;
import me.zephyr.circube.content.vlobby.triggers.DistanceTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;

public class Test extends Dungeon {
    public Test(int dungeonId, String dungeonName, int difficulty, int maxPlayers) {
        super(dungeonId, dungeonName, difficulty, maxPlayers, new BlockPos(84, -50, 188));
    }

    @Override
    protected void initDungeon() {
        DungeonStageChain chain = new DungeonStageChain();
        chain.addStage(new FirstStage());
        chain.addStage(new SecondStage());
        chain.start();
    }

    public class FirstStage extends DungeonStage {
        @Override
        protected void runStage() {
            BlockPos pos = new BlockPos(43, -46, 46);
            Runnable runnable = () -> {
                Runnable complete = this::complete;
                DungeonSpawner spawner = new DungeonSpawner(level, new BlockPos(43, -45, 36), EntityType.ZOMBIE, complete);
                spawner.start();
            };
            DistanceTrigger distanceTrigger = new DistanceTrigger(pos, level, 25, false, runnable);
            distanceTrigger.start();
        }
    }

    public class SecondStage extends DungeonStage {
        @Override
        protected void runStage() {
            BlockPos pos = new BlockPos(42, -45, 14);
            Runnable runnable = () -> {
                Runnable complete = Test.this::missionComplete;
                DungeonSpawner spawner = new DungeonSpawner(level, new BlockPos(42, -45, 1), EntityType.ZOMBIE, complete);
                spawner.start();
            };
            DistanceTrigger distanceTrigger = new DistanceTrigger(pos, level, 25, false, runnable);
            distanceTrigger.start();
        }
    }
}
