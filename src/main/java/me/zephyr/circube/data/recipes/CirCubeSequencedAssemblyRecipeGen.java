package me.zephyr.circube.data.recipes;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import me.zephyr.circube.CirCube;
import me.zephyr.circube.CirCubeItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public class CirCubeSequencedAssemblyRecipeGen extends CirCubeRecipeProvider {
    GeneratedRecipe
            KINETIC_MECHANISM = create("kinetic_mechanism", b -> b.require(I.copperSheet())
            .transitionTo(CirCubeItems.INCOMPLETE_KINETIC_MECHANISM.get())
            .addOutput(CirCubeItems.KINETIC_MECHANISM.get(), 120)
            .addOutput(Items.REDSTONE, 8)
            .addOutput(AllItems.ELECTRON_TUBE.get(), 8)
            .addOutput(AllItems.GOLDEN_SHEET.get(), 5)
            .addOutput(Items.GOLD_NUGGET, 3)
            .addOutput(AllItems.IRON_SHEET.get(), 2)
            .addOutput(AllItems.CRUSHED_GOLD.get(), 2)
            .addOutput(Items.QUARTZ, 1)
            .addOutput(Items.COMPASS, 1)
            .loops(3)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.cog()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.zincNugget()))
            .addStep(PressingRecipe::new, rb -> rb)),
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
