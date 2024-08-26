package me.zephyr.circube.content.stabilizer;

import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import me.zephyr.circube.CirCubeTextures;
import net.minecraft.client.gui.GuiGraphics;

public class StabilizerScreen extends AbstractSimiScreen {
    private final CirCubeTextures background = CirCubeTextures.STABILIZER;

    public StabilizerScreen() {

    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int baseX = guiLeft;
        int baseY = guiTop;
    }

    @Override
    protected void init() {
        setWindowSize(background.width, background.height + 2 + AllGuiTextures.PLAYER_INVENTORY.height);
        setWindowOffset(-20, 0);
        super.init();
    }
}
