package me.zephyr.circube.data.recipes;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import me.zephyr.circube.CirCubeItems;
import net.minecraft.data.PackOutput;

public class CirCubeCuttingRecipeGen extends CirCubeProcessingRecipeGen {

    GeneratedRecipe

            BRASS_SHELL = create(AllItems.BRASS_SHEET::get, b -> b.duration(200)
            .output(CirCubeItems.BRASS_SHELL.get(), 6)),

    STEEL_SHELL = create(CirCubeItems.STEEL_SHEET::get, b -> b.duration(500)
            .output(CirCubeItems.STEEL_SHELL.get(), 6));

    public CirCubeCuttingRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.CUTTING;
    }

}
