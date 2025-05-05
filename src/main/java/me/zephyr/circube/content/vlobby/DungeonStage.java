package me.zephyr.circube.content.vlobby;

public abstract class DungeonStage {
    protected Runnable onComplete;

    public void start(Runnable onComplete) {
        this.onComplete = onComplete;
        runStage();
    }

    protected abstract void runStage();

    protected void complete() {
        if (onComplete != null) onComplete.run();
    }
}
