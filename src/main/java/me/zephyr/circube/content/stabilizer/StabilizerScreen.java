package me.zephyr.circube.content.stabilizer;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import me.zephyr.circube.CirCubeGuiTextures;
import me.zephyr.circube.CirCubeItems;
import me.zephyr.circube.Lang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StabilizerScreen extends AbstractSimiContainerScreen<StabilizerMenu> {
    private static final int CARD_HEADER = 18;
    private static final int CARD_WIDTH = 124;
    private static final int CARD_HEIGHT = 40;  // 每个条目的高度
    private static final int SCREEN_HEIGHT = 165; // 界面总高度
    private static final int MAIN_AREA_HEIGHT = 120; // 主区域的高度

    private final ItemStack renderedItem = CirCubeItems.STABILIZER.asStack();

    private List<Rect2i> extraAreas = Collections.emptyList();

    private final LerpedFloat scroll = LerpedFloat.linear().startWithValue(0);
    private final List<StabilizerEntry> teleportEntries; // 存储传送点条目的列表
    private IconButton cancelButton, likeButton;  // “喜爱”按钮
    private IconButton deleteButton;  // 删除按钮

    public StabilizerScreen(StabilizerMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        CompoundTag tag = container.contentHolder.getOrCreateTag().getCompound("Stabilizer");
        menu.slotsActive = false;
        teleportEntries = loadTeleportEntries();  // 加载传送点条目
    }

    // 加载传送点条目，这里可以根据需要从NBT、玩家数据等源获取数据
    private List<StabilizerEntry> loadTeleportEntries() {
        // 创建5个测试条目，位置可以是随机位置或固定位置
        List<StabilizerEntry> entries = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BlockPos location = new BlockPos(100 + i, 64, 100 + i);  // 这里给出一个简单的示例坐标
            entries.add(new StabilizerEntry("传送点 " + (i + 1), location));
        }
        return entries;
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
        }

        int zLevel = 200;
        graphics.fillGradient(leftPos + 3, topPos + 14, leftPos + 3 + 178, topPos + 14 + 10, zLevel, 0x77000000,
                0x00000000);
        graphics.fillGradient(leftPos + 3, topPos + 124, leftPos + 3 + 178, topPos + 124 + 10, zLevel, 0x00000000,
                0x77000000);
        UIRenderHelper.swapAndBlitColor(UIRenderHelper.framebuffer, minecraft.getMainRenderTarget());
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

    public int renderStabilizerEntry(GuiGraphics graphics, StabilizerEntry entry, int yOffset, int mouseX, int mouseY,
                                     float partialTicks) {
        int zLevel = -100;

        AllGuiTextures light = AllGuiTextures.SCHEDULE_CARD_LIGHT;
        AllGuiTextures medium = AllGuiTextures.SCHEDULE_CARD_MEDIUM;
        AllGuiTextures dark = AllGuiTextures.SCHEDULE_CARD_DARK;

        int cardWidth = CARD_WIDTH;
        int cardHeader = CARD_HEADER;
        int maxRows = 0;
        int cardHeight = cardHeader + 10;

        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(leftPos + 25, topPos + yOffset, 0);

        UIRenderHelper.drawStretched(graphics, 0, 1, cardWidth, cardHeight - 2, zLevel, light);
        UIRenderHelper.drawStretched(graphics, 1, 0, cardWidth - 2, cardHeight, zLevel, light);
        UIRenderHelper.drawStretched(graphics, 1, 1, cardWidth - 2, cardHeight - 2, zLevel, dark);
        UIRenderHelper.drawStretched(graphics, 2, 2, cardWidth - 4, cardHeight - 4, zLevel, medium);
        UIRenderHelper.drawStretched(graphics, 2, 2, cardWidth - 4, cardHeader, zLevel, medium);

        AllGuiTextures.SCHEDULE_CARD_REMOVE.render(graphics, cardWidth - 14, 2);

        int i = teleportEntries.indexOf(entry);
        if (i > 0)
            AllGuiTextures.SCHEDULE_CARD_MOVE_UP.render(graphics, cardWidth, cardHeader - 14);
        if (i < teleportEntries.size() - 1)
            AllGuiTextures.SCHEDULE_CARD_MOVE_DOWN.render(graphics, cardWidth, cardHeader);

        matrixStack.popPose();

        return cardHeight;
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

    public boolean action(@Nullable GuiGraphics graphics, double mouseX, double mouseY, int click) {
        Component empty = Components.immutableEmpty();

        int mx = (int) mouseX;
        int my = (int) mouseY;
        int x = mx - leftPos - 25;
        int y = my - topPos - 25;
        if (x < 0 || x >= 205)
            return false;
        if (y < 0 || y >= 173)
            return false;
        y += scroll.getValue(0);

        for (int i = 0; i < teleportEntries.size(); i++) {
            StabilizerEntry entry = teleportEntries.get(i);
            int cardHeight = CARD_HEADER + 10;

            if (y >= cardHeight + 5) {
                y -= cardHeight + 10;
                if (y < 0)
                    return false;
                continue;
            }

            int fieldSize = 110;
            if (x > 0 && x <= fieldSize && y > 0 && y <= cardHeight || x > 0 && x <= fieldSize + 12 && y > 14 && y <= cardHeight) {
                List<Component> components = new ArrayList<>();
                components.add(Component.literal("Click to teleport"));
                renderActionTooltip(graphics, components, mx, my);
                if (click == 0) {}
                return true;
            }

            if (x > fieldSize && x <= fieldSize + 12) {
                if (y > 0 && y <= 14) {
                    renderActionTooltip(graphics, ImmutableList.of(Lang.translateDirect("gui.stabilizer.remove_entry")),
                            mx, my);
                    if (click == 0) {
                        teleportEntries.remove(entry);
                        init();
                    }
                    return true;
                }
            }

            if (x > fieldSize + 14 && x < fieldSize + 26) {
                if (y > 7 && y <= 20 && i > 0) {
                    renderActionTooltip(graphics, ImmutableList.of(Lang.translateDirect("gui.stabilizer.move_up")), mx,
                            my);
                    if (click == 0) {
                        teleportEntries.remove(entry);
                        teleportEntries.add(i - 1, entry);
                        init();
                    }
                    return true;
                }
                if (y > 20 && y <= 33 && i < teleportEntries.size() - 1) {
                    renderActionTooltip(graphics, ImmutableList.of(Lang.translateDirect("gui.stabilizer.move_down")), mx,
                            my);
                    if (click == 0) {
                        teleportEntries.remove(entry);
                        teleportEntries.add(i + 1, entry);
                        init();
                    }
                    return true;
                }
            }

            if (x < 0 || x > 15 || y > 20)
                return false;
            return true;
        }
        return true;
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
    public List<Rect2i> getExtraAreas() {
        return extraAreas;
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        float chaseTarget = scroll.getChaseTarget();
        float max = 40 - 120;
        for (StabilizerEntry scheduleEntry : teleportEntries) {
            max += CARD_HEADER + 5 + 10;
        }
        if (max > 0) {
            chaseTarget -= pDelta * 12;
            chaseTarget = Mth.clamp(chaseTarget, 0, max);
            scroll.chase((int) chaseTarget, 0.7f, LerpedFloat.Chaser.EXP);
        } else
            scroll.chase(0, 0.7f, LerpedFloat.Chaser.EXP);

        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }
}
