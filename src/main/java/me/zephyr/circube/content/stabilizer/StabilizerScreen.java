package me.zephyr.circube.content.stabilizer;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import me.zephyr.circube.CirCubeGuiTextures;
import me.zephyr.circube.CirCubeLang;
import me.zephyr.circube.CirCubePackets;
import me.zephyr.circube.content.stabilizer.packets.BeaconDeletePacket;
import me.zephyr.circube.content.stabilizer.packets.BeaconOrderUpdatePacket;
import me.zephyr.circube.content.stabilizer.packets.EntriesRequestPacket;
import me.zephyr.circube.content.stabilizer.packets.TeleportPacket;
import me.zephyr.circube.util.DataManager;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.gui.UIRenderHelper;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StabilizerScreen extends AbstractSimiContainerScreen<StabilizerMenu> {
    private static final int CARD_HEADER = 18;
    private static final int CARD_WIDTH = 124;

    private List<Rect2i> extraAreas = Collections.emptyList();

    private final LerpedFloat scroll = LerpedFloat.linear().startWithValue(0);
    private final List<StabilizerEntry> teleportEntries = new ArrayList<>();
    private IconButton cancelButton;

    private boolean modified = false;

    private boolean hasRequestedData = false;

    public StabilizerScreen(StabilizerMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        menu.slotsActive = false;
    }

    private void loadTeleportEntries() {
        if (minecraft != null && minecraft.player != null) {
            CirCubePackets.CHANNEL.sendToServer(new EntriesRequestPacket());
        }
    }

    public void updateTeleportEntries(List<StabilizerEntry> entries) {
        this.teleportEntries.clear();
        this.teleportEntries.addAll(entries);
        this.init();
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

        if (!hasRequestedData) {
            loadTeleportEntries();
            hasRequestedData = true;
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        scroll.tickChaser();
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

    protected void renderStabilizer(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        UIRenderHelper.swapAndBlitColor(minecraft.getMainRenderTarget(), UIRenderHelper.framebuffer);

        int yOffset = 25;
        float scrollOffset = -scroll.getValue(partialTicks);

        for (int i = 0; i <= teleportEntries.size(); i++) {
            startStencil(graphics, leftPos + 3, topPos + 14, 178, 120);
            matrixStack.pushPose();
            matrixStack.translate(0, scrollOffset, 0);

            if (i == teleportEntries.size()) {
                if (i > 0)
                    yOffset += 9;
                matrixStack.popPose();
                endStencil();
                break;
            }

            StabilizerEntry entry = teleportEntries.get(i);
            int cardY = yOffset;
            int cardHeight = renderStabilizerEntry(graphics, entry, cardY, mouseX, mouseY, partialTicks);
            yOffset += cardHeight;

            if (i + 1 < teleportEntries.size()) {
                yOffset += 10;
            }

            matrixStack.popPose();
            endStencil();

            startStencil(graphics, leftPos + 3, topPos + 14, 178, 120);
            matrixStack.pushPose();
            matrixStack.translate(0, scrollOffset, 0);
            renderStabilizerInformations(graphics, entry, cardY, mouseX, mouseY, partialTicks, cardHeight, i);
            matrixStack.popPose();
            endStencil();
        }

        int zLevel = 200;
        graphics.fillGradient(leftPos + 3, topPos + 14, leftPos + 3 + 178, topPos + 14 + 10, zLevel, 0x77000000,
                0x00000000);
        graphics.fillGradient(leftPos + 3, topPos + 124, leftPos + 3 + 178, topPos + 124 + 10, zLevel, 0x00000000,
                0x77000000);
        UIRenderHelper.swapAndBlitColor(UIRenderHelper.framebuffer, minecraft.getMainRenderTarget());
    }

    public int renderStabilizerEntry(GuiGraphics graphics, StabilizerEntry entry, int yOffset, int mouseX, int mouseY,
                                     float partialTicks) {
        int zLevel = -100;

        CirCubeGuiTextures light = CirCubeGuiTextures.STABILIZER_CARD_LIGHT;
        CirCubeGuiTextures medium = CirCubeGuiTextures.STABILIZER_CARD_MEDIUM;
        CirCubeGuiTextures dark = CirCubeGuiTextures.STABILIZER_CARD_DARK;

        int cardWidth = CARD_WIDTH;
        int cardHeader = CARD_HEADER;
        int maxRows = 0;
        int cardHeight = cardHeader + 14;

        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(leftPos + 25, topPos + yOffset, 0);

        me.zephyr.circube.util.UIRenderHelper.drawStretched(graphics, 0, 1, cardWidth, cardHeight - 2, zLevel, light);
        me.zephyr.circube.util.UIRenderHelper.drawStretched(graphics, 1, 0, cardWidth - 2, cardHeight, zLevel, light);
        me.zephyr.circube.util.UIRenderHelper.drawStretched(graphics, 1, 1, cardWidth - 2, cardHeight - 2, zLevel, dark);
        me.zephyr.circube.util.UIRenderHelper.drawStretched(graphics, 2, 2, cardWidth - 4, cardHeight - 4, zLevel, medium);
        me.zephyr.circube.util.UIRenderHelper.drawStretched(graphics, 2, 2, cardWidth - 4, cardHeader, zLevel, light);

        CirCubeGuiTextures.STABILIZER_CARD_FORGET.render(graphics, cardWidth - 14, 2);

        int i = teleportEntries.indexOf(entry);
        if (i > 0)
            CirCubeGuiTextures.STABILIZER_CARD_MOVE_UP.render(graphics, cardWidth, cardHeader - 14);
        if (i < teleportEntries.size() - 1)
            CirCubeGuiTextures.STABILIZER_CARD_MOVE_DOWN.render(graphics, cardWidth, cardHeader);

        matrixStack.popPose();

        return cardHeight;
    }

    public void renderStabilizerInformations(GuiGraphics graphics, StabilizerEntry entry, int yOffset, int mouseX,
                                             int mouseY, float partialTicks, int cardHeight, int entryIndex) {
        int cardWidth = CARD_WIDTH;
        int cardHeader = CARD_HEADER;

        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(leftPos + 25, topPos + yOffset, 0);

        ItemStack itemStack = entry.getIcon();
        graphics.renderItem(itemStack, 10, 3);
        matrixStack.translate(0, 0, 0);
        FormattedText displayText = FormattedText.of(entry.getName());
        graphics.drawString(font, font.substrByWidth(displayText, 120)
                .getString(), 30, 7, 0xff_f2f2ee);
        Component ownerText = CirCubeLang.translateDirect("gui.stabilizer.owner", entry.getOwner());
        graphics.drawString(font, ownerText, 12, 21, 0xff_e1e1d8);
        matrixStack.popPose();
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        float chaseTarget = scroll.getChaseTarget();
        float max = 40 - 120;
        for (StabilizerEntry scheduleEntry : teleportEntries) {
            max += CARD_HEADER + 5 + 14;
        }
        if (max > 0) {
            chaseTarget -= pDelta * 12;
            chaseTarget = Mth.clamp(chaseTarget, 0, max);
            scroll.chase((int) chaseTarget, 0.7f, LerpedFloat.Chaser.EXP);
        } else
            scroll.chase(0, 0.7f, LerpedFloat.Chaser.EXP);

        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    protected void renderForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        super.renderForeground(graphics, mouseX, mouseY, partialTicks);
        CirCubeGuiTextures bg = CirCubeGuiTextures.STABILIZER;
        GuiGameElement.of(menu.contentHolder).<GuiGameElement
                        .GuiRenderBuilder>at(leftPos + bg.width, topPos + bg.height - 56, -200)
                .scale(3)
                .render(graphics);
        action(graphics, mouseX, mouseY, -1);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        CirCubeGuiTextures.STABILIZER.render(graphics, leftPos, topPos);
        FormattedCharSequence formattedcharsequence = title.getVisualOrderText();
        int center = leftPos + (CirCubeGuiTextures.STABILIZER.width - 8) / 2;
        graphics.drawString(font, formattedcharsequence, (float) (center - font.width(formattedcharsequence) / 2),
                (float) topPos + 3, 0xbea1f0, false);
        renderStabilizer(graphics, mouseX, mouseY, partialTicks);
    }

    public boolean action(@Nullable GuiGraphics graphics, double mouseX, double mouseY, int click) {
        Component empty = Component.empty();

        int mx = (int) mouseX;
        int my = (int) mouseY;
        int x = mx - leftPos - 25;
        int y = my - topPos - 25;
        if (x < 0 || x >= 178)
            return false;
        if (y < 0 || y >= 98)
            return false;
        y += scroll.getValue(0);

        for (int i = 0; i < teleportEntries.size(); i++) {
            StabilizerEntry entry = teleportEntries.get(i);
            int cardHeight = CARD_HEADER + 14;

            if (y >= cardHeight + 5) {
                y -= cardHeight + 10;
                if (y < 0)
                    return false;
                continue;
            }

            int fieldSize = 110;
            if (x > 0 && x <= fieldSize && y > 0 && y <= cardHeight - 12 || x > 0 && x <= fieldSize + 12 && y > 14 && y <= cardHeight - 12) {
                if (entry.isActive()) {
                    renderActionTooltip(graphics, ImmutableList.of(CirCubeLang.translateDirect("gui.stabilizer.teleport")),
                            mx, my);
                    if (click == 0) {
                        minecraft.player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                        BlockPos targetPos = entry.getLocation();
                        CirCubePackets.CHANNEL.sendToServer(new TeleportPacket(targetPos, entry.getPositionControl()));
                        minecraft.player.closeContainer();
                    }
                    return true;
                } else {
                    renderActionTooltip(graphics, ImmutableList.of(CirCubeLang.translateDirect("gui.stabilizer.offline")),
                            mx, my);
                }
            }

            if (x > fieldSize && x <= fieldSize + 12) {
                if (y > 0 && y <= 14) {
                    renderActionTooltip(graphics, ImmutableList.of(CirCubeLang.translateDirect("gui.stabilizer.forget_entry")),
                            mx, my);
                    if (click == 0) {
                        String beaconId = entry.getBeaconId();
                        CirCubePackets.CHANNEL.sendToServer(new BeaconDeletePacket(beaconId));
                        DataManager.clientBeaconList.remove(beaconId);
                        teleportEntries.remove(entry);
                        init();
                    }
                    return true;
                }
            }

            if (x > fieldSize + 14 && x < fieldSize + 26) {
                if (y > 7 && y <= 16 && i > 0) {
                    renderActionTooltip(graphics, ImmutableList.of(CirCubeLang.translateDirect("gui.stabilizer.move_up")), mx,
                            my);
                    if (click == 0) {
                        teleportEntries.remove(entry);
                        teleportEntries.add(i - 1, entry);
                        modified = true;
                        init();
                    }
                    return true;
                }
                if (y > 17 && y <= 26 && i < teleportEntries.size() - 1) {
                    renderActionTooltip(graphics, ImmutableList.of(CirCubeLang.translateDirect("gui.stabilizer.move_down")), mx,
                            my);
                    if (click == 0) {
                        teleportEntries.remove(entry);
                        teleportEntries.add(i + 1, entry);
                        modified = true;
                        init();
                    }
                    return true;
                }
            }

            return x >= 0 && x <= 15 && y <= 20;
        }
        return true;
    }

    private void sendUpdatedOrderToServer() {
        List<String> beaconIds = teleportEntries.stream()
                .map(StabilizerEntry::getBeaconId)
                .collect(Collectors.toList());
        DataManager.clientBeaconList.clear();
        DataManager.clientBeaconList.addAll(beaconIds);
        CirCubePackets.CHANNEL.sendToServer(new BeaconOrderUpdatePacket(beaconIds));
    }

    protected void startStencil(GuiGraphics graphics, float x, float y, float w, float h) {
        RenderSystem.clear(GL30.GL_STENCIL_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);

        GL11.glDisable(GL11.GL_STENCIL_TEST);
        RenderSystem.stencilMask(~0);
        RenderSystem.clear(GL11.GL_STENCIL_BUFFER_BIT, Minecraft.ON_OSX);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        RenderSystem.stencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);
        RenderSystem.stencilMask(0xFF);
        RenderSystem.stencilFunc(GL11.GL_NEVER, 1, 0xFF);

        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(x, y, 0);
        matrixStack.scale(w, h, 1);
        graphics.fillGradient(0, 0, 1, 1, -100, 0xff000000, 0xff000000);
        matrixStack.popPose();

        GL11.glEnable(GL11.GL_STENCIL_TEST);
        RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
        RenderSystem.stencilFunc(GL11.GL_EQUAL, 1, 0xFF);
    }

    protected void endStencil() {
        GL11.glDisable(GL11.GL_STENCIL_TEST);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (action(null, pMouseX, pMouseY, pButton))
            return true;

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    private int getFieldSize(int minSize, Pair<ItemStack, Component> pair) {
        ItemStack stack = pair.getFirst();
        Component text = pair.getSecond();
        boolean hasItem = !stack.isEmpty();
        return Math.max((text == null ? 0 : font.width(text)) + (hasItem ? 20 : 0) + 16, minSize);
    }

    private void renderActionTooltip(@Nullable GuiGraphics graphics, List<Component> tooltip, int mx, int my) {
        if (graphics != null)
            graphics.renderTooltip(font, tooltip, Optional.empty(), mx, my);
    }

    @Override
    public void removed() {
        super.removed();
        if (modified)
            sendUpdatedOrderToServer();
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return extraAreas;
    }
}
