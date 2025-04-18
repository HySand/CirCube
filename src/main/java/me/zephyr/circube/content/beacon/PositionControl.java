package me.zephyr.circube.content.beacon;

import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import com.simibubi.create.foundation.utility.CreateLang;
import me.zephyr.circube.CirCubeLang;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.lang.Lang;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.function.Consumer;

public enum PositionControl {
    NORTH, EAST, SOUTH, WEST;

    private static String[] valuesAsString() {
        PositionControl[] values = values();
        return Arrays.stream(values)
                .map(dc -> Lang.asId(dc.name()))
                .toList()
                .toArray(new String[values.length]);
    }

    @OnlyIn(Dist.CLIENT)
    public static Pair<ScrollInput, Label> createWidget(int x, int y, Consumer<PositionControl> callback,
                                                        PositionControl initial) {
        PositionControl playerFacing = EAST;
        Entity cameraEntity = Minecraft.getInstance().cameraEntity;
        if (cameraEntity != null) {
            Direction direction = cameraEntity.getDirection();
            if (direction == Direction.WEST)
                playerFacing = WEST;
            if (direction == Direction.NORTH)
                playerFacing = NORTH;
            if (direction == Direction.SOUTH)
                playerFacing = SOUTH;
        }

        Label label = new Label(x + 4, y + 6, Component.empty()).withShadow();
        ScrollInput input = new SelectionScrollInput(x, y, 53, 16)
                .forOptions(CirCubeLang.translatedOptions("beacon.position_control", valuesAsString()))
                .titled(CirCubeLang.translateDirect("beacon.position_control"))
                .calling(s -> {
                    PositionControl mode = values()[s];
                    label.text = CreateLang.translateDirect("contraption.door_control." + Lang.asId(mode.name()) + ".short");
                    callback.accept(mode);
                })
                .addHint(CreateLang.translateDirect("contraption.door_control.player_facing",
                        CreateLang.translateDirect("contraption.door_control." + Lang.asId(playerFacing.name()) + ".short")))
                .setState(initial.ordinal());
        input.onChanged();
        return Pair.of(input, label);
    }
}
