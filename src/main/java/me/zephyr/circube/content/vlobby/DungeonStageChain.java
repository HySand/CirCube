package me.zephyr.circube.content.vlobby;

import java.util.LinkedList;
import java.util.Queue;

public class DungeonStageChain {
    private final Queue<DungeonStage> stages = new LinkedList<>();

    public void addStage(DungeonStage stage) {
        stages.add(stage);
    }

    public void start() {
        runNext();
    }

    private void runNext() {
        DungeonStage stage = stages.poll();
        if (stage != null) {
            stage.start(this::runNext);
        }
    }
}