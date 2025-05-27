package me.zephyr.circube.data.recipes;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.simibubi.create.AllRecipeTypes;
import me.zephyr.circube.CirCubeItems;
import net.minecraft.data.PackOutput;

public class CirCubeWashingRecipeGen extends CirCubeProcessingRecipeGen {

    GeneratedRecipe

            PURE_DARKNESS = create("pure_darkness", b -> b.require(ACItemRegistry.PURE_DARKNESS.get())
            .output(CirCubeItems.PURIFIED_DARKNESS));

    public CirCubeWashingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.SPLASHING;
    }
}
