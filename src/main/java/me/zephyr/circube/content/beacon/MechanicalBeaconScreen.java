package me.zephyr.circube.content.beacon;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.trains.station.NoShadowFontWrapper;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import me.zephyr.circube.CirCubeBlocks;
import me.zephyr.circube.CirCubeGuiTextures;
import me.zephyr.circube.CirCubePackets;
import me.zephyr.circube.content.beacon.packets.BeaconNameUpdatePacket;
import me.zephyr.circube.content.beacon.packets.BeaconPositionUpdatePacket;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.simibubi.create.foundation.gui.AllGuiTextures.PLAYER_INVENTORY;

public class MechanicalBeaconScreen extends AbstractSimiContainerScreen<MechanicalBeaconMenu> {
    private EditBox nameBox;
    private IconButton confirmButton;
    protected CirCubeGuiTextures background;
    private final String name;
    private final BlockPos pos;
    private final boolean brass;
    private PositionControl positionControl;

    private final ItemStack renderedItem;
    private List<Rect2i> extraAreas = Collections.emptyList();

    public MechanicalBeaconScreen(MechanicalBeaconMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        this.name = container.getName();
        this.pos = container.getPos();
        this.brass = container.isBrass();
        if (brass) {
            background = CirCubeGuiTextures.BRASS_BEACON;
            renderedItem = CirCubeBlocks.BRASS_BEACON.asStack();
        } else {
            background = CirCubeGuiTextures.ANDESITE_BEACON;
            renderedItem = CirCubeBlocks.ANDESITE_BEACON.asStack();
        }
        this.positionControl = container.getPositionControl();
    }

    @Override
    public void containerTick() {
        if (getFocused() != nameBox) {
            nameBox.setCursorPosition(nameBox.getValue()
                    .length());
            nameBox.setHighlightPos(nameBox.getCursorPosition());
        }
        super.containerTick();
    }

    @Override
    protected void init() {
        setWindowSize(PLAYER_INVENTORY.getWidth(), background.height + 4 + PLAYER_INVENTORY.getHeight());
        super.init();
        int x = leftPos;
        int y = topPos;
        clearWidgets();

        confirmButton = new IconButton(x + background.width - 33, y + background.height - 24, AllIcons.I_CONFIRM);
        confirmButton.withCallback(() -> minecraft.player.closeContainer());
        addRenderableWidget(confirmButton);

        Consumer<String> onTextChanged;

        onTextChanged = s -> nameBox.setX(nameBoxX(s, nameBox));
        nameBox = new EditBox(new NoShadowFontWrapper(font), x + 5, y + 4, background.width - 20, 10,
                Component.literal(name));
        nameBox.setBordered(false);
        nameBox.setMaxLength(24);
        if (brass) {
            nameBox.setTextColor(0x592424);
        } else {
            nameBox.setTextColor(0x303030);
        }
        nameBox.setValue(name);
        nameBox.setFocused(false);
        nameBox.mouseClicked(0, 0, 0);
        nameBox.setResponder(onTextChanged);
        nameBox.setX(nameBoxX(nameBox.getValue(), nameBox));
        addRenderableWidget(nameBox);

        Pair<ScrollInput, Label> positionControlWidgets =
                PositionControl.createWidget(x + 31, y + 77, mode -> positionControl = mode, positionControl);
        addRenderableWidget(positionControlWidgets.getFirst());
        addRenderableWidget(positionControlWidgets.getSecond());

        extraAreas = ImmutableList.of(new Rect2i(x + background.width, y + background.height - 45, 40, 48));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int invX = getLeftOfCentered(PLAYER_INVENTORY.getWidth());
        int invY = topPos + background.height + 4;
        renderPlayerInventory(graphics, invX, invY);

        int x = leftPos;
        int y = topPos;
        background.render(graphics, x, y);

        GuiGameElement.of(renderedItem)
                .<GuiGameElement.GuiRenderBuilder>at(x + background.width, y + background.height - 45, -200)
                .scale(3)
                .render(graphics);

        String text = nameBox.getValue();
        if (!nameBox.isFocused()) {
            if (brass) {
                CirCubeGuiTextures.BRASS_EDIT_NAME.render(graphics, nameBoxX(text, nameBox) + font.width(text) + 5, y + 1);
            } else {
                CirCubeGuiTextures.ANDESITE_EDIT_NAME.render(graphics, nameBoxX(text, nameBox) + font.width(text) + 5, y + 1);
            }
        }

        if (brass) {
            graphics.renderItem(AllBlocks.BRASS_DOOR.asStack(), x + 10, y + 78);
        } else {
            graphics.renderItem(AllBlocks.ANDESITE_DOOR.asStack(), x + 10, y + 78);
        }

    }

    private int nameBoxX(String s, EditBox nameBox) {
        return leftPos + background.width / 2 - (Math.min(font.width(s), nameBox.getWidth()) + 10) / 2;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (!nameBox.isFocused() && pMouseY > topPos && pMouseY < topPos + 14 && pMouseX > leftPos
                && pMouseX < leftPos + background.width) {
            nameBox.setFocused(true);
            nameBox.setHighlightPos(0);
            setFocused(nameBox);
            return true;
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        boolean hitEnter = getFocused() instanceof EditBox
                && (pKeyCode == InputConstants.KEY_RETURN || pKeyCode == InputConstants.KEY_NUMPADENTER);

        if (hitEnter && nameBox.isFocused()) {
            if (!nameBox.getValue().equals(name))
                CirCubePackets.CHANNEL.sendToServer(new BeaconNameUpdatePacket(pos, nameBox.getValue()));
            return true;
        }

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return extraAreas;
    }

    @Override
    public void removed() {
        super.removed();
        CirCubePackets.CHANNEL.sendToServer(new BeaconPositionUpdatePacket(pos, positionControl));
        if (nameBox == null || nameBox.getValue().equals(name))
            return;
        CirCubePackets.CHANNEL.sendToServer(new BeaconNameUpdatePacket(pos, nameBox.getValue()));
    }
}
