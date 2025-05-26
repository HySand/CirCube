package me.zephyr.circube.data.recipes;

import com.jesz.createdieselgenerators.CDGRecipes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

import static me.zephyr.circube.CirCube.MOD_ID;

public class CirCubeBulkFermentingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe

            Dirt = create(ResourceLocation.fromNamespaceAndPath(MOD_ID, Blocks.DIRT.asItem().toString()), b -> b.require(Blocks.MOSSY_COBBLESTONE)
            .output(Blocks.DIRT)
            .requiresHeat(HeatCondition.HEATED)
            .duration(3000));

    public CirCubeBulkFermentingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected CDGRecipes getRecipeType() {
        return CDGRecipes.BULK_FERMENTING;
    }
}
