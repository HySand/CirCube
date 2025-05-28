package me.zephyr.circube.content.teleport.beacon;

import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import me.zephyr.circube.CirCubeLang;
import me.zephyr.circube.util.DataManager;
import me.zephyr.circube.util.Utils;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static me.zephyr.circube.content.teleport.beacon.MechanicalBeaconBlock.HALF;
import static me.zephyr.circube.util.DataManager.clientBeaconList;
import static net.minecraft.util.Mth.clamp;

public class MechanicalBeaconBlockEntity extends KineticBlockEntity implements MenuProvider {
    public float lookingRotR = 0;
    private float turningSpeedR = 2;
    private String name = "";
    private String hash;
    private UUID owner = null;
    private String ownerName = null;
    private String icon = null;
    private PositionControl positionMode = PositionControl.NORTH;

    public MechanicalBeaconBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        if (getBlockState().getValue(HALF) == DoubleBlockHalf.LOWER) {
            if (level.isClientSide()) {
                var closestPlayer = level.getNearestPlayer(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 5.5, false);
                if (closestPlayer != null && clientBeaconList.contains(hash)) {
                    addParticle(closestPlayer);
                    double x = closestPlayer.getX() - worldPosition.getX() - 0.5D;
                    double z = closestPlayer.getZ() - worldPosition.getZ() - 0.5D;
                    float rotY = (float) ((float) Math.atan2(z, x) / Math.PI * 180 + 180);
                    moveOnTickR(rotY);
                } else {
                    lookingRotR += 2;
                }
                lookingRotR = rotClamp(360, lookingRotR);
            }

            KineticNetwork kineticNetwork = getOrCreateNetwork();
            if (kineticNetwork != null && capacity >= stress && lastStressApplied > 0) {
                level.setBlockAndUpdate(getBlockPos().above(), getBlockState().setValue(MechanicalBeaconBlock.ACTIVE, true).setValue(HALF, DoubleBlockHalf.UPPER));
                level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(MechanicalBeaconBlock.ACTIVE, true));
            } else {
                level.setBlockAndUpdate(getBlockPos().above(), getBlockState().setValue(MechanicalBeaconBlock.ACTIVE, false).setValue(HALF, DoubleBlockHalf.UPPER));
                level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(MechanicalBeaconBlock.ACTIVE, false));
            }
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
        if (getBlockState().getValue(HALF) == DoubleBlockHalf.UPPER) return;
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
        if (icon != null)
            compound.putString("Icon", icon);
        if (positionMode != null)
            NBTHelper.writeEnum(compound, "PositionMode", positionMode);
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
        if (compound.contains("Icon"))
            icon = compound.getString("Icon");
        if (compound.contains("PositionMode"))
            positionMode = NBTHelper.readEnum(compound, "PositionMode", PositionControl.class);
    }

    public String getBeaconId() {
        return hash = Utils.getOrCreateHashString(hash, level, worldPosition);
    }

    public void setBeaconId(String id) {
        this.hash = id;
    }

    public String getBeaconName() {
        return name = Utils.getOrCreateBeaconName(name);
    }

    public void setBeaconName(String beaconName) {
        this.name = beaconName;
        this.setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            DataManager.updateBeaconName((ServerLevel) level, hash, beaconName);
        }
    }

    public void initBlockEntity(Player player) {
        if (getBlockState().getValue(HALF) == DoubleBlockHalf.UPPER) return;
        this.hash = Utils.getOrCreateHashString(hash, level, worldPosition);
        this.name = Utils.getOrCreateBeaconName(name);
        this.owner = player.getUUID();
        this.ownerName = player.getName().getString();
        this.positionMode = PositionControl.NORTH;
        ResourceKey<Level> dimension = level.dimension();
        if (dimension.equals(Level.OVERWORLD)) {
            this.icon = "minecraft:grass_block";
        } else if (dimension.equals(Level.END)) {
            this.icon = "minecraft:end_stone";
        } else if (dimension.equals(Level.NETHER)) {
            this.icon = "minecraft:netherrack";
        } else {
            this.icon = "minecraft:stone";
        }
    }

    public boolean isBrass() {
        Block block = level.getBlockState(getBlockPos()).getBlock();
        ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(block);
        return blockId.getPath().equals("brass_beacon");
    }

    public UUID getOwner() {
        return owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
        this.setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            DataManager.updateBeaconIcon((ServerLevel) level, hash, icon);
        }
    }

    public void setIcon(ItemStack itemStack) {
        String icon;
        if (itemStack.isEmpty()) {
            icon = "minecraft:stone";
        } else {
            Item item = itemStack.getItem();
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
            icon = itemId.getNamespace() + ":" + itemId.getPath();
        }
        this.icon = icon;
        this.setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            DataManager.updateBeaconIcon((ServerLevel) level, hash, icon);
        }
    }

    public PositionControl getPositionMode() {
        return positionMode;
    }

    public void setPositionMode(PositionControl mode) {
        positionMode = mode;
        this.setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            DataManager.updateBeaconPositionMode((ServerLevel) level, hash, positionMode);
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (getBlockState().getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockEntity lowerEntity = level.getBlockEntity(worldPosition.below());
            if (lowerEntity instanceof MechanicalBeaconBlockEntity) {
                return ((MechanicalBeaconBlockEntity) lowerEntity).addToGoggleTooltip(tooltip, isPlayerSneaking);
            }
        }
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        CirCubeLang.translate("gui.goggles.beaconStats")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        if (name != null && !name.isEmpty()) {
            CirCubeLang.translate("tooltip.beacon_name")
                    .style(ChatFormatting.DARK_GRAY)
                    .space()
                    .add(CirCubeLang.text(name)
                            .style(ChatFormatting.AQUA))
                    .forGoggles(tooltip, 1);
        }
        if (ownerName != null) {
            CirCubeLang.translate("tooltip.beacon_owner")
                    .style(ChatFormatting.DARK_GRAY)
                    .space()
                    .add(CirCubeLang.text(ownerName)
                            .style(ChatFormatting.AQUA))
                    .forGoggles(tooltip, 1);
        }

        return added;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal(name);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return MechanicalBeaconMenu.create(id, inventory, this);
    }

    public ItemStack getIconItemStack() {
        if (icon == null || icon.isEmpty()) {
            ResourceLocation resourceLocation = ResourceLocation.tryParse("minecraft:barrier");
            return new ItemStack(ForgeRegistries.ITEMS.getValue(resourceLocation));
        } else {
            ResourceLocation resourceLocation = ResourceLocation.tryParse(icon);
            return new ItemStack(ForgeRegistries.ITEMS.getValue(resourceLocation));
        }
    }
}
