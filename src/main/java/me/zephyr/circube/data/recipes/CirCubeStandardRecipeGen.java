package me.zephyr.circube.data.recipes;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jesz.createdieselgenerators.CDGBlocks;
import com.jesz.createdieselgenerators.CDGItems;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.recipe.CompatMetals;
import com.simibubi.create.foundation.data.recipe.Mods;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import me.zephyr.circube.CirCube;
import me.zephyr.circube.CirCubeBlocks;
import me.zephyr.circube.CirCubeItems;
import net.createmod.catnip.platform.CatnipServices;
import net.lpcamors.optical.blocks.COBlocks;
import net.lpcamors.optical.items.COItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static me.zephyr.circube.CirCube.MOD_ID;

public class CirCubeStandardRecipeGen extends CirCubeRecipeProvider {
    final List<GeneratedRecipe> all = new ArrayList<>();
    String currentFolder = "";

    private final Marker CRAFTING = enterFolder("/");
    GeneratedRecipe

            ENGINE_PISTON = create(CDGItems.ENGINE_PISTON).unlockedBy(I::sealedMechanism).withNamespace("createdieselgenerators")
            .viaShaped(b -> b.define('A', I.sealedMechanism())
                    .define('S', I.shaft())
                    .define('Z', I.zincNugget())
                    .pattern("A  ")
                    .pattern(" S ")
                    .pattern("  Z")),

    PUMPJACK_HOLE = create(CDGBlocks.PUMPJACK_HOLE).unlockedBy(ACItemRegistry.TECTONIC_SHARD::get).withNamespace("createdieselgenerators")
            .viaShaped(b -> b.define('T', ACItemRegistry.TECTONIC_SHARD.get())
                    .define('F', AllBlocks.FLUID_PIPE.get())
                    .define('C', Blocks.CHAIN)
                    .define('G', I.copperCasing())
                    .pattern(" F ")
                    .pattern("CGC")
                    .pattern(" T ")),

    BLAZE_BURNER = create(CDGBlocks.BURNER).unlockedBy(() -> CirCubeItems.REINFORCED_COPPER_SHEET).withNamespace("createdieselgenerators")
            .viaShaped(b -> b.define('B', AllBlocks.BLAZE_BURNER)
                    .define('#', CirCubeItems.REINFORCED_BRASS_SHEET)
                    .define('@', CirCubeItems.REINFORCED_COPPER_SHEET)
                    .define('I', I.shaft())
                    .define('A', I.andesiteAlloy())
                    .pattern("A#A")
                    .pattern("IBI")
                    .pattern("A@A")),

    CHEMICAL_TURRET = create(CDGBlocks.CHEMICAL_TURRET).unlockedBy(() -> CDGItems.CHEMICAL_SPRAYER).withNamespace("createdieselgenerators")
            .viaShaped(b -> b.define('S', CDGItems.CHEMICAL_SPRAYER)
                    .define('@', I.precisionMechanism())
                    .define('#', CirCubeItems.REINFORCED_COPPER_SHEET)
                    .define('C', I.copperCasing())
                    .pattern("##S")
                    .pattern("#  ")
                    .pattern("@C ")),

    CHEMICAL_TURRET_LIGHTER = create(CDGBlocks.CHEMICAL_TURRET).unlockedBy(() -> CDGItems.CHEMICAL_SPRAYER_LIGHTER).withNamespace("createdieselgenerators").withSuffix("lighter")
            .viaShaped(b -> b.define('S', CDGItems.CHEMICAL_SPRAYER_LIGHTER)
                    .define('@', I.precisionMechanism())
                    .define('#', CirCubeItems.REINFORCED_COPPER_SHEET)
                    .define('C', I.copperCasing())
                    .pattern("##S")
                    .pattern("#  ")
                    .pattern("@C ")),

    CANISTER = create(CDGBlocks.CANISTER).unlockedByTag(() -> I.steelSheet()).withNamespace("createdieselgenerators")
            .viaShaped(b -> b.define('#', I.steelSheet())
                    .define('A', I.andesiteAlloy())
                    .define('B', Blocks.BARREL)
                    .pattern("A A")
                    .pattern("#B#")
                    .pattern("###")),

    OIL_BARREL = create(CDGBlocks.OIL_BARREL).unlockedByTag(() -> Tags.Items.BARRELS_WOODEN).withNamespace("createdieselgenerators")
            .viaShaped(b -> b.define('B', I.steelSheet())
                    .pattern("BBB")
                    .pattern("B B")
                    .pattern("BBB")),

    DIESEL_ENGINE = create(CDGBlocks.DIESEL_ENGINE).unlockedBy(() -> CirCubeItems.REINFORCED_BRASS_SHEET).withNamespace("createdieselgenerators")
            .viaShaped(b -> b.define('K', I.kineticMechanism())
                    .define('P', CDGItems.ENGINE_PISTON)
                    .define('B', CirCubeItems.REINFORCED_BRASS_SHEET.get())
                    .define('F', Items.FLINT)
                    .define('S', Blocks.POLISHED_BLACKSTONE_SLAB)
                    .pattern("PFP")
                    .pattern("BKB")
                    .pattern("SSS")),

    LARGE_DIESEL_ENGINE = create(CDGBlocks.MODULAR_DIESEL_ENGINE).unlockedBy(() -> CDGBlocks.DIESEL_ENGINE).withNamespace("createdieselgenerators")
            .viaShaped(b -> b.define('B', CirCubeItems.REINFORCED_BRASS_SHEET.get())
                    .define('C', CDGBlocks.DIESEL_ENGINE)
                    .define('S', Blocks.POLISHED_BLACKSTONE_SLAB)
                    .define('A', I.andesiteAlloy())
                    .pattern(" A ")
                    .pattern("BCB")
                    .pattern(" S ")),

    HUGE_DIESEL_ENGINE = create(CDGBlocks.HUGE_DIESEL_ENGINE).unlockedBy(() -> AllBlocks.STEAM_ENGINE).withNamespace("createdieselgenerators")
            .viaShaped(b -> b.define('S', AllBlocks.STEAM_ENGINE.get())
                    .define('P', AllBlocks.FLUID_PIPE.get())
                    .define('R', CirCubeItems.REINFORCED_BRASS_SHEET.get())
                    .define('F', Items.FLINT)
                    .define('A', I.andesiteAlloy())
                    .define('B', I.brassBlock())
                    .pattern("AFA")
                    .pattern("PSP")
                    .pattern("RBR")),

    BEAM_FOCUSER = create(COBlocks.BEAM_FOCUSER).unlockedBy(() -> COItems.OPTICAL_DEVICE).withNamespace("create_optical").withoutMarker()
            .viaShaped(b -> b.define('O', COItems.OPTICAL_DEVICE.get())
                    .define('R', CirCubeItems.REINFORCED_STEEL_SHEET.get())
                    .define('I', I.shaft())
                    .define('C', I.andesiteCasing())
                    .pattern(" O ")
                    .pattern("ICI")
                    .pattern(" R "));

    private final Marker KINETICS = enterFolder("kinetics");
    GeneratedRecipe

            ANDESITE_LIGHT = create(CirCubeBlocks.ANDESITE_LIGHT).unlockedBy(AllItems.POLISHED_ROSE_QUARTZ)
            .viaShaped(b -> b.define('C', AllBlocks.ANDESITE_CASING)
                    .define('R', AllItems.POLISHED_ROSE_QUARTZ)
                    .define('S', I.shaft())
                    .pattern("RRR")
                    .pattern("SCS")
                    .pattern("RRR")),

    BRASS_LIGHT = create(CirCubeBlocks.BRASS_LIGHT).unlockedBy(AllItems.POLISHED_ROSE_QUARTZ)
            .viaShaped(b -> b.define('C', AllBlocks.BRASS_CASING)
                    .define('R', AllItems.POLISHED_ROSE_QUARTZ)
                    .define('S', I.shaft())
                    .pattern("RRR")
                    .pattern("SCS")
                    .pattern("RRR")),

    KINETIC_MECHANISM = create(CirCubeItems.KINETIC_MECHANISM).unlockedBy(() -> AllItems.COPPER_SHEET)
            .viaShaped(b -> b.define('S', AllItems.COPPER_SHEET)
                    .define('C', I.cog())
                    .define('L', AllBlocks.LARGE_COGWHEEL.get())
                    .define('Z', I.zincNugget())
                    .pattern("ZCL")
                    .pattern("SSS")),

    ANDESITE_BEACON = create(CirCubeBlocks.ANDESITE_BEACON).unlockedBy(() -> CirCubeItems.STABILIZER)
            .viaShaped(b -> b
                    .define('S', CirCubeItems.STABILIZER.get())
                    .define('D', AllBlocks.DEPOT.get())
                    .define('C', AllBlocks.ANDESITE_CASING.get())
                    .pattern("S")
                    .pattern("D")
                    .pattern("C")),

    BRASS_BEACON = create(CirCubeBlocks.BRASS_BEACON).unlockedBy(() -> CirCubeItems.STABILIZER)
            .viaShaped(b -> b
                    .define('S', CirCubeItems.STABILIZER.get())
                    .define('D', AllBlocks.DEPOT.get())
                    .define('C', AllBlocks.BRASS_CASING.get())
                    .pattern("S")
                    .pattern("D")
                    .pattern("C")),

    PISTON_EXTENSION_POLE = create(AllBlocks.PISTON_EXTENSION_POLE).returns(2).withNamespace("create")
            .unlockedBy(I::andesiteAlloy)
            .viaShaped(b -> b.define('A', I.andesiteAlloy())
                    .define('S', Items.STICK)
                    .define('G', CirCubeItems.GRAPHITE_POWDER.get())
                    .pattern("SSS")
                    .pattern("AGA")
                    .pattern("SSS")),

    MECHANICAL_PRESS = create(AllBlocks.MECHANICAL_PRESS).unlockedBy(I::andesiteCasing).withNamespace("create")
            .viaShaped(b -> b.define('C', I.andesiteCasing())
                    .define('P', AllBlocks.PISTON_EXTENSION_POLE.get())
                    .define('S', I.shaft())
                    .define('I', AllTags.forgeItemTag("storage_blocks/iron"))
                    .pattern(" P ")
                    .pattern("SCS")
                    .pattern(" I ")),

    MECHANICAL_MIXER = create(AllBlocks.MECHANICAL_MIXER).unlockedBy(I::andesiteAlloy).withNamespace("create")
            .viaShaped(b -> b.define('C', I.andesiteCasing())
                    .define('S', I.cog())
                    .define('I', AllItems.WHISK.get())
                    .define('P', AllBlocks.PISTON_EXTENSION_POLE.get())
                    .pattern(" P ")
                    .pattern("SCS")
                    .pattern(" I ")),

    DEPLOYER = create(AllBlocks.DEPLOYER).unlockedBy(I::electronTube).withNamespace("create")
            .viaShaped(b -> b.define('I', AllItems.BRASS_HAND.get())
                    .define('B', I.electronTube())
                    .define('S', I.shaft())
                    .define('C', I.andesiteCasing())
                    .pattern(" B ")
                    .pattern("SCS")
                    .pattern(" I ")),

    ITEM_VAULT = create(AllBlocks.ITEM_VAULT).unlockedByTag(() -> Tags.Items.BARRELS_WOODEN).withNamespace("create")
            .viaShaped(b -> b.define('B', I.steelSheet())
                    .define('C', Tags.Items.BARRELS_WOODEN)
                    .pattern("B")
                    .pattern("C")
                    .pattern("B")),

    MECHANICAL_ARM = create(AllBlocks.MECHANICAL_ARM::get).unlockedBy(I::brassCasing).returns(1).withNamespace("create")
            .viaShaped(b -> b.define('L', CirCubeItems.REINFORCED_BRASS_SHEET)
                    .define('O', CirCubeItems.REINFORCED_STEEL_SHEET)
                    .define('I', I.precisionMechanism())
                    .define('A', I.andesiteAlloy())
                    .define('C', I.brassCasing())
                    .pattern("OLA")
                    .pattern("L  ")
                    .pattern("IC ")),

    TRAIN_CONTROLS = create(AllBlocks.TRAIN_CONTROLS).unlockedBy(I::railwayCasing).withNamespace("create")
            .viaShaped(b -> b.define('I', I.integratedCircuit())
                    .define('B', Items.LEVER)
                    .define('C', I.railwayCasing())
                    .pattern("B")
                    .pattern("C")
                    .pattern("I")),

    CONTRAPTION_CONTROLS = create(AllBlocks.CONTRAPTION_CONTROLS).unlockedBy(I::andesiteAlloy).withNamespace("create")
            .viaShaped(b -> b.define('B', ItemTags.BUTTONS)
                    .define('C', I.andesiteCasing())
                    .define('I', I.integratedCircuit())
                    .pattern("B")
                    .pattern("C")
                    .pattern("I")),

    MECHANICAL_DRILL = create(AllBlocks.MECHANICAL_DRILL).unlockedBy(I::andesiteCasing).withNamespace("create")
            .viaShaped(b -> b.define('C', I.andesiteCasing())
                    .define('A', I.andesiteAlloy())
                    .define('I', I.steel())
                    .pattern(" A ")
                    .pattern("AIA")
                    .pattern(" C ")),

    WATER_WHEEL = create(AllBlocks.WATER_WHEEL).unlockedBy(I::andesiteAlloy).withNamespace("create")
            .viaShaped(b -> b.define('S', I.planks())
                    .define('C', I.kineticMechanism())
                    .pattern("SSS")
                    .pattern("SCS")
                    .pattern("SSS")),

    WINDMILL_BEARING = create(AllBlocks.WINDMILL_BEARING).unlockedBy(I::andesiteAlloy).withNamespace("create")
            .viaShaped(b -> b.define('B', I.ironSheet())
                    .define('C', I.stone())
                    .define('I', I.kineticMechanism())
                    .pattern("B")
                    .pattern("C")
                    .pattern("I")),

    MECHANICAL_BEARING = create(AllBlocks.MECHANICAL_BEARING).unlockedBy(I::andesiteCasing).withNamespace("create")
            .viaShaped(b -> b.define('B', CirCubeItems.REINFORCED_IRON_SHEET.get())
                    .define('C', I.andesiteCasing())
                    .define('I', I.shaft())
                    .pattern("B")
                    .pattern("C")
                    .pattern("I")),

    CLOCKWORK_BEARING = create(AllBlocks.CLOCKWORK_BEARING).unlockedBy(I::brassCasing).withNamespace("create")
            .viaShaped(b -> b.define('S', I.integratedCircuit())
                    .define('B', CirCubeItems.REINFORCED_IRON_SHEET.get())
                    .define('C', I.brassCasing())
                    .pattern("B")
                    .pattern("C")
                    .pattern("S")),

    STEAM_ENGINE = create(AllBlocks.STEAM_ENGINE).unlockedByTag(I::copper).withNamespace("create")
            .viaShaped(b -> b.define('P', CirCubeItems.SEALED_MECHANISM.get())
                    .define('C', CirCubeItems.REINFORCED_COPPER_SHEET.get())
                    .define('A', I.andesiteAlloy())
                    .define('S', I.kineticMechanism())
                    .pattern(" P ")
                    .pattern("ASA")
                    .pattern("CCC")),

    GANTRY_PINION = create(AllBlocks.GANTRY_CARRIAGE).unlockedBy(I::andesiteCasing).withNamespace("create")
            .viaShaped(b -> b.define('B', CirCubeItems.REINFORCED_IRON_SHEET.get())
                    .define('C', I.andesiteCasing())
                    .define('I', I.logisticsMechanism())
                    .pattern("B")
                    .pattern("C")
                    .pattern("I")),

    ROPE_PULLEY = create(AllBlocks.ROPE_PULLEY).unlockedBy(I::andesiteAlloy).withNamespace("create")
            .viaShaped(b -> b.define('B', I.andesiteCasing())
                    .define('C', ItemTags.WOOL)
                    .define('I', CirCubeItems.REINFORCED_IRON_SHEET.get())
                    .pattern("B")
                    .pattern("C")
                    .pattern("I")),

    HOSE_PULLEY = create(AllBlocks.HOSE_PULLEY).unlockedByTag(I::copper).withNamespace("create")
            .viaShaped(b -> b.define('B', I.copperCasing())
                    .define('C', Items.DRIED_KELP_BLOCK)
                    .define('I', CirCubeItems.REINFORCED_COPPER_SHEET.get())
                    .pattern("B")
                    .pattern("C")
                    .pattern("I")),

    ELEVATOR_PULLEY = create(AllBlocks.ELEVATOR_PULLEY).unlockedByTag(I::brass).withNamespace("create")
            .viaShaped(b -> b.define('B', I.brassCasing())
                    .define('C', Items.DRIED_KELP_BLOCK)
                    .define('I', CirCubeItems.REINFORCED_IRON_SHEET.get())
                    .pattern("B")
                    .pattern("C")
                    .pattern("I")),

    PORTABLE_STORAGE_INTERFACE = create(AllBlocks.PORTABLE_STORAGE_INTERFACE).unlockedBy(I::andesiteCasing).withNamespace("create")
            .viaShapeless(b -> b.requires(I.andesiteCasing())
                    .requires(AllBlocks.CHUTE.get())
                    .requires(I.logisticsMechanism())),

    PORTABLE_FLUID_INTERFACE = create(AllBlocks.PORTABLE_FLUID_INTERFACE).unlockedBy(I::copperCasing).withNamespace("create")
            .viaShapeless(b -> b.requires(I.copperCasing())
                    .requires(AllBlocks.CHUTE.get())
                    .requires(I.logisticsMechanism())),

    SEQUENCED_GEARSHIFT = create(AllBlocks.SEQUENCED_GEARSHIFT).unlockedBy(I::brassCasing).withNamespace("create")
            .viaShapeless(b -> b.requires(I.brassCasing())
                    .requires(I.cog())
                    .requires(I.electronTube())
                    .requires(I.integratedCircuit())),

    CART_ASSEMBLER = create(AllBlocks.CART_ASSEMBLER).unlockedBy(I::andesiteAlloy).withNamespace("create")
            .viaShaped(b -> b.define('L', ItemTags.LOGS)
                    .define('R', I.redstone())
                    .define('C', I.andesiteAlloy())
                    .define('I', CirCubeItems.REINFORCED_IRON_SHEET.get())
                    .pattern("III")
                    .pattern("CRC")
                    .pattern("L L"));

    private final Marker MATERIALS = enterFolder("materials");
    GeneratedRecipe

            STEEL_COMPACT = metalCompacting(ImmutableList.of(CirCubeItems.STEEL_NUGGET, CirCubeItems.STEEL_INGOT, CirCubeBlocks.STEEL_BLOCK),
            ImmutableList.of(I::steelNugget, I::steel, I::steelBlock)),

    TRANSMITTER = create(AllItems.TRANSMITTER).unlockedByTag(I::copper).withNamespace("create")
            .viaShaped(b -> b.define('L', I.copperSheet())
                    .define('N', Items.LIGHTNING_ROD)
                    .define('R', I.integratedCircuit())
                    .pattern(" N ")
                    .pattern("LLL")
                    .pattern(" R ")),

    RED_KEY = create(CirCubeItems.RED_KEY).unlockedByTag(I::steelNugget)
            .viaShaped(b -> b.define('L', I.steelNugget())
                    .define('R', I.redstone())
                    .pattern("  R")
                    .pattern(" R ")
                    .pattern("L  ")),

    BLUE_KEY = create(CirCubeItems.BLUE_KEY).unlockedByTag(I::steelNugget)
            .viaShaped(b -> b.define('L', I.steelNugget())
                    .define('R', Items.LAPIS_LAZULI)
                    .pattern("  R")
                    .pattern(" R ")
                    .pattern("L  ")),

    MAGNETIC_RED_KEY = create(CirCubeItems.MAGNETIC_RED_KEY).unlockedByTag(I::steel)
            .viaShaped(b -> b.define('L', I.steel())
                    .define('R', ACItemRegistry.SCARLET_NEODYMIUM_INGOT.get())
                    .pattern("  R")
                    .pattern(" R ")
                    .pattern("L  ")),

    MAGNETIC_BLUE_KEY = create(CirCubeItems.MAGNETIC_BLUE_KEY).unlockedByTag(I::steel)
            .viaShaped(b -> b.define('L', I.steel())
                    .define('R', ACItemRegistry.AZURE_NEODYMIUM_INGOT.get())
                    .pattern("  R")
                    .pattern(" R ")
                    .pattern("L  ")),

    TUFF = create(() -> Blocks.TUFF).unlockedBy(() -> CirCubeItems.GRAPHITE_POWDER).returns(5)
            .viaShaped(b -> b.define('#', Blocks.MUD)
                    .define('G', CirCubeItems.GRAPHITE_POWDER)
                    .pattern("#G#")
                    .pattern("G#G")
                    .pattern("#G#")),

    LIGHT_CHORUS_FRUIT = create(() -> CirCubeItems.LIGHT_CHORUS_FRUIT).unlockedBy(() -> CirCubeItems.PURIFIED_DARKNESS).returns(1)
            .viaShaped(b -> b.define('F', Items.CHORUS_FRUIT)
                    .define('D', CirCubeItems.PURIFIED_DARKNESS)
                    .pattern(" D ")
                    .pattern("DFD")
                    .pattern(" D ")),

    RAW_SCARLET_NEODYMIUM = create(ACItemRegistry.RAW_SCARLET_NEODYMIUM::get)
            .viaCooking(CirCubeItems.RAW_PALE_NEODYMIUM)
            .rewardXP(.1f)
            .inBlastFurnace(),

    BUDDING_NEODYMIUM = create(() -> CirCubeBlocks.BUDDING_NEODYMIUM_BLOCK).unlockedBy(ACItemRegistry.TELECORE::get).returns(2)
            .viaShaped(b -> b.define('C', ACItemRegistry.TELECORE.get())
                    .define('S', ACBlockRegistry.ENERGIZED_GALENA_SCARLET.get())
                    .define('E', ACBlockRegistry.ENERGIZED_GALENA_NEUTRAL.get())
                    .define('A', ACBlockRegistry.ENERGIZED_GALENA_AZURE.get())
                    .pattern("SEA")
                    .pattern("SCA")
                    .pattern("SEA"));

    private final Marker LOGISTICS = enterFolder("logistics");
    GeneratedRecipe

            REDSTONE_REQUESTER = create(AllBlocks.REDSTONE_REQUESTER).unlockedBy(I::cardboard).withNamespace("create")
            .viaShaped(b -> b.define('C', I.redstone())
                    .define('B', I.steel())
                    .define('A', I.stockLink())
                    .pattern("C")
                    .pattern("A")
                    .pattern("B")),

    PACKAGE_FROGPORT = create(AllBlocks.PACKAGE_FROGPORT).unlockedBy(I::cardboard).withNamespace("create")
            .viaShaped(b -> b.define('C', I.logisticsMechanism())
                    .define('B', Tags.Items.SLIMEBALLS)
                    .define('A', I.vault())
                    .pattern("B")
                    .pattern("A")
                    .pattern("C"));

    private final Marker MISC = enterFolder("misc");
    GeneratedRecipe

            D4 = create(CirCubeItems.D4).unlockedBy(() -> AllItems.COPPER_NUGGET)
            .viaShapeless(b -> b.requires(I.copperNugget())),
            D6 = create(CirCubeItems.D6).unlockedBy(() -> Items.GOLD_NUGGET)
                    .viaShapeless(b -> b.requires(Items.GOLD_NUGGET)),
            D8 = create(CirCubeItems.D8).unlockedBy(() -> Items.IRON_NUGGET)
                    .viaShapeless(b -> b.requires(Items.IRON_NUGGET)),
            D10 = create(CirCubeItems.D10).unlockedBy(() -> AllItems.ZINC_NUGGET)
                    .viaShapeless(b -> b.requires(I.zincNugget())),
            D12 = create(CirCubeItems.D12).unlockedBy(() -> CirCubeItems.STEEL_NUGGET)
                    .viaShapeless(b -> b.requires(I.steelNugget())),
            D20 = create(CirCubeItems.D20).unlockedBy(() -> AllItems.BRASS_NUGGET)
                    .viaShapeless(b -> b.requires(I.brassNugget()));

    public CirCubeStandardRecipeGen(PackOutput p_i48262_1_) {
        super(p_i48262_1_);
    }

    Marker enterFolder(String folder) {
        currentFolder = folder;
        return new Marker();
    }

    GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder(currentFolder, result);
    }

    GeneratedRecipeBuilder create(ResourceLocation result) {
        return new GeneratedRecipeBuilder(currentFolder, result);
    }

    GeneratedRecipeBuilder create(ItemProviderEntry<? extends ItemLike> result) {
        return create(result::get);
    }

    GeneratedRecipe createSpecial(Supplier<? extends SimpleCraftingRecipeSerializer<?>> serializer, String recipeType,
                                  String path) {
        ResourceLocation location = CirCube.asResource(recipeType + "/" + currentFolder + "/" + path);
        return register(consumer -> {
            SpecialRecipeBuilder b = SpecialRecipeBuilder.special(serializer.get());
            b.save(consumer, location.toString());
        });
    }

    GeneratedRecipe blastCrushedMetal(Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> ingredient) {
        return create(result::get).withSuffix("_from_crushed")
                .viaCooking(ingredient::get)
                .rewardXP(.1f)
                .inBlastFurnace();
    }

    GeneratedRecipe blastModdedCrushedMetal(ItemEntry<? extends Item> ingredient, CompatMetals metal) {
        for (Mods mod : metal.getMods()) {
            String metalName = metal.getName(mod);
            ResourceLocation ingot = mod.ingotOf(metalName);
            String modId = mod.getId();
            create(ingot).withSuffix("_compat_" + modId)
                    .whenModLoaded(modId)
                    .viaCooking(ingredient::get)
                    .rewardXP(.1f)
                    .inBlastFurnace();
        }
        return null;
    }

    GeneratedRecipe recycleGlass(BlockEntry<? extends Block> ingredient) {
        return create(() -> Blocks.GLASS).withSuffix("_from_" + ingredient.getId()
                        .getPath())
                .viaCooking(ingredient::get)
                .forDuration(50)
                .inFurnace();
    }

    GeneratedRecipe recycleGlassPane(BlockEntry<? extends Block> ingredient) {
        return create(() -> Blocks.GLASS_PANE).withSuffix("_from_" + ingredient.getId()
                        .getPath())
                .viaCooking(ingredient::get)
                .forDuration(50)
                .inFurnace();
    }

    GeneratedRecipe metalCompacting(List<ItemProviderEntry<? extends ItemLike>> variants,
                                    List<Supplier<TagKey<Item>>> ingredients) {
        GeneratedRecipe result = null;
        for (int i = 0; i + 1 < variants.size(); i++) {
            ItemProviderEntry<? extends ItemLike> currentEntry = variants.get(i);
            ItemProviderEntry<? extends ItemLike> nextEntry = variants.get(i + 1);
            Supplier<TagKey<Item>> currentIngredient = ingredients.get(i);
            Supplier<TagKey<Item>> nextIngredient = ingredients.get(i + 1);

            result = create(nextEntry).withSuffix("_from_compacting")
                    .unlockedBy(currentEntry::get)
                    .viaShaped(b -> b.pattern("###")
                            .pattern("###")
                            .pattern("###")
                            .define('#', currentIngredient.get()));

            result = create(currentEntry).returns(9)
                    .withSuffix("_from_decompacting")
                    .unlockedBy(nextEntry::get)
                    .viaShapeless(b -> b.requires(nextIngredient.get()));
        }
        return result;
    }

    GeneratedRecipe conversionCycle(List<ItemProviderEntry<? extends ItemLike>> cycle) {
        GeneratedRecipe result = null;
        for (int i = 0; i < cycle.size(); i++) {
            ItemProviderEntry<? extends ItemLike> currentEntry = cycle.get(i);
            ItemProviderEntry<? extends ItemLike> nextEntry = cycle.get((i + 1) % cycle.size());
            result = create(nextEntry).withSuffix("_from_conversion")
                    .unlockedBy(currentEntry::get)
                    .viaShapeless(b -> b.requires(currentEntry.get()));
        }
        return result;
    }

    GeneratedRecipe clearData(ItemProviderEntry<? extends ItemLike> item) {
        return create(item).withSuffix("_clear")
                .unlockedBy(item::get)
                .viaShapeless(b -> b.requires(item.get()));
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> p_200404_1_) {
        all.forEach(c -> c.register(p_200404_1_));
        CirCube.LOGGER.info(getName() + " registered " + all.size() + " recipe" + (all.size() == 1 ? "" : "s"));
    }

    protected GeneratedRecipe register(GeneratedRecipe recipe) {
        all.add(recipe);
        return recipe;
    }

    @Override
    public String getName() {
        return "Create's Standard Recipes";
    }

    static class Marker {
    }

    private record ModdedCookingRecipeResult(FinishedRecipe wrapped, ResourceLocation outputOverride,
                                             List<ICondition> conditions) implements FinishedRecipe {
        @Override
        public ResourceLocation getId() {
            return wrapped.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return wrapped.getType();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return wrapped.serializeAdvancement();
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return wrapped.getAdvancementId();
        }

        @Override
        public void serializeRecipeData(JsonObject object) {
            wrapped.serializeRecipeData(object);
            object.addProperty("result", outputOverride.toString());

            JsonArray conds = new JsonArray();
            conditions.forEach(c -> conds.add(CraftingHelper.serialize(c)));
            object.add("conditions", conds);
        }
    }

    private record ConditionSupportingShapelessRecipeResult(FinishedRecipe wrapped, List<ICondition> conditions)
            implements FinishedRecipe {
        @Override
        public ResourceLocation getId() {
            return wrapped.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return wrapped.getType();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return wrapped.serializeAdvancement();
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return wrapped.getAdvancementId();
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject pJson) {
            wrapped.serializeRecipeData(pJson);

            JsonArray conds = new JsonArray();
            conditions.forEach(c -> conds.add(CraftingHelper.serialize(c)));
            pJson.add("conditions", conds);
        }
    }

    class GeneratedRecipeBuilder {

        private final String path;
        List<ICondition> recipeConditions;
        private String suffix;
        private Supplier<? extends ItemLike> result;
        private ResourceLocation compatDatagenOutput;
        private String namespace;
        private boolean defaultPath;
        private Supplier<ItemPredicate> unlockedBy;
        private int amount;

        private GeneratedRecipeBuilder(String path) {
            this.path = path;
            this.recipeConditions = new ArrayList<>();
            this.suffix = "";
            this.amount = 1;
            this.namespace = MOD_ID;
            this.defaultPath = false;
        }

        public GeneratedRecipeBuilder(String path, Supplier<? extends ItemLike> result) {
            this(path);
            this.result = result;
        }

        public GeneratedRecipeBuilder(String path, ResourceLocation result) {
            this(path);
            this.compatDatagenOutput = result;
        }

        GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }

        GeneratedRecipeBuilder withNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        GeneratedRecipeBuilder withoutMarker() {
            this.defaultPath = true;
            return this;
        }

        GeneratedRecipeBuilder unlockedBy(Supplier<? extends ItemLike> item) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                    .of(item.get())
                    .build();
            return this;
        }

        GeneratedRecipeBuilder unlockedByTag(Supplier<TagKey<Item>> tag) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                    .of(tag.get())
                    .build();
            return this;
        }

        GeneratedRecipeBuilder whenModLoaded(String modid) {
            return withCondition(new ModLoadedCondition(modid));
        }

        GeneratedRecipeBuilder whenModMissing(String modid) {
            return withCondition(new NotCondition(new ModLoadedCondition(modid)));
        }

        GeneratedRecipeBuilder withCondition(ICondition condition) {
            recipeConditions.add(condition);
            return this;
        }

        GeneratedRecipeBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        GeneratedRecipe viaShaped(UnaryOperator<ShapedRecipeBuilder> builder) {
            return register(consumer -> {
                ShapedRecipeBuilder b =
                        builder.apply(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                ResourceLocation id = defaultPath ? defaultLocation() : createLocation("crafting");
                b.save(consumer, id);
            });
        }

        GeneratedRecipe viaShapeless(UnaryOperator<ShapelessRecipeBuilder> builder) {
            return register(consumer -> {
                ShapelessRecipeBuilder b =
                        builder.apply(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));

                b.save(result -> {
                    consumer.accept(!recipeConditions.isEmpty()
                            ? new ConditionSupportingShapelessRecipeResult(result, recipeConditions)
                            : result);
                }, createLocation("crafting"));
            });
        }

        GeneratedRecipe viaNetheriteSmithing(Supplier<? extends Item> base, Supplier<Ingredient> upgradeMaterial) {
            return register(consumer -> {
                SmithingTransformRecipeBuilder b =
                        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                                Ingredient.of(base.get()), upgradeMaterial.get(), RecipeCategory.COMBAT, result.get()
                                        .asItem());
                b.unlocks("has_item", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(base.get())
                        .build()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        private ResourceLocation createSimpleLocation(String recipeType) {
            if (namespace.equals(MOD_ID)) {
                return CirCube.asResource(recipeType + "/" + getRegistryName().getPath() + suffix);
            }
            return ResourceLocation.fromNamespaceAndPath(namespace, recipeType + "/" + getRegistryName().getPath() + suffix);
        }

        private ResourceLocation createLocation(String recipeType) {
            if (namespace.equals(MOD_ID)) {
                return CirCube.asResource(recipeType + "/" + path + "/" + getRegistryName().getPath() + suffix);
            }
            return ResourceLocation.fromNamespaceAndPath(namespace, recipeType + "/" + path + "/" + getRegistryName().getPath() + suffix);
        }

        private ResourceLocation defaultLocation() {
            if (namespace.equals(MOD_ID)) {
                return CirCube.asResource(getRegistryName().getPath() + suffix);
            }
            return ResourceLocation.fromNamespaceAndPath(namespace, getRegistryName().getPath() + suffix);
        }

        private ResourceLocation getRegistryName() {
            return compatDatagenOutput == null ? CatnipServices.REGISTRIES.getKeyOrThrow(result.get()
                    .asItem()) : compatDatagenOutput;
        }

        GeneratedCookingRecipeBuilder viaCooking(Supplier<? extends ItemLike> item) {
            return unlockedBy(item).viaCookingIngredient(() -> Ingredient.of(item.get()));
        }

        GeneratedCookingRecipeBuilder viaCookingTag(Supplier<TagKey<Item>> tag) {
            return unlockedByTag(tag).viaCookingIngredient(() -> Ingredient.of(tag.get()));
        }

        GeneratedCookingRecipeBuilder viaCookingIngredient(Supplier<Ingredient> ingredient) {
            return new GeneratedCookingRecipeBuilder(ingredient);
        }

        class GeneratedCookingRecipeBuilder {

            private final Supplier<Ingredient> ingredient;
            private final RecipeSerializer<? extends AbstractCookingRecipe> FURNACE = RecipeSerializer.SMELTING_RECIPE,
                    SMOKER = RecipeSerializer.SMOKING_RECIPE, BLAST = RecipeSerializer.BLASTING_RECIPE,
                    CAMPFIRE = RecipeSerializer.CAMPFIRE_COOKING_RECIPE;
            private float exp;
            private int cookingTime;

            GeneratedCookingRecipeBuilder(Supplier<Ingredient> ingredient) {
                this.ingredient = ingredient;
                cookingTime = 200;
                exp = 0;
            }

            GeneratedCookingRecipeBuilder forDuration(int duration) {
                cookingTime = duration;
                return this;
            }

            GeneratedCookingRecipeBuilder rewardXP(float xp) {
                exp = xp;
                return this;
            }

            GeneratedRecipe inFurnace() {
                return inFurnace(b -> b);
            }

            GeneratedRecipe inFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                return create(FURNACE, builder, 1);
            }

            GeneratedRecipe inSmoker() {
                return inSmoker(b -> b);
            }

            GeneratedRecipe inSmoker(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(FURNACE, builder, 1);
                create(CAMPFIRE, builder, 3);
                return create(SMOKER, builder, .5f);
            }

            GeneratedRecipe inBlastFurnace() {
                return inBlastFurnace(b -> b);
            }

            GeneratedRecipe inBlastFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(FURNACE, builder, 1);
                return create(BLAST, builder, .5f);
            }

            private GeneratedRecipe create(RecipeSerializer<? extends AbstractCookingRecipe> serializer,
                                           UnaryOperator<SimpleCookingRecipeBuilder> builder, float cookingTimeModifier) {
                return register(consumer -> {
                    boolean isOtherMod = compatDatagenOutput != null;

                    SimpleCookingRecipeBuilder b = builder.apply(SimpleCookingRecipeBuilder.generic(ingredient.get(),
                            RecipeCategory.MISC, isOtherMod ? Items.DIRT : result.get(), exp,
                            (int) (cookingTime * cookingTimeModifier), serializer));

                    if (unlockedBy != null)
                        b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));

                    b.save(result -> {
                        consumer.accept(
                                isOtherMod ? new ModdedCookingRecipeResult(result, compatDatagenOutput, recipeConditions)
                                        : result);
                    }, createSimpleLocation(CatnipServices.REGISTRIES.getKeyOrThrow(serializer)
                            .getPath()));
                });
            }
        }
    }
}
