package me.zephyr.circube.content.beacon;

import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.content.trains.station.NoShadowFontWrapper;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.utility.Components;
import me.zephyr.circube.CirCubeGuiTextures;
import me.zephyr.circube.CirCubePackets;
import me.zephyr.circube.content.beacon.packets.BeaconNameUpdatePacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class MechanicalBeaconScreen extends AbstractSimiScreen {
    private EditBox nameBox;
    private IconButton confirmButton;
    private MechanicalBeaconBlockEntity entity;
    protected CirCubeGuiTextures background;
    private boolean brass;
    public MechanicalBeaconScreen(MechanicalBeaconBlockEntity be, boolean brass) {
        super(Component.literal(be.getBeaconName()));
        this.entity = be;
        this.brass = brass;
        if (brass) {
            background = CirCubeGuiTextures.BRASS_BEACON;
        } else {
            background = CirCubeGuiTextures.ANDESITE_BEACON;
        }
    }

    @Override
    public void tick() {
        if (getFocused() != nameBox) {
            nameBox.setCursorPosition(nameBox.getValue()
                    .length());
            nameBox.setHighlightPos(nameBox.getCursorPosition());
        }
        super.tick();
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = guiLeft;
        int y = guiTop;
        background.render(graphics, x, y);

        String text = nameBox.getValue();
        if (!nameBox.isFocused()) {
            if (brass) {
                CirCubeGuiTextures.BRASS_EDIT_NAME.render(graphics, nameBoxX(text, nameBox) + font.width(text) + 5, y + 1);
            } else {
                CirCubeGuiTextures.ANDESITE_EDIT_NAME.render(graphics, nameBoxX(text, nameBox) + font.width(text) + 5, y + 1);
            }
        }

    }

    @Override
    protected void init() {
        setWindowSize(background.width, background.height);
        super.init();
        int x = guiLeft;
        int y = guiTop;
        clearWidgets();

        confirmButton = new IconButton( x + background.width - 33, y + background.height - 24, AllIcons.I_CONFIRM);
        confirmButton.withCallback(() -> minecraft.player.closeContainer());
        addRenderableWidget(confirmButton);

        Consumer<String> onTextChanged;

        onTextChanged = s -> nameBox.setX(nameBoxX(s, nameBox));
        nameBox = new EditBox(new NoShadowFontWrapper(font), x + 5, y + 4, background.width - 20, 10,
                Components.literal(entity.getBeaconName()));
        nameBox.setBordered(false);
        nameBox.setMaxLength(24);
        if (brass) {
            nameBox.setTextColor(0x592424);
        } else {
            nameBox.setTextColor(0x303030);
        }
        nameBox.setValue(entity.getBeaconName());
        nameBox.setFocused(false);
        nameBox.mouseClicked(0, 0, 0);
        nameBox.setResponder(onTextChanged);
        nameBox.setX(nameBoxX(nameBox.getValue(), nameBox));
        addRenderableWidget(nameBox);
    }

    private int nameBoxX(String s, EditBox nameBox) {
        return guiLeft + background.width / 2 - (Math.min(font.width(s), nameBox.getWidth()) + 10) / 2;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (!nameBox.isFocused() && pMouseY > guiTop && pMouseY < guiTop + 14 && pMouseX > guiLeft
                && pMouseX < guiLeft + background.width) {
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
            if (!nameBox.getValue().equals(entity.getBeaconName()))
                CirCubePackets.CHANNEL.sendToServer(new BeaconNameUpdatePacket(entity.getBlockPos(), nameBox.getValue()));
            return true;
        }

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}
