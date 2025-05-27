package me.zephyr.circube.content.teleport.item;

import me.zephyr.circube.content.teleport.beacon.PositionControl;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class StabilizerEntry {
    private final String beaconId;
    private final String name;
    private final BlockPos location;
    private final String owner;
    private final ItemStack icon;
    private final String iconName;
    private final boolean active;
    private final String levelName;
    private final PositionControl positionControl;

    public StabilizerEntry(String id, String name, String level, BlockPos location, String icon, String owner, boolean active, PositionControl positionControl) {
        this.beaconId = id;
        this.name = name;
        this.levelName = level;
        this.location = location;
        this.iconName = icon;
        Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(icon));
        this.icon = new ItemStack(item, 1);
        this.owner = owner;
        this.active = active;
        this.positionControl = positionControl;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public String getIconName() {
        return iconName;
    }

    public BlockPos getLocation() {
        return location;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public boolean isActive() {
        return active;
    }

    public PositionControl getPositionControl() {
        return positionControl;
    }

    public String getLevelName() {
        return levelName;
    }
}
