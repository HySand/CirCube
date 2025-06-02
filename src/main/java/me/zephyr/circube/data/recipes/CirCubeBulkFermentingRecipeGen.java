package me.zephyr.circube.data.recipes;

import com.jesz.createdieselgenerators.CDGRecipes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;

public class CirCubeBulkFermentingRecipeGen extends CirCubeProcessingRecipeGen {

    GeneratedRecipe

            Dirt = create(Blocks.DIRT.asItem().toString(), b -> b.require(Blocks.MOSSY_COBBLESTONE)
            .output(Blocks.DIRT)
            .requiresHeat(HeatCondition.HEATED)
            .duration(1000));

    public CirCubeBulkFermentingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected CDGRecipes getRecipeType() {
        return CDGRecipes.BULK_FERMENTING;
    }
}
