package me.zephyr.circube.data.recipes;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import me.zephyr.circube.CirCubeItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import static me.zephyr.circube.CirCube.MOD_ID;

public class CirCubePressingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe
            STEEL = create(ResourceLocation.fromNamespaceAndPath(MOD_ID, "steel_ingot"), b -> b.require(CirCubeRecipeProvider.I.steel())
            .output(CirCubeItems.STEEL_SHEET.get()));

    public CirCubePressingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.PRESSING;
    }
}
