package me.zephyr.circube.content.vlobby;

import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.zephyr.circube.CirCubeGuiTextures;
import me.zephyr.circube.CirCubeLang;
import me.zephyr.circube.CirCubePackets;
import me.zephyr.circube.content.vlobby.packets.JoinRoomPacket;
import me.zephyr.circube.content.vlobby.packets.LeaveRoomPacket;
import me.zephyr.circube.content.vlobby.packets.RoomEntriesRequestPacket;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.gui.AbstractSimiScreen;
import net.createmod.catnip.gui.UIRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import javax.annotation.Nullable;
import java.util.*;


public class LobbyScreen extends AbstractSimiScreen {
    private static final int CARD_HEIGHT = 51;
    private static final int CARD_WIDTH = 114;

    private final LerpedFloat scroll = LerpedFloat.linear().startWithValue(0);
    private final List<RoomEntry> roomEntries = new ArrayList<>();

    private boolean hasRequestedData = false;

    private int tickCount = 0;

    public LobbyScreen() {
        super();
    }

    private static ResourceLocation getSkinLocation(UUID uuid) {
        GameProfile profile = new GameProfile(uuid, null);
        Minecraft minecraft = Minecraft.getInstance();
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures = minecraft.getSkinManager().getInsecureSkinInformation(profile);

        if (textures.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            MinecraftProfileTexture skinTexture = textures.get(MinecraftProfileTexture.Type.SKIN);
            return minecraft.getSkinManager().registerTexture(skinTexture, MinecraftProfileTexture.Type.SKIN);
        } else {
            return DefaultPlayerSkin.getDefaultSkin(profile.getId());
        }
    }

    private void loadLobbyEntries() {
        if (minecraft != null && minecraft.player != null) {
            CirCubePackets.CHANNEL.sendToServer(new RoomEntriesRequestPacket());
        }
    }

    public void updateRoomEntries(List<RoomEntry> entries) {
        this.roomEntries.clear();
        this.roomEntries.addAll(entries);
        this.init();
    }

    @Override
    protected void init() {
        CirCubeGuiTextures bg = CirCubeGuiTextures.VLOBBY;
        setWindowSize(bg.width, bg.height);
        super.init();
        clearWidgets();

        if (!hasRequestedData) {
            loadLobbyEntries();
            hasRequestedData = true;
        }
    }

    @Override
    public void tick() {
        scroll.tickChaser();
        tickCount++;
        if (tickCount % 60 == 0) {
            loadLobbyEntries();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        partialTicks = minecraft.getFrameTime();


        renderBackground(graphics);
        renderWindow(graphics, mouseX, mouseY, partialTicks);
        for (Renderable widget : this.renderables)
            widget.render(graphics, mouseX, mouseY, partialTicks);
        renderWindowForeground(graphics, mouseX, mouseY, partialTicks);

    }

    protected void renderVLobby(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        UIRenderHelper.swapAndBlitColor(minecraft.getMainRenderTarget(), UIRenderHelper.framebuffer);

        int yOffset = 18;
        float scrollOffset = -scroll.getValue(partialTicks);

        for (int i = 0; i <= roomEntries.size(); i++) {
            startStencil(graphics, guiLeft + 8, guiTop + 8, 147, 164);
            matrixStack.pushPose();
            matrixStack.translate(0, scrollOffset, 0);

            if (i == roomEntries.size()) {
                if (i > 0)
                    yOffset += 6;
                matrixStack.popPose();
                endStencil();
                break;
            }

            RoomEntry entry = roomEntries.get(i);
            int cardY = yOffset;
            int cardHeight = renderRoomEntry(graphics, entry, cardY, mouseX, mouseY, partialTicks);
            yOffset += cardHeight;

            if (i + 1 < roomEntries.size()) {
                yOffset += 6;
            }

            matrixStack.popPose();
            endStencil();

            startStencil(graphics, guiLeft + 8, guiTop + 8, 147, 164);
            matrixStack.pushPose();
            matrixStack.translate(0, scrollOffset, 0);
            renderRoomInformations(graphics, entry, cardY, mouseX, mouseY, partialTicks, cardHeight, i);
            matrixStack.popPose();
            endStencil();
        }

        UIRenderHelper.swapAndBlitColor(UIRenderHelper.framebuffer, minecraft.getMainRenderTarget());
    }

    public int renderRoomEntry(GuiGraphics graphics, RoomEntry entry, int yOffset, int mouseX, int mouseY,
                               float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(guiLeft + 16, guiTop + yOffset, 0);

        int cardHeight = CARD_HEIGHT;
        if (entry.getMaxPlayers() > 5) {
            cardHeight += 22;
            CirCubeGuiTextures.VLOBBY_LARGE_CARD.render(graphics, 0, 0);
        } else {
            CirCubeGuiTextures.VLOBBY_CARD.render(graphics, 0, 0);
        }

        matrixStack.popPose();

        return cardHeight;
    }

    public void renderRoomInformations(GuiGraphics graphics, RoomEntry entry, int yOffset, int mouseX,
                                       int mouseY, float partialTicks, int cardHeight, int entryIndex) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(guiLeft + 25, guiTop + yOffset, 0);

        FormattedText displayText = FormattedText.of(entry.getName());
        graphics.drawString(font, font.substrByWidth(displayText, 120)
                .getString(), 2, 7, 0xff_f2f2ee);
        int difficultyXOffset = 80;
        for (int i = 0; i < entry.getDifficulty(); i++) {
            CirCubeGuiTextures.VLOBBY_DIFFICULTY.render(graphics, difficultyXOffset, 6);
            difficultyXOffset -= 22;
        }
        int slotXOffset = 2;
        int slotYOffset = 25;
        for (int i = 0; i < Math.min(entry.getMaxPlayers(), 5); i++) {
            CirCubeGuiTextures.VLOBBY_SLOT.render(graphics, slotXOffset, slotYOffset);
            if (entry.getPlayers().size() > i) {
                ResourceLocation resourceLocation = getSkinLocation(entry.getPlayers().get(i));
                graphics.blit(resourceLocation, slotYOffset - 22, slotYOffset + 1, 16, 16, 8, 8, 8, 8, 64, 64);
                graphics.blit(resourceLocation, slotXOffset - 22, slotYOffset + 1, 16, 16, 40, 8, 8, 8, 64, 64);

            }
            slotXOffset += 19;
        }
        if (entry.getMaxPlayers() > 5) {
            slotXOffset = 2;
            slotYOffset += 19;
            for (int i = 5; i < Math.min(entry.getMaxPlayers(), 10); i++) {
                CirCubeGuiTextures.VLOBBY_SLOT.render(graphics, slotXOffset, slotYOffset);
                if (entry.getPlayers().size() > i) {
                    ResourceLocation resourceLocation = getSkinLocation(entry.getPlayers().get(i));
                    graphics.blit(resourceLocation, slotYOffset - 22, slotYOffset + 1, 16, 16, 8, 8, 8, 8, 64, 64);
                    graphics.blit(resourceLocation, slotXOffset - 22, slotYOffset + 1, 16, 16, 40, 8, 8, 8, 64, 64);

                }
                slotXOffset += 19;
            }
        }

        matrixStack.popPose();
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        float chaseTarget = scroll.getChaseTarget();
        float max = 40 - 179;
        for (RoomEntry roomEntry : roomEntries) {
            max += CARD_HEIGHT + 5;
            if (roomEntry.getMaxPlayers() > 5)
                max += 22;
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
    protected void renderWindowForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        super.renderWindowForeground(graphics, mouseX, mouseY, partialTicks);
        action(graphics, mouseX, mouseY, -1);
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        CirCubeGuiTextures.VLOBBY.render(graphics, guiLeft, guiTop);
        renderVLobby(graphics, mouseX, mouseY, partialTicks);
    }

    public boolean action(@Nullable GuiGraphics graphics, double mouseX, double mouseY, int click) {
        Component empty = Component.empty();

        int mx = (int) mouseX;
        int my = (int) mouseY;
        int x = mx - guiLeft - 15;
        int y = my - guiTop - 17;
        if (x < 0 || x >= 114)
            return false;
        if (y < 0 || y >= 183)
            return false;
        y += scroll.getValue(0);

        for (RoomEntry entry : roomEntries) {
            int cardHeight = CARD_HEIGHT;
            if (entry.getMaxPlayers() > 5)
                cardHeight += 22;

            if (y >= cardHeight + 5) {
                y -= cardHeight + 6;
                if (y < 0)
                    return false;
                continue;
            }

            int fieldSize = 114;
            if (x > 0 && x <= fieldSize && y > 0 && y <= cardHeight) {

                UUID player = minecraft.player.getUUID();
                if (entry.isStarted()) {
                    renderActionTooltip(graphics, ImmutableList.of(CirCubeLang.translateDirect("gui.vlobby.started")),
                            mx, my);
                } else if (entry.getPlayers().contains(player)) {
                    renderActionTooltip(graphics, ImmutableList.of(CirCubeLang.translateDirect("gui.vlobby.leave")),
                            mx, my);
                    if (click == 0) {
                        entry.removePlayer(player);
                        CirCubePackets.CHANNEL.sendToServer(new LeaveRoomPacket(entry.getId()));
                        init();
                    }
                } else if (hasStartedGame(player)) {
                    renderActionTooltip(graphics, ImmutableList.of(CirCubeLang.translateDirect("gui.vlobby.can_not_join")),
                            mx, my);
                } else {
                    renderActionTooltip(graphics, ImmutableList.of(CirCubeLang.translateDirect("gui.vlobby.join")),
                            mx, my);
                    if (click == 0) {
                        removePlayerFromOtherEntries(player);
                        entry.addPlayer(player);
                        if (entry.getPlayers().size() == entry.getMaxPlayers()) {
                            entry.startGame();
                        }
                        CirCubePackets.CHANNEL.sendToServer(new JoinRoomPacket(entry.getId()));
                        init();
                    }
                }

                return true;
            }
            return x >= 0 && x <= 15 && y <= 20;
        }
        return true;
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

    private void renderActionTooltip(@Nullable GuiGraphics graphics, List<Component> tooltip, int mx, int my) {
        if (graphics != null)
            graphics.renderTooltip(font, tooltip, Optional.empty(), mx, my);
    }

    private boolean hasStartedGame(UUID uuid) {
        for (RoomEntry entry : roomEntries) {
            if (entry.isStarted() && entry.getPlayers().contains(uuid)) return true;
        }
        return false;
    }

    private void removePlayerFromOtherEntries(UUID uuid) {
        for (RoomEntry entry : roomEntries) {
            entry.getPlayers().remove(uuid);
        }
    }
}
