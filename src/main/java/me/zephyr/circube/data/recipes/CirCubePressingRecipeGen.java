package me.zephyr.circube.data.recipes;

import com.simibubi.create.AllRecipeTypes;
import me.zephyr.circube.CirCubeItems;
import net.minecraft.data.PackOutput;

public class CirCubePressingRecipeGen extends CirCubeProcessingRecipeGen {

    GeneratedRecipe
            STEEL = create("steel_ingot", b -> b.require(CirCubeRecipeProvider.I.steel())
            .output(CirCubeItems.STEEL_SHEET.get()));

    public CirCubePressingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.PRESSING;
    }
}
