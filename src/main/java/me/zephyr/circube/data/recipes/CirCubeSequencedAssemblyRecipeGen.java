package me.zephyr.circube.data.recipes;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import me.zephyr.circube.CirCube;
import me.zephyr.circube.CirCubeItems;
import net.lpcamors.optical.COMod;
import net.lpcamors.optical.COUtils;
import net.lpcamors.optical.items.COItems;
import net.lpcamors.optical.recipes.FocusingRecipe;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public class CirCubeSequencedAssemblyRecipeGen extends CirCubeRecipeProvider {
    GeneratedRecipe
            LOGISTICS_MECHANISM = create("logistics_mechanism", b -> b.require(I.andesiteAlloy())
            .transitionTo(CirCubeItems.INCOMPLETE_LOGISTICS_MECHANISM.get())
            .addOutput(CirCubeItems.LOGISTICS_MECHANISM.get(), 120)
            .addOutput(Items.REDSTONE, 8)
            .addOutput(AllItems.ELECTRON_TUBE.get(), 8)
            .addOutput(AllItems.ANDESITE_ALLOY.get(), 5)
            .addOutput(Items.GOLD_NUGGET, 3)
            .addOutput(AllItems.IRON_SHEET.get(), 2)
            .addOutput(AllItems.CRUSHED_GOLD.get(), 2)
            .addOutput(Items.QUARTZ, 1)
            .addOutput(Items.COMPASS, 1)
            .loops(5)
            .addStep(PressingRecipe::new, rb -> rb)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.cog()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllBlocks.LARGE_COGWHEEL.get()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllItems.ELECTRON_TUBE.get()))),

    INTEGRATED_CIRCUIT = create("integrated_circuit", b -> b.require(AllItems.STURDY_SHEET.get())
            .transitionTo(CirCubeItems.INCOMPLETE_INTEGRATED_CIRCUIT.get())
            .addOutput(CirCubeItems.INTEGRATED_CIRCUIT.get(), 100)
            .addOutput(Items.REDSTONE, 8)
            .addOutput(AllItems.ELECTRON_TUBE.get(), 8)
            .addOutput(AllItems.GOLDEN_SHEET.get(), 5)
            .addOutput(Items.GOLD_NUGGET, 3)
            .addOutput(CirCubeItems.REINFORCED_GOLDEN_SHEET.get(), 1)
            .addOutput(AllItems.CRUSHED_GOLD.get(), 3)
            .addOutput(Items.QUARTZ, 1)
            .addOutput(Items.COMPASS, 1)
            .loops(4)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.GOLD_NUGGET))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.GOLD_NUGGET))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.GOLD_NUGGET))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.GOLD_NUGGET))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.goldSheet()))
            .addStep(PressingRecipe::new, rb -> rb)),

    SEALED_MECHANISM = create("sealed_mechanism", b -> b.require(CirCubeItems.PLASTIC.get())
            .transitionTo(CirCubeItems.INCOMPLETE_SEALED_MECHANISM.get())
            .addOutput(CirCubeItems.SEALED_MECHANISM.get(), 125)
            .addOutput(Items.REDSTONE, 8)
            .addOutput(AllBlocks.SHAFT.get(), 8)
            .addOutput(AllItems.GOLDEN_SHEET.get(), 5)
            .addOutput(Items.GOLD_NUGGET, 3)
            .addOutput(AllItems.ANDESITE_ALLOY.get(), 2)
            .addOutput(AllItems.CRUSHED_GOLD.get(), 2)
            .addOutput(Items.QUARTZ, 1)
            .addOutput(Items.CLOCK, 1)
            .loops(3)
            .addStep(PressingRecipe::new, rb -> rb)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.cog()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllBlocks.LARGE_COGWHEEL.get()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(CirCubeItems.REINFORCED_GOLDEN_SHEET.get()))
            .addStep(PressingRecipe::new, rb -> rb)),

    NETHERITE_INGOT = create("netherite_ingot", b -> b.require(CirCubeItems.REINFORCED_GOLDEN_SHEET.get())
            .transitionTo(CirCubeItems.INCOMPLETE_NETHERITE_INGOT.get())
            .addOutput(Items.NETHERITE_INGOT, 75)
            .addOutput(AllItems.GOLDEN_SHEET.get(), 5)
            .addOutput(Items.GOLD_NUGGET, 15)
            .addOutput(AllItems.CRUSHED_GOLD.get(), 5)
            .loops(3)
            .addStep(FillingRecipe::new, rb -> rb.require(Fluids.LAVA, 500))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.NETHERITE_SCRAP))
            .addStep(PressingRecipe::new, rb -> rb)),

    STEEL = create("steel", b -> b.require(CirCubeItems.RAW_STEEL_INGOT)
            .transitionTo(CirCubeItems.INCOMPLETE_STEEL_INGOT.get())
            .addOutput(CirCubeItems.STEEL_INGOT, 90)
            .addOutput(AllItems.IRON_SHEET.get(), 3)
            .addOutput(Items.IRON_NUGGET, 5)
            .addOutput(AllItems.CRUSHED_IRON.get(), 2)
            .loops(7)
            .addStep(FillingRecipe::new, rb -> rb.require(ACFluidRegistry.ACID_FLUID_SOURCE.get(), 50))
            .addStep(PressingRecipe::new, rb -> rb)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.BLAZE_POWDER))
            .addStep(PressingRecipe::new, rb -> rb)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.BLAZE_POWDER))
            .addStep(PressingRecipe::new, rb -> rb)),

    STABILIZER = create("stabilizer", b -> b.require(Items.ENDER_PEARL)
            .transitionTo(CirCubeItems.INCOMPLETE_STABILIZER.get())
            .addOutput(CirCubeItems.STABILIZER, 85)
            .addOutput(Items.AMETHYST_SHARD, 10)
            .addOutput(Items.ENDER_EYE, 3)
            .addOutput(ACItemRegistry.PURE_DARKNESS.get(), 2)
            .loops(1)
            .addStep(FillingRecipe::new, rb -> rb.require(PotionFluidHandler.potionIngredient(Potions.REGENERATION, 200)))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.AMETHYST_SHARD))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(CirCubeItems.PURIFIED_DARKNESS.get()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.AMETHYST_SHARD))),

    OPTICAL_DEVICE = createCO("optical_device", b -> b.require(Items.AMETHYST_SHARD)
            .transitionTo(COItems.INCOMPLETE_OPTICAL_DEVICE)
            .addOutput(COItems.OPTICAL_DEVICE, 100)
            .loops(3)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(CirCubeItems.REINFORCED_STEEL_SHEET))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.GLASS_PANE))
            .addStep(FillingRecipe::new, rb -> rb.require(Fluids.WATER, 500))
            .addStep(PressingRecipe::new, rb -> rb)),

    OPTICAL_DEVICE_FOCUSING = createCO("optical_device_focusing", b -> b.require(CirCubeItems.REINFORCED_STEEL_SHEET)
            .transitionTo(COItems.INCOMPLETE_OPTICAL_DEVICE)
            .addOutput(COItems.OPTICAL_DEVICE, 100)
            .loops(1)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.AMETHYST_SHARD))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.steelSheet()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.GLASS_PANE))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.steelSheet()))
            .addStep(FocusingRecipe::gamma, p -> p)),

    COPPER_COIL = coil("copper", COItems.COPPER_COIL, 3),
            GOLDEN_COIL = coil("golden", COItems.GOLDEN_COIL, 6),
            ZINC_COIL = coil("zinc", COItems.ZINC_COIL, 4),
            ROSE_QUARTZ_CATALYST_COIL = coil("rose_quartz_catalyst", COItems.ROSE_QUARTZ_CATALYST_COIL, 3,
                    FocusingRecipe::gamma, rb -> rb),

    CAVE_TREASURE = createCracking("cave_box", b -> b.require(CirCubeItems.LOCKED_PIT_BOX)
            .transitionTo(CirCubeItems.INCOMPLETE_TREASURE_BOX)
            .addOutput(CirCubeItems.PIT_BOX, 100)
            .loops(12)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.key()))),

    WORKSHOP_TREASURE = createCracking("workshop_box", b -> b.require(CirCubeItems.LOCKED_WORKSHOP_BOX)
            .transitionTo(CirCubeItems.INCOMPLETE_TREASURE_BOX)
            .addOutput(CirCubeItems.WORKSHOP_BOX, 100)
            .loops(12)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.key())));

    public CirCubeSequencedAssemblyRecipeGen(PackOutput output) {
        super(output);
    }

    protected GeneratedRecipe create(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new SequencedAssemblyRecipeBuilder(CirCube.asResource(name)))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    protected GeneratedRecipe createCracking(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new SequencedAssemblyRecipeBuilder(CirCube.asResource(name + "_cracking")))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    protected GeneratedRecipe createCO(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new SequencedAssemblyRecipeBuilder(ResourceLocation.fromNamespaceAndPath(COMod.ID, name)))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    public <T extends ProcessingRecipe<?>> GeneratedRecipe coil(String name, ItemEntry<?> item, int loops) {
        return createCO(name + "_coil", b -> b.require(ACBlockRegistry.TESLA_BULB.get())
                .transitionTo(COUtils.EQ_INCOMPLETE.get(item))
                .addOutput(item, 100)
                .addOutput(COUtils.EQ_SHEETS.get(item), 10)
                .loops(loops)
                .addStep(DeployerApplicationRecipe::new, rb -> rb.require(COUtils.EQ_SHEETS.get(item)))
                .addStep(FillingRecipe::new, rb -> rb.require(Fluids.LAVA, 250))
                .addStep(PressingRecipe::new, rb -> rb));
    }

    public <T extends ProcessingRecipe<?>> GeneratedRecipe coil(String name, ItemEntry<?> item, int loops, ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, UnaryOperator<ProcessingRecipeBuilder<T>> builder) {
        return createCO(name + "_coil", b -> b.require(ACBlockRegistry.TESLA_BULB.get())
                .transitionTo(COUtils.EQ_INCOMPLETE.get(item))
                .addOutput(item, 100)
                .addOutput(COUtils.EQ_SHEETS.get(item), 10)
                .loops(loops)
                .addStep(DeployerApplicationRecipe::new, rb -> rb.require(COUtils.EQ_SHEETS.get(item)))
                .addStep(FillingRecipe::new, rb -> rb.require(Fluids.LAVA, 250))
                .addStep(PressingRecipe::new, rb -> rb)
                .addStep(factory, builder));
    }

    @Override
    public @NotNull String getName() {
        return "CirCube's Sequenced Assembly Recipes";
    }
}
