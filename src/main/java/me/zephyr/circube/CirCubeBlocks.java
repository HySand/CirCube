package me.zephyr.circube;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.simibubi.create.content.kinetics.transmission.SplitShaftInstance;
import com.simibubi.create.content.kinetics.transmission.SplitShaftRenderer;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import me.zephyr.circube.content.light.AndesiteLightBlock;
import me.zephyr.circube.content.light.AndesiteLightBlockEntity;
import me.zephyr.circube.content.light.BrassLightBlock;
import me.zephyr.circube.content.light.BrassLightBlockEntity;
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
            .transform(BlockStressDefaults.setImpact(1))
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

    public static final BlockEntry<AndesiteLightBlock> ANDESITE_LIGHT = REGISTRATE
            .block("andesite_light", AndesiteLightBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL)
                    .lightLevel(s -> s.getValue(AndesiteLightBlock.POWERED) ? 15 : 0))
            .transform(axeOrPickaxe())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> BlockStateGen.axisBlock(c, p, AssetLookup.forPowered(c, p)))
            .transform(BlockStressDefaults.setImpact(2))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntityEntry<AndesiteLightBlockEntity> ANDESITE_LIGHT_ENTITY = REGISTRATE
            .blockEntity("andesite_light", AndesiteLightBlockEntity::new)
            .instance(() -> SplitShaftInstance::new, false)
            .validBlocks(ANDESITE_LIGHT)
            .renderer(() -> SplitShaftRenderer::new)
            .register();

    public static final BlockEntry<BrassLightBlock> BRASS_LIGHT = REGISTRATE
            .block("brass_light", BrassLightBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noOcclusion().mapColor(MapColor.TERRACOTTA_BROWN)
                    .lightLevel(s -> s.getValue(BrassLightBlock.POWERED) ? 15 : 0))
            .transform(axeOrPickaxe())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> BlockStateGen.axisBlock(c, p, AssetLookup.forPowered(c, p)))
            .transform(BlockStressDefaults.setImpact(8))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntityEntry<BrassLightBlockEntity> BRASS_LIGHT_ENTITY = REGISTRATE
            .blockEntity("brass_light", BrassLightBlockEntity::new)
            .instance(() -> SplitShaftInstance::new, false)
            .validBlocks(BRASS_LIGHT)
            .renderer(() -> SplitShaftRenderer::new)
            .register();

    public static void register() {
    }
}
