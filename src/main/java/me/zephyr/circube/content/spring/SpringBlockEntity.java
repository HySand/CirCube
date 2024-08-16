package me.zephyr.circube.content.spring;

import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static me.zephyr.circube.content.spring.SpringBlock.POWERED;

public class SpringBlockEntity extends SplitShaftBlockEntity {
    private static final int TICK_INTERVAL = 3;
    private int tickTimer = 0;

    private static final int MAX_STRESS = 3;
    private int currentStress = 0;


    public SpringBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public float getRotationSpeedModifier(Direction face) {
        return 1;
    }

    @Override
    public void tick() {
        super.tick();


        if (tickTimer-- < 0) {
            tickTimer = TICK_INTERVAL;
            float absSpeed = Mth.abs(getSpeed());
            if (level.isClientSide()) {
                if (absSpeed > 0) {
                    Vec3 loc = Vec3.atBottomCenterOf(getBlockPos());
                    level.addParticle(ParticleTypes.LARGE_SMOKE, false, loc.x, loc.y + 0.5, loc.z, 0, 0.05, 0);
                }
            }
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("Stress", currentStress);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        currentStress = compound.getInt("Stress");
        super.read(compound, clientPacket);
    }
}
