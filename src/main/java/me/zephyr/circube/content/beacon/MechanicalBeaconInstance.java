package me.zephyr.circube.content.beacon;

import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class MechanicalBeaconInstance extends KineticBlockEntityInstance<MechanicalBeaconBlockEntity> {
    protected final RotatingData shaft;

    public MechanicalBeaconInstance(MaterialManager materialManager, MechanicalBeaconBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        shaft = getRotatingMaterial().getModel(AllPartialModels.SHAFT_HALF, blockState, Direction.DOWN).createInstance();
        setup(shaft);
    }

    @Override
    public void update() {
        updateRotation(shaft);
    }

    @Override
    public void updateLight() {
        BlockPos behind = pos.relative(Direction.DOWN);
        relight(behind, shaft);
    }

    @Override
    protected void remove() {
        shaft.delete();
    }
}
