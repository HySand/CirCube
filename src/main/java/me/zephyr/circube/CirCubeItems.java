package me.zephyr.circube;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.CombustibleItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import me.zephyr.circube.content.DiceItem;
import me.zephyr.circube.content.teleport.stabilizer.StabilizerItem;
import me.zephyr.circube.content.treasure.KnifeBoxItem;
import me.zephyr.circube.content.treasure.PitBoxItem;
import me.zephyr.circube.content.treasure.WorkshopBoxItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class CirCubeItems {
    private static final CreateRegistrate REGISTRATE = CirCube.getRegistrate();

    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_STABILIZER = REGISTRATE
            .item("incomplete_stabilizer", SequencedAssemblyItem::new)
            .register();
    public static final ItemEntry<StabilizerItem> STABILIZER = REGISTRATE
            .item("stabilizer", StabilizerItem::new)
            .register();
    public static final ItemEntry<Item> LIGHT_CHORUS_FRUIT = REGISTRATE
            .item("light_chorus_fruit", Item::new)
            .properties(p -> p.food(new FoodProperties.Builder().nutrition(4)
                    .saturationMod(0.3F)
                    .effect(() -> new MobEffectInstance(CirCubeEffects.PURE_LIGHT.get(), 20 * 300, 0), 1)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<Item> PURIFIED_DARKNESS = REGISTRATE
            .item("purified_darkness", Item::new)
            .register();
    public static final ItemEntry<DiceItem> D4 = REGISTRATE
            .item("four_sided_die", DiceItem::new)
            .register();
    public static final ItemEntry<DiceItem> D6 = REGISTRATE
            .item("six_sided_die", DiceItem::new)
            .register();
    public static final ItemEntry<DiceItem> D8 = REGISTRATE
            .item("eight_sided_die", DiceItem::new)
            .register();
    public static final ItemEntry<DiceItem> D10 = REGISTRATE
            .item("ten_sided_die", DiceItem::new)
            .register();
    public static final ItemEntry<DiceItem> D12 = REGISTRATE
            .item("twelve_sided_die", DiceItem::new)
            .register();
    public static final ItemEntry<DiceItem> D20 = REGISTRATE
            .item("twenty_sided_die", DiceItem::new)
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
    public static final ItemEntry<Item> PLASTIC = REGISTRATE
            .item("plastic", Item::new)
            .register();
    public static final ItemEntry<Item> RAW_STEEL_INGOT = REGISTRATE
            .item("raw_steel_ingot", Item::new)
            .register();
    public static final ItemEntry<Item> STEEL_INGOT = REGISTRATE
            .item("steel_ingot", Item::new)
            .tag(AllTags.forgeItemTag("ingots/steel"))
            .register();
    public static final ItemEntry<Item> STEEL_SHEET = REGISTRATE
            .item("steel_sheet", Item::new)
            .tag(AllTags.forgeItemTag("plates/steel"))
            .register();
    public static final ItemEntry<Item> STEEL_NUGGET = REGISTRATE
            .item("steel_nugget", Item::new)
            .tag(AllTags.forgeItemTag("nuggets/steel"))
            .register();
    public static final ItemEntry<CombustibleItem> GRAPHITE_POWDER = REGISTRATE
            .item("graphite_powder", CombustibleItem::new)
            .onRegister(i -> i.setBurnTime(600))
            .register();
    public static final ItemEntry<Item> RAW_PALE_NEODYMIUM = REGISTRATE
            .item("raw_pale_neodymium", Item::new)
            .register();


    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_STEEL_INGOT = REGISTRATE
            .item("incomplete_steel_ingot", SequencedAssemblyItem::new)
            .register();
    public static final ItemEntry<Item> REINFORCED_IRON_SHEET = REGISTRATE
            .item("reinforced_iron_sheet", Item::new)
            .register();
    public static final ItemEntry<Item> REINFORCED_GOLDEN_SHEET = REGISTRATE
            .item("reinforced_golden_sheet", Item::new)
            .register();
    public static final ItemEntry<Item> REINFORCED_COPPER_SHEET = REGISTRATE
            .item("reinforced_copper_sheet", Item::new)
            .register();
    public static final ItemEntry<Item> REINFORCED_BRASS_SHEET = REGISTRATE
            .item("reinforced_brass_sheet", Item::new)
            .register();
    public static final ItemEntry<Item> REINFORCED_STEEL_SHEET = REGISTRATE
            .item("reinforced_steel_sheet", Item::new)
            .register();

    public static final ItemEntry<Item> BRASS_SHELL = REGISTRATE
            .item("brass_shell", Item::new)
            .register();
    public static final ItemEntry<Item> STEEL_SHELL = REGISTRATE
            .item("steel_shell", Item::new)
            .register();
    public static final ItemEntry<Item> SHELL = REGISTRATE
            .item("shell", Item::new)
            .register();

    public static final ItemEntry<Item> RED_KEY = REGISTRATE
            .item("red_key", Item::new)
            .tag(AllTags.forgeItemTag("keys/simple"))
            .register();
    public static final ItemEntry<Item> BLUE_KEY = REGISTRATE
            .item("blue_key", Item::new)
            .tag(AllTags.forgeItemTag("keys/simple"))
            .register();
    public static final ItemEntry<Item> MAGNETIC_RED_KEY = REGISTRATE
            .item("magnetic_red_key", Item::new)
            .tag(AllTags.forgeItemTag("keys/magnetic"))
            .register();
    public static final ItemEntry<Item> MAGNETIC_BLUE_KEY = REGISTRATE
            .item("magnetic_blue_key", Item::new)
            .tag(AllTags.forgeItemTag("keys/magnetic"))
            .register();

    public static final ItemEntry<Item> LOCKED_PIT_BOX = locked_box("pit", "wood"),
            LOCKED_WORKSHOP_BOX = locked_box("workshop", "wood"),
            LOCKED_KNIFE_BOX = locked_box("knife", "steel");

    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_PIT_BOX = incompleteBox("pit", "wood"),
            INCOMPLETE_WORKSHOP_BOX = incompleteBox("workshop", "wood"),
            INCOMPLETE_KNIFE_BOX = incompleteBox("knife", "steel");

    public static final ItemEntry<PitBoxItem> PIT_BOX = box("pit", "wood", PitBoxItem::new);
    public static final ItemEntry<WorkshopBoxItem> WORKSHOP_BOX = box("workshop", "wood", WorkshopBoxItem::new);
    public static final ItemEntry<KnifeBoxItem> KNIFE_BOX = box("knife", "steel", KnifeBoxItem::new);

    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_SMALL_AMMO = REGISTRATE
            .item("incomplete_small_ammo", SequencedAssemblyItem::new)
            .register();
    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_LARGE_AMMO = REGISTRATE
            .item("incomplete_large_ammo", SequencedAssemblyItem::new)
            .register();
    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_MICRO_AMMO = REGISTRATE
            .item("incomplete_micro_ammo", SequencedAssemblyItem::new)
            .register();
    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_SHOTGUN_AMMO = REGISTRATE
            .item("incomplete_shotgun_ammo", SequencedAssemblyItem::new)
            .register();
    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_MASSIVE_AMMO = REGISTRATE
            .item("incomplete_massive_ammo", SequencedAssemblyItem::new)
            .register();
    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_SPECIAL_AMMO = REGISTRATE
            .item("incomplete_special_ammo", SequencedAssemblyItem::new)
            .register();

    private static ItemEntry<Item> locked_box(String name, String type) {
        return REGISTRATE.item("locked_" + name + "_box", Item::new)
                .model((ctx, prov) -> prov
                        .withExistingParent(ctx.getName(), prov.mcLoc("item/generated"))
                        .texture("layer0", prov.modLoc("item/locked_" + type + "_box")))
                .register();
    }

    private static <T extends Item> ItemEntry<T> box(String name, String type, NonNullFunction<Item.Properties, T> factory) {
        return REGISTRATE.item(name + "_box", factory)
                .model((ctx, prov) -> prov
                        .withExistingParent(ctx.getName(), prov.mcLoc("item/generated"))
                        .texture("layer0", prov.modLoc("item/" + type + "_box")))
                .register();
    }

    private static ItemEntry<SequencedAssemblyItem> incompleteBox(String name, String type) {
        return REGISTRATE.item("incomplete_" + name + "_box", SequencedAssemblyItem::new)
                .model((ctx, prov) -> prov
                        .withExistingParent(ctx.getName(), prov.mcLoc("item/generated"))
                        .texture("layer0", prov.modLoc("item/incomplete_" + type + "_box")))
                .register();
    }

    public static void register() {
    }
}
