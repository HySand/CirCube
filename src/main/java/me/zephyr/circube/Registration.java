package me.zephyr.circube;


import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderStoryBoardEntry;
import com.simibubi.create.infrastructure.ponder.AllPonderTags;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import me.zephyr.circube.spring.SpringBlock;
import me.zephyr.circube.spring.SpringBlockEntity;
import me.zephyr.circube.spring.SpringScenes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static me.zephyr.circube.Circube.MODID;

@SuppressWarnings("removal")
public class Registration {
    private static final CreateRegistrate REGISTRATE = Circube.getRegistrate();
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(MODID);

    public static final BlockEntry<SpringBlock> SPRING = REGISTRATE
            .block("spring", SpringBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.noOcclusion().mapColor(MapColor.PODZOL))
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(BlockStressDefaults.setCapacity(32))
            .transform(BlockStressDefaults.setGeneratorSpeed(SpringBlock::getSpeedRange))
            .transform(axeOrPickaxe())
            .blockstate((c, p) -> BlockStateGen.directionalBlockIgnoresWaterlogged(c, p, s -> AssetLookup.partialBaseModel(c, p)))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntityEntry<SpringBlockEntity> SPRING_ENTITY = REGISTRATE
            .blockEntity("spring", SpringBlockEntity::new)
            .instance(() -> ShaftInstance::new, false)
            .validBlocks(SPRING)
            .renderer(() -> ShaftRenderer::new)
            .register();


    public static final ItemEntry<Item> STABILIZER = REGISTRATE
            .item("stabilizer", Item::new)
            .register();
    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_NETHERITE_INGOT = REGISTRATE
            .item("incomplete_netherite_ingot", SequencedAssemblyItem::new)
            .register();
    public static final ItemEntry<Item> KINETIC_MECHANISM = REGISTRATE
            .item("kinetic_mechanism", Item::new)
            .register();
    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_KINETIC_MECHANISM = REGISTRATE
            .item("incomplete_kinetic_mechanism", SequencedAssemblyItem::new)
            .register();
    public static final ItemEntry<Item> LOGISTICS_MECHANISM = REGISTRATE
            .item("logistics_mechanism", Item::new)
            .register();
    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_LOGISTICS_MECHANISM = REGISTRATE
            .item("incomplete_logistics_mechanism", SequencedAssemblyItem::new)
            .register();
    public static final ItemEntry<Item> SEALED_MECHANISM = REGISTRATE
            .item("sealed_mechanism", Item::new)
            .register();
    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_SEALED_MECHANISM = REGISTRATE
            .item("incomplete_sealed_mechanism", SequencedAssemblyItem::new)
            .register();
    public static final ItemEntry<Item> INTEGRATED_CIRCUIT = REGISTRATE
            .item("integrated_circuit", Item::new)
            .register();
    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_INTEGRATED_CIRCUIT = REGISTRATE
            .item("incomplete_integrated_circuit", SequencedAssemblyItem::new)
            .register();


    private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final RegistryObject<CreativeModeTab> TAB =
            REGISTER.register("circube",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.circube"))
                            .withTabsBefore(ResourceLocation.of("create:palettes", ':'))
                            .icon(() -> KINETIC_MECHANISM.get().asItem().getDefaultInstance())
                            .displayItems(
                                    (parameters, output) ->
                                            output.acceptAll(
                                                    REGISTRATE.getAll(Registries.ITEM).stream().filter(
                                                            itemRegistryEntry -> !(itemRegistryEntry.get() instanceof SequencedAssemblyItem)
                                                    ).map(
                                                            regObj -> new ItemStack(regObj.get())
                                                    ).toList()
                                            )
                            )
                            .build());

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }

    public static void register() {
        HELPER.forComponents(SPRING).addStoryBoard("spring", SpringScenes::Spring, AllPonderTags.KINETIC_SOURCES);
        PonderRegistry.TAGS.forTag(AllPonderTags.KINETIC_SOURCES).add(SPRING);
    }
}
