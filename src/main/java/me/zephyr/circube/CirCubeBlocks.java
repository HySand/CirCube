package me.zephyr.circube;


import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.simibubi.create.content.kinetics.transmission.SplitShaftRenderer;
import com.simibubi.create.content.kinetics.transmission.SplitShaftVisual;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import me.zephyr.circube.config.CStress;
import me.zephyr.circube.content.ShadowBlock;
import me.zephyr.circube.content.geyser.GeyserBlock;
import me.zephyr.circube.content.light.MechanicalLightBlock;
import me.zephyr.circube.content.light.MechanicalLightBlockEntity;
import me.zephyr.circube.content.neodymium.BuddingNeodymiumBlock;
import me.zephyr.circube.content.neodymium.PaleNeodymiumNodeBlock;
import me.zephyr.circube.content.teleport.beacon.MechanicalBeaconBlock;
import me.zephyr.circube.content.teleport.beacon.MechanicalBeaconBlockEntity;
import me.zephyr.circube.content.teleport.beacon.MechanicalBeaconRenderer;
import me.zephyr.circube.content.teleport.beacon.MechanicalBeaconVisual;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.Tags;

import static com.simibubi.create.foundation.data.BlockStateGen.simpleCubeAll;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.*;


@SuppressWarnings("removal")
public class CirCubeBlocks {
    private static final CreateRegistrate REGISTRATE = CirCube.getRegistrate();

    public static final BlockEntry<MechanicalLightBlock> ANDESITE_LIGHT = REGISTRATE
            .block("andesite_light", MechanicalLightBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL)
                    .lightLevel(s -> s.getValue(MechanicalLightBlock.POWERED) ? 15 : 0))
            .transform(axeOrPickaxe())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> BlockStateGen.axisBlock(c, p, AssetLookup.forPowered(c, p)))
            .transform(CStress.setNoImpact())
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<MechanicalLightBlock> BRASS_LIGHT = REGISTRATE
            .block("brass_light", MechanicalLightBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noOcclusion().mapColor(MapColor.TERRACOTTA_BROWN)
                    .lightLevel(s -> s.getValue(MechanicalLightBlock.POWERED) ? 15 : 0))
            .transform(axeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> BlockStateGen.axisBlock(c, p, AssetLookup.forPowered(c, p)))
            .transform(CStress.setNoImpact())
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntityEntry<MechanicalLightBlockEntity> LIGHT_ENTITY = REGISTRATE
            .blockEntity("mechanical_light", MechanicalLightBlockEntity::new)
            .visual(() -> SplitShaftVisual::new, false)
            .validBlocks(ANDESITE_LIGHT, BRASS_LIGHT)
            .renderer(() -> SplitShaftRenderer::new)
            .register();

    public static final BlockEntry<MechanicalBeaconBlock> ANDESITE_BEACON = REGISTRATE
            .block("andesite_beacon", MechanicalBeaconBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL)
                    .lightLevel(s -> s.getValue(MechanicalBeaconBlock.ACTIVE) ? 5 : 0))
            .transform(axeOrPickaxe())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> {
                ModelFile bottom_off = AssetLookup.partialBaseModel(c, p, "bottom_off");
                ModelFile bottom_on = AssetLookup.partialBaseModel(c, p, "bottom_on");
                ModelFile top = AssetLookup.partialBaseModel(c, p, "top");
                p.getVariantBuilder(c.get()).forAllStatesExcept((state) -> {
                    boolean lower = state.getValue(MechanicalBeaconBlock.HALF) == DoubleBlockHalf.LOWER;
                    boolean active = state.getValue(MechanicalBeaconBlock.ACTIVE);
                    ModelFile model;
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
            .properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL)
                    .lightLevel(s -> s.getValue(MechanicalBeaconBlock.ACTIVE) ? 5 : 0))
            .transform(axeOrPickaxe())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> {
                ModelFile bottom_off = AssetLookup.partialBaseModel(c, p, "bottom_off");
                ModelFile bottom_on = AssetLookup.partialBaseModel(c, p, "bottom_on");
                ModelFile top = AssetLookup.partialBaseModel(c, p, "top");
                p.getVariantBuilder(c.get()).forAllStatesExcept((state) -> {
                    boolean lower = state.getValue(MechanicalBeaconBlock.HALF) == DoubleBlockHalf.LOWER;
                    boolean active = state.getValue(MechanicalBeaconBlock.ACTIVE);
                    ModelFile model;
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

    public static final BlockEntry<ShadowBlock> SHADOW = REGISTRATE
            .block("shadow", ShadowBlock::new)
            .initialProperties(() -> Blocks.BARRIER)
            .properties(p -> p
                    .noOcclusion()
                    .isSuffocating((state, level, pos) -> false)
                    .noLootTable()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(-1.0F, Float.MAX_VALUE))
            .blockstate(simpleCubeAll("shadow"))
            .simpleItem()
            .register();

    public static final BlockEntry<GeyserBlock> GEYSER = REGISTRATE
            .block("geyser", GeyserBlock::new)
            .initialProperties(() -> Blocks.DEEPSLATE)
            .properties(p -> p
                    .noLootTable()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(100, 1200))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<Block> STEEL_BLOCK = REGISTRATE.block("steel_block", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_CYAN)
                    .requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(simpleCubeAll("steel_block"))
            .tag(BlockTags.NEEDS_DIAMOND_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .transform(tagBlockAndItem("storage_blocks/steel"))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .lang("Block of Steel")
            .register();

    public static final BlockEntry<BuddingNeodymiumBlock> BUDDING_NEODYMIUM_BLOCK = REGISTRATE
            .block("budding_neodymium", BuddingNeodymiumBlock::new)
            .initialProperties(ACBlockRegistry.GALENA::get)
            .properties(p -> p.randomTicks().noLootTable())
            .blockstate((c, p) -> BlockStateGen.directionalBlockIgnoresWaterlogged(c, p, s -> AssetLookup.partialBaseModel(c, p)))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<PaleNeodymiumNodeBlock> PALE_NEODYMIUM_NODE = REGISTRATE
            .block("pale_neodymium_node", props -> new PaleNeodymiumNodeBlock(false))
            .initialProperties(ACBlockRegistry.AZURE_NEODYMIUM_NODE::get)
            .blockstate((c, p) -> {
                ModelFile[] models = new ModelFile[]{
                        p.models().getExistingFile(p.modLoc("block/pale_neodymium_node/block_0")),
                        p.models().getExistingFile(p.modLoc("block/pale_neodymium_node/block_1")),
                        p.models().getExistingFile(p.modLoc("block/pale_neodymium_node/block_2"))
                };

                VariantBlockStateBuilder builder = p.getVariantBuilder(c.get());

                for (Direction dir : Direction.values()) {
                    int xRot = 0, yRot = 0;
                    switch (dir) {
                        case DOWN -> xRot = 180;
                        case UP -> xRot = 0;
                        case NORTH -> xRot = 90;
                        case SOUTH -> {
                            xRot = 90;
                            yRot = 180;
                        }
                        case WEST -> {
                            xRot = 90;
                            yRot = 270;
                        }
                        case EAST -> {
                            xRot = 90;
                            yRot = 90;
                        }
                    }

                    for (ModelFile model : models) {
                        builder.partialState().with(BlockStateProperties.FACING, dir)
                                .addModels(ConfiguredModel.builder()
                                        .modelFile(model)
                                        .rotationX(xRot)
                                        .rotationY(yRot)
                                        .build());
                    }
                }
            })

            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            lt.applyExplosionDecay(b, LootItem.lootTableItem(CirCubeItems.RAW_PALE_NEODYMIUM.get())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 5.0f)))
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
            .item()
            .transform(customItemModel())
            .register();

    public static void register() {
    }
}
