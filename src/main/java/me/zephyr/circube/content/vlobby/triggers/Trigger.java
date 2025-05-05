package me.zephyr.circube.content.vlobby.triggers;

public abstract class Trigger {
    protected boolean repeat;

    public Trigger(boolean repeat) {
        this.repeat = repeat;
    }

    public abstract void start();

    public abstract void stop();
}
