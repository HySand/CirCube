package me.zephyr.circube.data.recipes;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import me.zephyr.circube.CirCubeItems;
import net.minecraft.data.PackOutput;

import static me.zephyr.circube.CirCube.MOD_ID;

public class CirCubeCuttingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe

            BRASS_SHELL = create(MOD_ID, AllItems.BRASS_SHEET::get, b -> b.duration(200)
            .output(CirCubeItems.BRASS_SHELL.get(), 6));

    public CirCubeCuttingRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.CUTTING;
    }

}
