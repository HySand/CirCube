package me.zephyr.circube.data.recipes;

import com.jesz.createdieselgenerators.CDGFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import me.zephyr.circube.CirCubeItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import static me.zephyr.circube.CirCube.MOD_ID;

public class CirCubeMixingRecipeGen extends ProcessingRecipeGen {
    GeneratedRecipe

            RAW_STEEL = create(ResourceLocation.fromNamespaceAndPath(MOD_ID, "raw_steel"), b -> b.require(CirCubeRecipeProvider.I.iron())
            .require(CirCubeRecipeProvider.I.iron())
            .require(CirCubeRecipeProvider.I.iron())
            .require(CirCubeRecipeProvider.I.iron())
            .require(CirCubeRecipeProvider.I.iron())
            .require(CirCubeRecipeProvider.I.iron())
            .require(Items.COAL)
            .output(CirCubeItems.RAW_STEEL_INGOT.get(), 6)
            .requiresHeat(HeatCondition.SUPERHEATED)),
            SHELL = create(ResourceLocation.fromNamespaceAndPath(MOD_ID, "shell"), b -> b.require(Items.RED_DYE)
                    .require(CirCubeItems.PLASTIC.get())
                    .require(CirCubeRecipeProvider.I.brassNugget())
                    .output(CirCubeItems.SHELL.get(), 4)
                    .requiresHeat(HeatCondition.HEATED)),
            PLASTIC = create(ResourceLocation.fromNamespaceAndPath(MOD_ID, "plastic"), b -> b.require(Items.GLOWSTONE_DUST)
                    .require(CDGFluids.GASOLINE.get(), 500)
                    .output(CirCubeItems.PLASTIC.get(), 5)
                    .requiresHeat(HeatCondition.SUPERHEATED));

    public CirCubeMixingRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.MIXING;
    }
}
