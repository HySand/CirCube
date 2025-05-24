package me.zephyr.circube.data.recipes;

import com.google.common.base.Supplier;
import com.simibubi.create.foundation.data.recipe.MechanicalCraftingRecipeBuilder;
import me.zephyr.circube.CirCube;
import me.zephyr.circube.CirCubeItems;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public class CirCubeMechanicalCraftingRecipeGen extends CirCubeRecipeProvider {
    GeneratedRecipe
            REINFORCED_IRON_SHEET = create(CirCubeItems.REINFORCED_IRON_SHEET::get).returns(2)
            .recipe(b -> b
                    .key('N', Ingredient.of(I.ironNugget()))
                    .key('S', Ingredient.of(I.ironSheet()))
                    .patternLine(" N N ")
                    .patternLine("SSSSS")
                    .patternLine(" N N ")
                    .patternLine("SSSSS")
                    .patternLine(" N N ")
                    .disallowMirrored()),
            REINFORCED_COPPER_SHEET = create(CirCubeItems.REINFORCED_COPPER_SHEET::get).returns(3)
                    .recipe(b -> b
                            .key('N', Ingredient.of(I.copperNugget()))
                            .key('S', Ingredient.of(I.copperSheet()))
                            .patternLine(" N N ")
                            .patternLine("SSSSS")
                            .patternLine(" N N ")
                            .patternLine("SSSSS")
                            .patternLine(" N N ")
                            .disallowMirrored()),
            REINFORCED_GOLDEN_SHEET = create(CirCubeItems.REINFORCED_GOLDEN_SHEET::get).returns(2)
                    .recipe(b -> b
                            .key('N', Ingredient.of(Items.GOLD_NUGGET))
                            .key('S', Ingredient.of(I.goldSheet()))
                            .patternLine(" N N ")
                            .patternLine("SSSSS")
                            .patternLine(" N N ")
                            .patternLine("SSSSS")
                            .patternLine(" N N ")
                            .disallowMirrored()),
            REINFORCED_BRASS_SHEET = create(CirCubeItems.REINFORCED_BRASS_SHEET::get).returns(3)
                    .recipe(b -> b
                            .key('N', Ingredient.of(I.brassNugget()))
                            .key('S', Ingredient.of(I.brassSheet()))
                            .patternLine("SSSSS")
                            .patternLine("SNSNS")
                            .patternLine("SSSSS")
                            .patternLine("SNSNS")
                            .patternLine("SSSSS")
                            .disallowMirrored()),
            REINFORCED_STEEL_SHEET = create(CirCubeItems.REINFORCED_STEEL_SHEET::get).returns(3)
                    .recipe(b -> b
                            .key('N', Ingredient.of(I.steelNugget()))
                            .key('S', Ingredient.of(CirCubeItems.STEEL_SHEET.get()))
                            .patternLine("SSSSS")
                            .patternLine("SNSNS")
                            .patternLine("SSSSS")
                            .patternLine("SNSNS")
                            .patternLine("SSSSS")
                            .disallowMirrored());

    public CirCubeMechanicalCraftingRecipeGen(PackOutput output) {
        super(output);
    }

    GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder(result);
    }

    class GeneratedRecipeBuilder {

        private String suffix;
        private Supplier<ItemLike> result;
        private int amount;

        public GeneratedRecipeBuilder(Supplier<ItemLike> result) {
            this.suffix = "";
            this.result = result;
            this.amount = 1;
        }

        GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }

        GeneratedRecipeBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        GeneratedRecipe recipe(UnaryOperator<MechanicalCraftingRecipeBuilder> builder) {
            return register(consumer -> {
                MechanicalCraftingRecipeBuilder b =
                        builder.apply(MechanicalCraftingRecipeBuilder.shapedRecipe(result.get(), amount));
                ResourceLocation location = CirCube.asResource("mechanical_crafting/" + CatnipServices.REGISTRIES.getKeyOrThrow(result.get()
                                .asItem())
                        .getPath() + suffix);
                b.build(consumer, location);
            });
        }
    }

    @Override
    public @NotNull String getName() {
        return "CirCube's Mechanical Crafting Recipes";
    }
}
