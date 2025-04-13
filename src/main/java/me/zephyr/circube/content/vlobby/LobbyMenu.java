package me.zephyr.circube.content.vlobby;

import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class LobbyMenu extends GhostItemMenu<ItemStack> {
    public boolean slotsActive = true;
    static final int slots = 2;

    public LobbyMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    @Override
    protected ItemStackHandler createGhostInventory() {
        return new ItemStackHandler(slots);
    }

    @Override
    protected boolean allowRepeats() {
        return true;
    }

    @Override
    protected ItemStack createOnClient(FriendlyByteBuf friendlyByteBuf) {
        return new ItemStack(Item.byId(1));
    }

    @Override
    protected void addSlots() {
    }

    @Override
    protected void saveData(ItemStack itemStack) {

    }
}
