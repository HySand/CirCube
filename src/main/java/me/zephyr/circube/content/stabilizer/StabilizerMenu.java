package me.zephyr.circube.content.stabilizer;

import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;


public class StabilizerMenu extends GhostItemMenu<ItemStack> {
    public boolean slotsActive = true;
    public int targetSlotsActive = 1;
    static final int slots = 2;

    public StabilizerMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public StabilizerMenu(MenuType<?> type, int id, Inventory inv, ItemStack contentHolder) {
        super(type, id, inv, contentHolder);
    }

    @Override
    protected ItemStack createOnClient(FriendlyByteBuf extraData) {
        return extraData.readItem();
    }

    @Override
    protected void addSlots() {
        addPlayerSlots(46, 140);
        for (int i = 0; i < slots; i++)
            addSlot(new InactiveItemHandlerSlot(ghostInventory, i, i, 54 + 20 * i, 88));
    }

    @Override
    protected void saveData(ItemStack contentHolder) {
    }

    @Override
    protected ItemStackHandler createGhostInventory() {
        return new ItemStackHandler(slots);
    }

    @Override
    protected boolean allowRepeats() {
        return true;
    }

    class InactiveItemHandlerSlot extends SlotItemHandler {

        private final int targetIndex;

        public InactiveItemHandlerSlot(IItemHandler itemHandler, int targetIndex, int index, int xPosition,
                                       int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
            this.targetIndex = targetIndex;
        }

        @Override
        public boolean isActive() {
            return slotsActive && targetIndex < targetSlotsActive;
        }

    }
}
