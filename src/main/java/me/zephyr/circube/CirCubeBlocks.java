package me.zephyr.circube;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.FluidEntry;
import me.zephyr.circube.content.spring.SpringBlock;
import me.zephyr.circube.content.spring.SpringBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;


@SuppressWarnings("removal")
public class CirCubeBlocks {
    private static final CreateRegistrate REGISTRATE = CirCube.getRegistrate();


    public static final BlockEntry<SpringBlock> SPRING = REGISTRATE
            .block("spring", SpringBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL))
            .transform(axeOrPickaxe())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> BlockStateGen.directionalBlockIgnoresWaterlogged(c, p, s -> AssetLookup.partialBaseModel(c, p)))
            .transform(BlockStressDefaults.setImpact(2))
            .transform(BlockStressDefaults.setCapacity(32))
            .transform(BlockStressDefaults.setGeneratorSpeed(SpringBlock::getSpeedRange))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntityEntry<SpringBlockEntity> SPRING_ENTITY = REGISTRATE
            .blockEntity("spring", SpringBlockEntity::new)
            .instance(() -> ShaftInstance::new, false)
            .validBlocks(SPRING)
            .renderer(() -> ShaftRenderer::new)
            .register();

    public static void register() {
    }
}
