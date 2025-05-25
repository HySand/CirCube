package me.zephyr.circube.data.recipes;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import me.zephyr.circube.CirCubeItems;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;

public class CirCubeRecipeProvider extends CreateRecipeProvider {
    public CirCubeRecipeProvider(PackOutput output) {
        super(output);
    }

    protected static class I {

        static TagKey<Item> redstone() {
            return Tags.Items.DUSTS_REDSTONE;
        }

        static TagKey<Item> planks() {
            return ItemTags.PLANKS;
        }

        static TagKey<Item> woodSlab() {
            return ItemTags.WOODEN_SLABS;
        }

        static TagKey<Item> gold() {
            return Tags.Items.INGOTS_GOLD;
        }

        static TagKey<Item> goldSheet() {
            return AllTags.forgeItemTag("plates/gold");
        }

        static TagKey<Item> stone() {
            return Tags.Items.STONE;
        }

        static ItemLike andesiteAlloy() {
            return AllItems.ANDESITE_ALLOY.get();
        }

        static ItemLike shaft() {
            return AllBlocks.SHAFT.get();
        }

        static ItemLike cog() {
            return AllBlocks.COGWHEEL.get();
        }

        static ItemLike largeCog() {
            return AllBlocks.LARGE_COGWHEEL.get();
        }

        static ItemLike andesiteCasing() {
            return AllBlocks.ANDESITE_CASING.get();
        }

        static ItemLike vault() {
            return AllBlocks.ITEM_VAULT.get();
        }

        static ItemLike stockLink() {
            return AllBlocks.STOCK_LINK.get();
        }

        static TagKey<Item> brass() {
            return AllTags.forgeItemTag("ingots/brass");
        }

        static TagKey<Item> brassSheet() {
            return AllTags.forgeItemTag("plates/brass");
        }

        static TagKey<Item> iron() {
            return Tags.Items.INGOTS_IRON;
        }

        static TagKey<Item> ironNugget() {
            return Tags.Items.NUGGETS_IRON;
        }

        static TagKey<Item> zinc() {
            return AllTags.forgeItemTag("ingots/zinc");
        }

        static TagKey<Item> ironSheet() {
            return AllTags.forgeItemTag("plates/iron");
        }

        static TagKey<Item> sturdySheet() {
            return AllTags.forgeItemTag("plates/obsidian");
        }

        static ItemLike brassCasing() {
            return AllBlocks.BRASS_CASING.get();
        }

        static ItemLike cardboard() {
            return AllItems.CARDBOARD.get();
        }

        static ItemLike railwayCasing() {
            return AllBlocks.RAILWAY_CASING.get();
        }

        static ItemLike electronTube() {
            return AllItems.ELECTRON_TUBE.get();
        }

        static ItemLike precisionMechanism() {
            return AllItems.PRECISION_MECHANISM.get();
        }

        static TagKey<Item> brassBlock() {
            return AllTags.forgeItemTag("storage_blocks/brass");
        }

        static TagKey<Item> zincBlock() {
            return AllTags.forgeItemTag("storage_blocks/zinc");
        }

        static TagKey<Item> wheatFlour() {
            return AllTags.forgeItemTag("flour/wheat");
        }

        static TagKey<Item> copper() {
            return Tags.Items.INGOTS_COPPER;
        }

        static TagKey<Item> copperNugget() {
            return AllTags.forgeItemTag("nuggets/copper");
        }

        static TagKey<Item> copperBlock() {
            return Tags.Items.STORAGE_BLOCKS_COPPER;
        }

        static TagKey<Item> copperSheet() {
            return AllTags.forgeItemTag("plates/copper");
        }

        static TagKey<Item> brassNugget() {
            return AllTags.forgeItemTag("nuggets/brass");
        }

        static TagKey<Item> zincNugget() {
            return AllTags.forgeItemTag("nuggets/zinc");
        }

        static ItemLike copperCasing() {
            return AllBlocks.COPPER_CASING.get();
        }

        static ItemLike refinedRadiance() {
            return AllItems.REFINED_RADIANCE.get();
        }

        static ItemLike shadowSteel() {
            return AllItems.SHADOW_STEEL.get();
        }

        static Ingredient netherite() {
            return Ingredient.of(Tags.Items.INGOTS_NETHERITE);
        }

        static TagKey<Item> steel() {
            return AllTags.forgeItemTag("ingots/steel");
        }

        static TagKey<Item> steelNugget() {
            return AllTags.forgeItemTag("nuggets/steel");
        }

        static TagKey<Item> steelBlock() {
            return AllTags.forgeItemTag("storage_blocks/steel");
        }

        static TagKey<Item> steelSheet() {
            return AllTags.forgeItemTag("plates/steel");
        }

        static ItemLike kineticMechanism() {
            return CirCubeItems.KINETIC_MECHANISM.get();
        }

        static ItemLike logisticsMechanism() {
            return CirCubeItems.LOGISTICS_MECHANISM.get();
        }

        static ItemLike integratedCircuit() {
            return CirCubeItems.INTEGRATED_CIRCUIT.get();
        }

        static ItemLike sealedMechanism() {
            return CirCubeItems.SEALED_MECHANISM.get();
        }

    }
}
