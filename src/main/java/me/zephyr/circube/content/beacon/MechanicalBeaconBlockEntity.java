package me.zephyr.circube.content.beacon;

import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import me.zephyr.circube.Lang;
import me.zephyr.circube.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

import static net.minecraft.util.Mth.clamp;

public class MechanicalBeaconBlockEntity extends KineticBlockEntity {
    public float lookingRotR = 0;
    private float turningSpeedR = 2;
    private String name = "";
    private String hash;
    private UUID owner = null;
    private String ownerName = null;

    public MechanicalBeaconBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.name = getBeaconName();
    }


    @Override
    public void tick() {
        super.tick();
        var closestPlayer = level.getNearestPlayer(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 5.5, false);
        if (closestPlayer != null && getBlockState().getValue(MechanicalBeaconBlock.ACTIVE)) {
            addParticle(closestPlayer);
            double x = closestPlayer.getX() - worldPosition.getX() - 0.5D;
            double z = closestPlayer.getZ() - worldPosition.getZ() - 0.5D;
            float rotY = (float) ((float) Math.atan2(z, x) / Math.PI * 180 + 180);
            moveOnTickR(rotY);
        } else {
            lookingRotR += 2;
        }
        lookingRotR = rotClamp(360, lookingRotR);

        KineticNetwork kineticNetwork = getOrCreateNetwork();
        if (kineticNetwork != null && capacity >= stress && lastStressApplied > 0) {
            level.setBlock(getBlockPos(), getBlockState().setValue(MechanicalBeaconBlock.ACTIVE, true), 2);
        } else {
            level.setBlock(getBlockPos(), getBlockState().setValue(MechanicalBeaconBlock.ACTIVE, false), 2);
        }
    }

    private float rotClamp(int clampTo, float value) {
        if (value >= clampTo) {
            return value - clampTo;
        } else if (value < 0) {
            return value + clampTo;
        } else {
            return value;
        }
    }

    private boolean checkBound(int amount, float rot) {
        float Rot = Math.round(rot);
        float Rot2 = rotClamp(360, Rot + 180);
        return ((Rot - amount <= lookingRotR && lookingRotR <= Rot + amount) || (
                Rot2 - amount <= lookingRotR && lookingRotR <= Rot2 + amount));
    }

    private void moveOnTickR(float rot) {
        if (!checkBound(2, rot)) {
            double check = (rotClamp(180, rot) - rotClamp(180, lookingRotR) + 180) % 180;
            if (check < 90) {
                lookingRotR += turningSpeedR;
            } else {
                lookingRotR -= turningSpeedR;
            }
            lookingRotR = rotClamp(360, lookingRotR);
            if (checkBound(10, rot)) {
                turningSpeedR = 2;
            } else {
                turningSpeedR += 1;
                turningSpeedR = clamp(turningSpeedR, 2, 20);
            }
        }
    }

    private void addParticle(Player player) {
        if (level == null) {
            return;
        }
        var r = level.getRandom();
        Vec3 playerPos = player.position();
        SimpleParticleType p = (r.nextInt(10) > 7) ? ParticleTypes.ENCHANT : ParticleTypes.PORTAL;

        int j = r.nextInt(2) * 2 - 1;
        int k = r.nextInt(2) * 2 - 1;

        double y = worldPosition.getY() + 1;

        int rd = r.nextInt(10);
        if (rd > 5) {
            if (p == ParticleTypes.ENCHANT) {
                level.addParticle(p, playerPos.x, playerPos.y + 1.5D, playerPos.z,
                        (worldPosition.getX() + 0.5D - playerPos.x), (y - 1.25D - playerPos.y),
                        (worldPosition.getZ() + 0.5D - playerPos.z));
            } else {
                level.addParticle(p, worldPosition.getX() + 0.5D, y + 0.8D,
                        worldPosition.getZ() + 0.5D,
                        (playerPos.x - worldPosition.getX()) - r.nextDouble(),
                        (playerPos.y - worldPosition.getY() - 0.5D) - r.nextDouble() * 0.5D,
                        (playerPos.z - worldPosition.getZ()) - r.nextDouble());
            }
        }
        if (rd > 8) {
            level.addParticle(p, y + 0.5D, worldPosition.getY() + 0.8D,
                    worldPosition.getZ() + 0.5D,
                    r.nextDouble() * j, (r.nextDouble() - 0.25D) * 0.125D, r.nextDouble() * k);
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        if (name != null)
            compound.putString("Name", name);
        if (hash != null)
            compound.putString("Id", hash);
        if (owner != null)
            compound.putUUID("Owner", owner);
        if (ownerName != null)
            compound.putString("OwnerName", ownerName);
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if (compound.contains("Name"))
            name = compound.getString("Name");
        if (compound.contains("Id"))
            hash = compound.getString("Id");
        if (compound.contains("Owner"))
            owner = compound.getUUID("Owner");
        if (compound.contains("OwnerName"))
            ownerName = compound.getString("OwnerName");
    }

    public String getBeaconId() {
        return hash = Utils.getOrCreateHashString(hash, level, worldPosition);
    }

    public void setBeaconId(String id) {
        hash = id;
    }

    public String getBeaconName() {
        return name = Utils.getOrCreateBeaconName(name);
    }

    public void setBeaconName(String beaconName) {
        name = beaconName;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(String player) {
        ownerName = player;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwner(UUID player) {
        owner = player;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        Lang.translate("gui.goggles.beaconStats")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        if (name != null && !name.isEmpty()) {
            Lang.translate("tooltip.beacon_name")
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip);
            Lang.text(name)
                    .style(ChatFormatting.AQUA)
                    .forGoggles(tooltip);
        }
        if (ownerName != null) {
            Lang.translate("tooltip.beacon_owner")
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip);
            Lang.text(ownerName)
                    .style(ChatFormatting.AQUA)
                    .forGoggles(tooltip);
        }

        return added;
    }
}
