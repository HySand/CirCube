package me.zephyr.circube.data.recipes;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.jesz.createdieselgenerators.CDGFluids;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import me.zephyr.circube.CirCubeItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

public class CirCubeMixingRecipeGen extends CirCubeProcessingRecipeGen {
    GeneratedRecipe

            RAW_STEEL = create("raw_steel", b -> b.require(CirCubeRecipeProvider.I.iron())
            .require(CirCubeRecipeProvider.I.iron())
            .require(CirCubeRecipeProvider.I.iron())
            .require(CirCubeRecipeProvider.I.iron())
            .require(CirCubeRecipeProvider.I.iron())
            .require(CirCubeRecipeProvider.I.iron())
            .require(Items.COAL)
            .output(CirCubeItems.RAW_STEEL_INGOT.get(), 6)
            .requiresHeat(HeatCondition.SUPERHEATED)),

    SHELL = create("shell", b -> b.require(Items.RED_DYE)
            .require(CirCubeItems.PLASTIC.get())
            .require(CirCubeRecipeProvider.I.brassNugget())
            .output(CirCubeItems.SHELL.get(), 4)
            .requiresHeat(HeatCondition.HEATED)),

    PLASTIC = create("plastic", b -> b.require(Items.GLOWSTONE_DUST)
            .require(CDGFluids.GASOLINE.get(), 500)
            .output(CirCubeItems.PLASTIC.get(), 3)
            .requiresHeat(HeatCondition.SUPERHEATED)),

    ACID = create("acid", b -> b.require(ACBlockRegistry.RADROCK.get())
            .require(Fluids.WATER, 500)
            .output(ACFluidRegistry.ACID_FLUID_SOURCE.get(), 100)
            .requiresHeat(HeatCondition.SUPERHEATED)),

    ACID_FROM_ROCK = create("acid_from_rock", b -> b.require(ACBlockRegistry.ACIDIC_RADROCK.get())
            .output(ACFluidRegistry.ACID_FLUID_SOURCE.get(), 1000)
            .requiresHeat(HeatCondition.SUPERHEATED));

    public CirCubeMixingRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.MIXING;
    }
}
