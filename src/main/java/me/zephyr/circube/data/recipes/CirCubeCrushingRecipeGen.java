package me.zephyr.circube.data.recipes;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

import java.util.function.UnaryOperator;

public class CirCubeCrushingRecipeGen extends CirCubeProcessingRecipeGen {
    GeneratedRecipe
            TUFF = mineralRecycling(AllPaletteStoneTypes.TUFF, b -> b.duration(350)
            .output(.3f, Items.FLINT, 1)
            .output(.02f, Items.LAPIS_LAZULI, 1)
            .output(.1f, AllItems.COPPER_NUGGET.get(), 1)
            .output(.1f, AllItems.ZINC_NUGGET.get(), 1)
            .output(.1f, Items.IRON_NUGGET, 1));

    public CirCubeCrushingRecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.CRUSHING;
    }

    protected GeneratedRecipe mineralRecycling(AllPaletteStoneTypes type,
                                               UnaryOperator<ProcessingRecipeBuilder<ProcessingRecipe<?>>> transform) {
        create(Create.asResource(type.name().toLowerCase() + "_recycling"), b -> transform.apply(b.require(type.materialTag)));
        return create(Create.ID, type.getBaseBlock()::get, transform);
    }
}
