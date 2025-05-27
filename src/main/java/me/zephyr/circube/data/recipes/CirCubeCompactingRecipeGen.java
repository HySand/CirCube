package me.zephyr.circube.data.recipes;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class CirCubeCompactingRecipeGen extends CirCubeProcessingRecipeGen {

    GeneratedRecipe

            NETHERRACK = create("netherrack", b -> b.require(Blocks.STONE)
            .require(Items.NETHER_WART)
            .require(Items.MAGMA_CREAM)
            .output(Blocks.NETHERRACK)
            .requiresHeat(HeatCondition.SUPERHEATED));

    public CirCubeCompactingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.COMPACTING;
    }
}
