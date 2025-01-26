package me.zephyr.circube.content.stabilizer;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;

public class StabilizerEntry {
    private String name;
    private BlockPos location;
    private String owner;
    private ItemStack icon;

    public StabilizerEntry(String name, BlockPos location, String icon, String owner) {
        this.name = name;
        this.location = location;
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(icon));
        this.icon = new ItemStack(Objects.requireNonNullElse(item, Items.GRASS_BLOCK));
        this.owner = owner;
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

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public BlockPos getLocation() {
        return location;
    }

    public void setLocation(BlockPos location) {
        this.location = location;
    }
}
