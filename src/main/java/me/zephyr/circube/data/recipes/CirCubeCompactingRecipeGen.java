package me.zephyr.circube.data.recipes;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import static me.zephyr.circube.CirCube.MOD_ID;

public class CirCubeCompactingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe

            NETHERRACK = create(ResourceLocation.fromNamespaceAndPath(MOD_ID, "netherrack"), b -> b.require(Blocks.STONE)
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
