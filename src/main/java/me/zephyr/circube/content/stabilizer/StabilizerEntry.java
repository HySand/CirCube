package me.zephyr.circube.content.stabilizer;

import me.zephyr.circube.content.beacon.PositionControl;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class StabilizerEntry {
    private final String beaconId;
    private String name;
    private BlockPos location;
    private String owner;
    private ItemStack icon;
    private final String iconName;
    private final boolean active;
    private PositionControl positionControl;

    public StabilizerEntry(String id, String name, BlockPos location, String icon, String owner, boolean active, PositionControl positionControl) {
        this.beaconId = id;
        this.name = name;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public BlockPos getLocation() {
        return location;
    }

    public void setLocation(BlockPos location) {
        this.location = location;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public boolean isActive() {
        return active;
    }

    public void setPositionControl(PositionControl positionControl) {
        this.positionControl = positionControl;
    }

    public PositionControl getPositionControl() {
        return positionControl;
    }
}
