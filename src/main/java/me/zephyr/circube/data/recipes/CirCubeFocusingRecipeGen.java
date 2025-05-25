package me.zephyr.circube.data.recipes;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import me.zephyr.circube.CirCube;
import net.lpcamors.optical.CORecipeTypes;
import net.lpcamors.optical.compat.jei.FocusingRecipeBuilder;
import net.lpcamors.optical.recipes.FocusingRecipe;
import net.lpcamors.optical.recipes.FocusingRecipeParams;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static me.zephyr.circube.CirCube.MOD_ID;

public class CirCubeFocusingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe

            BLAZE_POWDER = create(() -> ResourceLocation.fromNamespaceAndPath(MOD_ID, Items.BLAZE_POWDER.toString()), f -> (FocusingRecipeBuilder) f.require(Items.GLOWSTONE_DUST)
                    .output(Items.BLAZE_POWDER)
                    .duration(200), FocusingRecipeParams.BeamTypeCondition.GAMMA);

    public CirCubeFocusingRecipeGen(PackOutput generator) {
        super(generator);
    }

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(Supplier<ResourceLocation> name,
                                                                     UnaryOperator<FocusingRecipeBuilder> transform,
                                                                     FocusingRecipeParams.BeamTypeCondition b) {
        ProcessingRecipeSerializer<FocusingRecipe> serializer = getSerializer();

        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new FocusingRecipeBuilder(serializer.getFactory(), name.get(), b)).build(c);

        all.add(generatedRecipe);
        return generatedRecipe;
    }

    @Override
    protected CORecipeTypes getRecipeType() {
        return CORecipeTypes.FOCUSING;
    }
}
