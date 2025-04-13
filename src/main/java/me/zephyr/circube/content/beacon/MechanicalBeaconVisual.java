package me.zephyr.circube.content.beacon;


import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static me.zephyr.circube.content.beacon.MechanicalBeaconBlock.HALF;

public class MechanicalBeaconVisual extends KineticBlockEntityVisual<MechanicalBeaconBlockEntity> {
    private final RotatingInstance shaft;

    public MechanicalBeaconVisual(VisualizationContext context, MechanicalBeaconBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        float speed = blockEntity.getSpeed();
        if (level.getBlockState(blockEntity.getBlockPos()).getValue(HALF) == DoubleBlockHalf.LOWER) {
            shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                    .createInstance();
            shaft.setup(blockEntity, speed)
                    .setPosition(getVisualPosition())
                    .rotateToFace(Direction.SOUTH)
                    .setChanged();
        } else {
            shaft = null;
        }

    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        if (shaft != null)
            consumer.accept(shaft);
    }

    @Override
    public void updateLight(float v) {
        if (shaft != null)
            relight(shaft);
    }

    @Override
    protected void _delete() {
        if (shaft != null)
            shaft.delete();
    }
}
