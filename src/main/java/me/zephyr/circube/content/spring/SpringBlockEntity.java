package me.zephyr.circube.content.spring;

import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import me.zephyr.circube.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class SpringBlockEntity extends GeneratingKineticBlockEntity {

    private static final float MAX_POWER = 307200;
    private float stressInside = 0;
    private float currentStress = 0;
    public int signal = 0;

    private float BASE_IMPACT = (float) BlockStressValues.getImpact(getStressConfigKey());
    private float BASE_CAPACITY = (float) BlockStressValues.getImpact(getStressConfigKey());

    public SpringBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public float getGeneratedSpeed() {
        if (stressInside > 0) {
            return 8;
        } else if (stressInside < 0) {
            return -8;
        } else {
            return 0;
        }
    }

    @Override
    public float calculateStressApplied() {
        float impact = Math.max(currentStress, BASE_IMPACT);
        KineticNetwork kineticNetwork = getOrCreateNetwork();
        if (isCharging(kineticNetwork) && capacity >= stress && speed != 0) {
            impact += (float) Math.floor((capacity - stress) / Math.abs(speed));
        } else {
            impact = 2;
        }
        currentStress = impact;
        lastStressApplied = impact;
        return impact;
    }

    @Override
    public void tick() {
        super.tick();
        updateGeneratedRotation();
        if (!level.isClientSide()) {
            KineticNetwork kineticNetwork = getOrCreateNetwork();
            //充能
            if (isCharging(kineticNetwork)) {
                if (speed > 0) {
                    signal = 8;
                    stressInside = Math.min(stressInside + currentStress * speed, MAX_POWER);
                } else {
                    signal = 8;
                    stressInside = Math.max(stressInside + currentStress * speed, -MAX_POWER);
                }
            } else {
                if (stressInside == 0) signal = 4;
                else signal = 0;
            }
            level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());

            //消耗
            if (stressInside > 0) {
                if (isOverStressed()) {
                    stressInside = Math.max(stressInside - BASE_IMPACT * 8, 0);
                } else {
                    stressInside = Math.max(stressInside - stress - (BASE_IMPACT - currentStress) * 8, 0);
                }
            } else if (stressInside < 0) {
                if (isOverStressed()) {
                    stressInside = Math.min(stressInside + BASE_IMPACT * 8, 0);
                } else {
                    stressInside = Math.min(stressInside + stress + (BASE_IMPACT - currentStress) * 8, 0);
                }
            }
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        BASE_IMPACT = (float) BlockStressValues.getImpact(getStressConfigKey());
        BASE_CAPACITY = (float) BlockStressValues.getCapacity(getStressConfigKey());
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        Lang.number((int) Math.abs(stressInside / 20))
                .translate("generic.unit.stress")
                .style(ChatFormatting.AQUA)
                .space()
                .add(Lang.translate("gui.goggles.stress_inside")
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);
        return added;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putFloat("CurrentStress", currentStress);
        compound.putFloat("StressInside", stressInside);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        currentStress = compound.getFloat("CurrentStress");
        stressInside = compound.getFloat("StressInside");
    }

    private boolean isCharging(KineticNetwork network) {
        if (network == null) return false;
        for (KineticBlockEntity source : network.sources.keySet()) {
            if (!(source instanceof SpringBlockEntity)) return true;
        }
        return false;
    }
}
