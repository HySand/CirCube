package me.zephyr.circube;

import com.simibubi.create.AllFluids;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.Color;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import me.zephyr.circube.content.whitewash.WhitewashLiquidType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class CirCubeFluids {
    private static final CreateRegistrate REGISTRATE = CirCube.getRegistrate();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> LIME_WATER =
            REGISTRATE.fluid("whitewash",
                            new ResourceLocation("circube:block/whitewash_still"),
                            new ResourceLocation("circube:block/whitewash_flow"),
                            WhitewashLiquidType.create(0x333333, () -> 1f / 32f ))
                    .lang("Whitewash")
                    .properties(b -> b.viscosity(1000)
                            .density(1000))
                    .fluidProperties(p -> p.levelDecreasePerBlock(1)
                            .tickRate(5)
                            .slopeFindDistance(4)
                            .explosionResistance(100f))
                    .register();

    public static void register() {
    }
}
