package me.zephyr.circube;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static me.zephyr.circube.CirCube.MODID;

public enum CirCubeGuiTextures implements ScreenElement {

    STABILIZER("stabilizer", 192, 165),
    STABILIZER_CARD_DARK("stabilizer", 3, 169, 1, 1),
    STABILIZER_CARD_MEDIUM("stabilizer", 4, 169, 1, 1),
    STABILIZER_CARD_LIGHT("stabilizer", 5, 169, 1, 1),
    STABILIZER_CARD_MOVE_DOWN("stabilizer", 0, 176, 12, 12),
    STABILIZER_CARD_MOVE_UP("stabilizer", 12, 176, 12, 12),
    STABILIZER_CARD_FORGET("stabilizer", 24, 176, 12, 12),
    ANDESITE_BEACON("mechanical_beacon", 134, 102),
    BRASS_BEACON("mechanical_beacon", 0, 102, 134, 102),
    ANDESITE_EDIT_NAME("mechanical_beacon", 0, 208, 13, 13),
    BRASS_EDIT_NAME("mechanical_beacon", 13, 208, 13, 13);

    public final ResourceLocation location;
    public final int width;
    public final int height;
    public final int startX;
    public final int startY;

    CirCubeGuiTextures(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    CirCubeGuiTextures(int startX, int startY) {
        this("icons", startX * 16, startY * 16, 16, 16);
    }

    CirCubeGuiTextures(String location, int startX, int startY, int width, int height) {
        this(MODID, location, startX, startY, width, height);
    }

    CirCubeGuiTextures(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(namespace, "textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }


    @OnlyIn(Dist.CLIENT)
    public void bind() {
        RenderSystem.setShaderTexture(0, location);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(GuiGraphics graphics, int x, int y) {
        graphics.blit(location, x, y, startX, startY, width, height);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(GuiGraphics graphics, int x, int y, Color c) {
        bind();
        UIRenderHelper.drawColoredTexture(graphics, c, x, y, startX, startY, width, height);
    }
}
