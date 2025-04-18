package me.zephyr.circube;


import com.simibubi.create.content.kinetics.transmission.SplitShaftRenderer;
import com.simibubi.create.content.kinetics.transmission.SplitShaftVisual;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import me.zephyr.circube.config.CStress;
import me.zephyr.circube.content.beacon.MechanicalBeaconBlock;
import me.zephyr.circube.content.beacon.MechanicalBeaconBlockEntity;
import me.zephyr.circube.content.beacon.MechanicalBeaconRenderer;
import me.zephyr.circube.content.beacon.MechanicalBeaconVisual;
import me.zephyr.circube.content.light.AndesiteLightBlock;
import me.zephyr.circube.content.light.AndesiteLightBlockEntity;
import me.zephyr.circube.content.light.BrassLightBlock;
import me.zephyr.circube.content.light.BrassLightBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOnly;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;


@SuppressWarnings("removal")
public class CirCubeBlocks {
    private static final CreateRegistrate REGISTRATE = CirCube.getRegistrate();

    public static final BlockEntry<AndesiteLightBlock> ANDESITE_LIGHT = REGISTRATE
            .block("andesite_light", AndesiteLightBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL)
                    .lightLevel(s -> s.getValue(AndesiteLightBlock.POWERED) ? 15 : 0))
            .transform(axeOrPickaxe())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> BlockStateGen.axisBlock(c, p, AssetLookup.forPowered(c, p)))
            .transform(CStress.setImpact(2.0))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntityEntry<AndesiteLightBlockEntity> ANDESITE_LIGHT_ENTITY = REGISTRATE
            .blockEntity("andesite_light", AndesiteLightBlockEntity::new)
            .visual(() -> SplitShaftVisual::new, false)
            .validBlocks(ANDESITE_LIGHT)
            .renderer(() -> SplitShaftRenderer::new)
            .register();

    public static final BlockEntry<BrassLightBlock> BRASS_LIGHT = REGISTRATE
            .block("brass_light", BrassLightBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noOcclusion().mapColor(MapColor.TERRACOTTA_BROWN)
                    .lightLevel(s -> s.getValue(BrassLightBlock.POWERED) ? 15 : 0))
            .transform(axeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> BlockStateGen.axisBlock(c, p, AssetLookup.forPowered(c, p)))
            .transform(CStress.setImpact(8.0))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntityEntry<BrassLightBlockEntity> BRASS_LIGHT_ENTITY = REGISTRATE
            .blockEntity("brass_light", BrassLightBlockEntity::new)
            .visual(() -> SplitShaftVisual::new, false)
            .validBlocks(BRASS_LIGHT)
            .renderer(() -> SplitShaftRenderer::new)
            .register();

    public static final BlockEntry<MechanicalBeaconBlock> ANDESITE_BEACON = REGISTRATE
            .block("andesite_beacon", MechanicalBeaconBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL))
            .transform(axeOrPickaxe())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> {
                ModelFile bottom_off = AssetLookup.partialBaseModel(c, p, "bottom_off");
                ModelFile bottom_on = AssetLookup.partialBaseModel(c, p, "bottom_on");
                ModelFile top = AssetLookup.partialBaseModel(c, p, "top");
                p.getVariantBuilder(c.get()).forAllStatesExcept((state) -> {
                    boolean lower = state.getValue(MechanicalBeaconBlock.HALF) == DoubleBlockHalf.LOWER;
                    boolean active = state.getValue(MechanicalBeaconBlock.ACTIVE);
                    ModelFile model = null;
                    if (lower) {
                        if (active) {
                            model = bottom_on;
                        } else {
                            model = bottom_off;
                        }
                    } else {
                        model = top;
                    }
                    return ConfiguredModel.builder().modelFile(model).build();
                });
            })
            .transform(CStress.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<MechanicalBeaconBlock> BRASS_BEACON = REGISTRATE
            .block("brass_beacon", MechanicalBeaconBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL))
            .transform(axeOrPickaxe())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> {
                ModelFile bottom_off = AssetLookup.partialBaseModel(c, p, "bottom_off");
                ModelFile bottom_on = AssetLookup.partialBaseModel(c, p, "bottom_on");
                ModelFile top = AssetLookup.partialBaseModel(c, p, "top");
                p.getVariantBuilder(c.get()).forAllStatesExcept((state) -> {
                    boolean lower = state.getValue(MechanicalBeaconBlock.HALF) == DoubleBlockHalf.LOWER;
                    boolean active = state.getValue(MechanicalBeaconBlock.ACTIVE);
                    ModelFile model = null;
                    if (lower) {
                        if (active) {
                            model = bottom_on;
                        } else {
                            model = bottom_off;
                        }
                    } else {
                        model = top;
                    }
                    return ConfiguredModel.builder().modelFile(model).build();
                });
            })
            .transform(CStress.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntityEntry<MechanicalBeaconBlockEntity> MECHANICAL_BEACON_ENTITY = REGISTRATE
            .blockEntity("mechanical_beacon", MechanicalBeaconBlockEntity::new)
            .visual(() -> MechanicalBeaconVisual::new, true)
            .validBlocks(ANDESITE_BEACON, BRASS_BEACON)
            .renderer(() -> MechanicalBeaconRenderer::new)
            .register();

    public static void register() {
    }
}
