package me.zephyr.circube.data.recipes;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import me.zephyr.circube.CirCube;
import me.zephyr.circube.CirCubeFluids;
import me.zephyr.circube.CirCubeItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
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
                    .addOutput(CirCubeItems.INTEGRATED_CIRCUIT.get(), 120)
                    .addOutput(Items.REDSTONE, 8)
                    .addOutput(AllItems.ELECTRON_TUBE.get(), 8)
                    .addOutput(AllItems.GOLDEN_SHEET.get(), 5)
                    .addOutput(Items.GOLD_NUGGET, 3)
                    .addOutput(AllItems.IRON_SHEET.get(), 2)
                    .addOutput(AllItems.CRUSHED_GOLD.get(), 2)
                    .addOutput(Items.QUARTZ, 1)
                    .addOutput(Items.COMPASS, 1)
                    .loops(4)
                    .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.GOLD_NUGGET))
                    .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.GOLD_NUGGET))
                    .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.GOLD_NUGGET))
                    .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.GOLD_NUGGET))
                    .addStep(PressingRecipe::new, rb -> rb)
                    .addStep(FillingRecipe::new, rb -> rb.require(CirCubeFluids.MOLTEN_GOLD.get(), 100))),
            SEALED_MECHANISM = create("sealed_mechanism", b -> b.require(CirCubeItems.PLASTIC.get())
                    .transitionTo(CirCubeItems.INCOMPLETE_SEALED_MECHANISM.get())
                    .addOutput(CirCubeItems.SEALED_MECHANISM.get(), 120)
                    .addOutput(Items.REDSTONE, 8)
                    .addOutput(AllItems.ELECTRON_TUBE.get(), 8)
                    .addOutput(AllItems.GOLDEN_SHEET.get(), 5)
                    .addOutput(Items.GOLD_NUGGET, 3)
                    .addOutput(AllItems.IRON_SHEET.get(), 2)
                    .addOutput(AllItems.CRUSHED_GOLD.get(), 2)
                    .addOutput(Items.QUARTZ, 1)
                    .addOutput(Items.CLOCK, 1)
                    .loops(3)
                    .addStep(PressingRecipe::new, rb -> rb)
                    .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.cog()))
                    .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllBlocks.LARGE_COGWHEEL.get()))
                    .addStep(DeployerApplicationRecipe::new, rb -> rb.require(CirCubeItems.REINFORCED_GOLDEN_SHEET.get()))
                    .addStep(PressingRecipe::new, rb -> rb)
                    .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllItems.ELECTRON_TUBE.get()))),
            NETHERITE_INGOT = create("netherite_ingot", b -> b.require(I.gold())
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
                    .addOutput(CirCubeItems.STEEL_INGOT, 75)
                    .addOutput(AllItems.GOLDEN_SHEET.get(), 5)
                    .addOutput(Items.GOLD_NUGGET, 15)
                    .addOutput(AllItems.CRUSHED_GOLD.get(), 5)
                    .loops(7)
                    .addStep(FillingRecipe::new, rb -> rb.require(Fluids.LAVA, 500))
                    .addStep(PressingRecipe::new, rb -> rb)
                    .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.BLAZE_POWDER))
                    .addStep(PressingRecipe::new, rb -> rb)
                    .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.BLAZE_POWDER))
                    .addStep(PressingRecipe::new, rb -> rb));
    ;


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

    @Override
    public @NotNull String getName() {
        return "CirCube's Sequenced Assembly Recipes";
    }
}
