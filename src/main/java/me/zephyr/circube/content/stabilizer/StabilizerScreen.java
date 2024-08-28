package me.zephyr.circube.content.stabilizer;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import me.zephyr.circube.CirCubeGuiTextures;
import me.zephyr.circube.CirCubeItems;
import me.zephyr.circube.content.beacon.MechanicalBeacon;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StabilizerScreen extends AbstractSimiContainerScreen<StabilizerMenu> {
    private static final int CARD_HEADER = 18;
    private static final int CARD_WIDTH = 124;

    private final ItemStack renderedItem = CirCubeItems.STABILIZER.asStack();

    private List<Rect2i> extraAreas = Collections.emptyList();

    private List<LerpedFloat> horizontalScrolls = new ArrayList<>();
    private LerpedFloat scroll = LerpedFloat.linear()
            .startWithValue(0);

    private Stabilizer stabilizer;

    private IconButton cancelButton, editorDelete;

    public StabilizerScreen(StabilizerMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        CompoundTag tag = container.contentHolder.getOrCreateTag().getCompound("Stabilizer");
        menu.slotsActive = false;
    }

    @Override
    protected void init() {
        CirCubeGuiTextures bg = CirCubeGuiTextures.STABILIZER;
        setWindowSize(bg.width, bg.height);
        super.init();
        clearWidgets();

        cancelButton = new IconButton(leftPos + bg.width - 33, topPos + bg.height - 24, AllIcons.I_MTD_CLOSE);
        cancelButton.withCallback(() -> minecraft.player.closeContainer());
        addRenderableWidget(cancelButton);

        extraAreas = ImmutableList.of(new Rect2i(leftPos + bg.width, topPos + bg.height - 56, 48, 48));
        horizontalScrolls.clear();

        for (int i = 0; i < stabilizer.entries.size(); i++)
            horizontalScrolls.add(LerpedFloat.linear()
                    .startWithValue(0));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        partialTicks = minecraft.getFrameTime();

        if (menu.slotsActive)
            super.render(graphics, mouseX, mouseY, partialTicks);
        else {
            renderBackground(graphics);
            renderBg(graphics, partialTicks, mouseX, mouseY);
            for (Renderable widget : this.renderables)
                widget.render(graphics, mouseX, mouseY, partialTicks);
            renderForeground(graphics, mouseX, mouseY, partialTicks);
        }
    }


    @Override
    protected void containerTick() {
        super.containerTick();
        scroll.tickChaser();
        for (LerpedFloat lerpedFloat : horizontalScrolls)
            lerpedFloat.tickChaser();
    }

    @Override
    protected void renderForeground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = guiGraphics.pose();
        super.renderForeground(guiGraphics, mouseX, mouseY, partialTicks);
        CirCubeGuiTextures bg = CirCubeGuiTextures.STABILIZER;
        GuiGameElement.of(menu.contentHolder).<GuiGameElement
                        .GuiRenderBuilder>at(leftPos + bg.width, topPos + bg.height - 56, -200)
                .scale(3)
                .render(guiGraphics);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        CirCubeGuiTextures.STABILIZER.render(guiGraphics, leftPos, topPos);

        FormattedCharSequence formattedcharsequence = title.getVisualOrderText();
        int center = leftPos + (CirCubeGuiTextures.STABILIZER.width - 8) / 2;
        guiGraphics.drawString(font, formattedcharsequence, (float) (center - font.width(formattedcharsequence) / 2),
                (float) topPos + 3, 0xbea1f0, false);
    }

    public int renderScheduleEntry(GuiGraphics graphics, StabilizerEntry entry, int yOffset, int mouseX, int mouseY, float partialTicks) {
        return 0;
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return extraAreas;
    }
}
