package me.zephyr.circube.data.recipes;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import me.zephyr.circube.CirCubeItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class CirCubeMillingRecipeGen extends CirCubeProcessingRecipeGen {
    GeneratedRecipe
            COAL = create(ResourceLocation.fromNamespaceAndPath(Create.ID, Items.COAL.toString()), b -> b.duration(100)
            .require(Items.COAL)
            .output(CirCubeItems.GRAPHITE_POWDER, 3)
            .output(.1f, CirCubeItems.GRAPHITE_POWDER, 1));

    public CirCubeMillingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.MILLING;
    }
}
