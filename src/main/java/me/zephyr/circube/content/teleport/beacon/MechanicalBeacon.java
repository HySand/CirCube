package me.zephyr.circube.content.teleport.beacon;

import net.minecraft.core.BlockPos;

public interface MechanicalBeacon {
    String getBeaconId();

    String getBeaconName();

    BlockPos getPos();

    BlockPos getOwner();
}
