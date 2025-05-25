package me.zephyr.circube;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.CombustibleItem;
import com.tterrag.registrate.builders.MenuBuilder.ForgeMenuFactory;
import com.tterrag.registrate.builders.MenuBuilder.ScreenFactory;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import me.zephyr.circube.content.dice.DiceItem;
import me.zephyr.circube.content.stabilizer.StabilizerItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;

public class CirCubeItems {
    private static final CreateRegistrate REGISTRATE = CirCube.getRegistrate();

    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_STABILIZER = REGISTRATE
            .item("incomplete_stabilizer", SequencedAssemblyItem::new)
            .register();
    public static final ItemEntry<StabilizerItem> STABILIZER = REGISTRATE
            .item("stabilizer", StabilizerItem::new)
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

    private static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> MenuEntry<C> register(String name, ForgeMenuFactory<C> factory, NonNullSupplier<ScreenFactory<C, S>> screenFactory) {
        return REGISTRATE
                .menu(name, factory, screenFactory)
                .register();
    }

    public static void register() {
    }
}
