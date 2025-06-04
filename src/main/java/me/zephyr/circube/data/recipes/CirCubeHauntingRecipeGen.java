package me.zephyr.circube.data.recipes;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.simibubi.create.AllRecipeTypes;
import me.zephyr.circube.CirCubeItems;
import net.minecraft.data.PackOutput;

public class CirCubeHauntingRecipeGen extends CirCubeProcessingRecipeGen {

    GeneratedRecipe

            RAW_AZURE_NEODYMIUM = create("raw_azure_neodymium", b -> b.require(CirCubeItems.RAW_PALE_NEODYMIUM.get())
            .output(ACItemRegistry.RAW_AZURE_NEODYMIUM.get()));

    public CirCubeHauntingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.HAUNTING;
    }
}
