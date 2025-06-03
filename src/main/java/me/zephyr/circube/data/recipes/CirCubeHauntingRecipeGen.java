package me.zephyr.circube.data.recipes;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.simibubi.create.AllRecipeTypes;
import net.minecraft.data.PackOutput;

public class CirCubeHauntingRecipeGen extends CirCubeProcessingRecipeGen {

    GeneratedRecipe

            RAW_SCARLET_NEODYMIUM = create("raw_scarlet_neodymium", b -> b.require(ACItemRegistry.PURE_DARKNESS.get())
            .output(ACItemRegistry.RAW_SCARLET_NEODYMIUM.get()));

    public CirCubeHauntingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.HAUNTING;
    }
}
