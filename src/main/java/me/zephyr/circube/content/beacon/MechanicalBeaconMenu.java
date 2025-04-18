package me.zephyr.circube.content.beacon;

import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import me.zephyr.circube.CirCubeMenuTypes;
import me.zephyr.circube.CirCubePackets;
import me.zephyr.circube.content.beacon.packets.BeaconIconUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;


public class MechanicalBeaconMenu extends GhostItemMenu<MechanicalBeaconBlockEntity> {
    private String name;
    private BlockPos pos;
    private boolean brass;
    private ItemStack icon;
    private PositionControl positionControl;

    public MechanicalBeaconMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public MechanicalBeaconMenu(MenuType<?> type, int id, Inventory inv, MechanicalBeaconBlockEntity be) {
        super(type, id, inv, be);
        this.name = be.getBeaconName();
        this.pos = be.getBlockPos();
        this.brass = be.isBrass();
        this.positionControl = be.getPositionMode();
    }

    @Override
    protected ItemStackHandler createGhostInventory() {
        ItemStackHandler newInv = new ItemStackHandler(1);
        ItemStack stack = contentHolder.getIconItemStack();
        if (!stack.isEmpty()) {
            newInv.setStackInSlot(0, stack);
        }
        return newInv;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId == playerInventory.selected && clickTypeIn != ClickType.THROW)
            return;
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Override
    protected void initAndReadInventory(MechanicalBeaconBlockEntity be) {
        super.initAndReadInventory(be);
    }

    @Override
    protected boolean allowRepeats() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected MechanicalBeaconBlockEntity createOnClient(FriendlyByteBuf friendlyByteBuf) {
        ClientLevel world = Minecraft.getInstance().level;
        this.name = friendlyByteBuf.readUtf();
        this.pos = friendlyByteBuf.readBlockPos();
        this.brass = friendlyByteBuf.readBoolean();
        this.icon = ItemStack.of(friendlyByteBuf.readNbt());
        this.positionControl = friendlyByteBuf.readEnum(PositionControl.class);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof MechanicalBeaconBlockEntity mechanicalBeaconBlockEntity) {
            return mechanicalBeaconBlockEntity;
        }
        return null;
    }

    @Override
    protected void saveData(MechanicalBeaconBlockEntity mechanicalBeaconBlockEntity) {
        CirCubePackets.CHANNEL.sendToServer(new BeaconIconUpdatePacket(pos, ghostInventory.getStackInSlot(0)));
    }

    public static MechanicalBeaconMenu create(int id, Inventory inv, MechanicalBeaconBlockEntity be) {
        return new MechanicalBeaconMenu(CirCubeMenuTypes.MECHANICAL_BEACON_MENU.get(), id, inv, be);
    }

    @Override
    protected void addSlots() {
        addPlayerSlots(8, 124);
        addSlot(new SlotItemHandler(ghostInventory, 0, 55, 38));
    }

    public String getName() {
        return name;
    }

    public BlockPos getPos() {
        return pos;
    }

    public boolean isBrass() {
        return brass;
    }

    public PositionControl getPositionControl() {
        return positionControl;
    }
}
