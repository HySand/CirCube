package me.zephyr.circube.content.spring;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import me.zephyr.circube.CirCube;
import me.zephyr.circube.CirCubeBlocks;
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
    private int springCount = 0;
    public int signal = 0;

    private float BASE_IMPACT = (float) BlockStressValues.getImpact(getStressConfigKey());
    private final float EXTRA_IMPACT = 4;

    public SpringBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public float getGeneratedSpeed() {
        if (stressInside > 19) {
            return 4;
        } else if (stressInside < -19) {
            return -4;
        } else {
            return 0;
        }
    }

    @Override
    public float calculateStressApplied() {
        float impact;
        /*
        if (isCharging() && capacity >= stress && speed != 0) {
            impact += (float) Math.floor((capacity - stress) / Math.abs(speed));
        } else {
            impact = 2;
        }
        */
        if (isCharging()) {
            impact = BASE_IMPACT + EXTRA_IMPACT;
        } else {
            impact = BASE_IMPACT;
        }
        lastStressApplied = impact;
        return impact;
    }

    @Override
    public void tick() {
        super.tick();
        updateGeneratedRotation();

        //充能
        if (isCharging()) {
            if (speed > 0) {
                setSignal(8);
                stressInside = Math.min(stressInside + EXTRA_IMPACT * speed, MAX_POWER);
            } else {
                setSignal(8);
                stressInside = Math.max(stressInside + EXTRA_IMPACT * speed, -MAX_POWER);
            }
        } else {
            if (stressInside == 0) {
                setSignal(4);
            } else {
                setSignal(0);
            }
        }

        //消耗
        if (!level.isClientSide() && springCount > 0) {
            if (stressInside > 0) {
                if (isOverStressed()) {
                    stressInside = Math.max(stressInside - BASE_IMPACT * 4, 0);
                } else {
                    CirCube.LOGGER.info(stressInside + " " + stress + " " + speed + " " + springCount);
                    if (isCharging()) {
                        stressInside = Math.max(stressInside - (stress - EXTRA_IMPACT * speed) / springCount, 0);
                    } else {
                        stressInside = Math.max(stressInside - stress / springCount, 0);
                    }

                }
            } else if (stressInside < 0) {
                if (isOverStressed()) {
                    stressInside = Math.min(stressInside + BASE_IMPACT * 4, 0);
                } else {
                    if (isCharging()) {
                        stressInside = Math.min(stressInside + (stress + EXTRA_IMPACT * speed) / springCount, 0);
                    } else {
                        stressInside = Math.min(stressInside + stress / springCount, 0);
                    }
                }
            }
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        BASE_IMPACT = (float) BlockStressValues.getImpact(getStressConfigKey());

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
        compound.putFloat("StressInside", stressInside);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        stressInside = compound.getFloat("StressInside");
    }

    private boolean isCharging() {
        if (level.isClientSide() || isOverStressed()) {
            return false;
        }

        KineticNetwork kineticNetwork = getOrCreateNetwork();
        if (kineticNetwork == null) {
            return false;
        }

        int count = 0;
        for (KineticBlockEntity member : kineticNetwork.sources.keySet()) {
            if (member instanceof SpringBlockEntity && member.getGeneratedSpeed() != 0) count++;
        }
        springCount = count;

        for (KineticBlockEntity source : kineticNetwork.sources.keySet()) {
            if (source.getBlockPos() != getBlockPos() && source.getGeneratedSpeed() != 0) {
                return true;
            }
        }
        return false;
    }

    private void setSignal(int strength) {
        signal = strength;
        level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
    }
}
