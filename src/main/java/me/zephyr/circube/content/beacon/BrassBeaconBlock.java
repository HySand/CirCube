package me.zephyr.circube.content.beacon;

import me.zephyr.circube.CirCubeBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BrassBeaconBlock extends MechanicalBeaconBlock{
    public BrassBeaconBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends MechanicalBeaconBlockEntity> getBlockEntityType() {
        return CirCubeBlocks.BRASS_BEACON_ENTITY.get();
    }
}
