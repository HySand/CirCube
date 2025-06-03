package me.zephyr.circube;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import me.zephyr.circube.content.teleport.beacon.MechanicalBeaconMenu;
import me.zephyr.circube.content.teleport.beacon.MechanicalBeaconScreen;
import me.zephyr.circube.content.teleport.stabilizer.StabilizerMenu;
import me.zephyr.circube.content.teleport.stabilizer.StabilizerScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class CirCubeMenuTypes {
    private static final CreateRegistrate REGISTRATE = CirCube.getRegistrate();

    public static final MenuEntry<MechanicalBeaconMenu> MECHANICAL_BEACON_MENU =
            register("beacon_manage", MechanicalBeaconMenu::new, () -> MechanicalBeaconScreen::new);
    public static final MenuEntry<StabilizerMenu> STABILIZER_MENU =
            register("stabilizer_selection", StabilizerMenu::new, () -> StabilizerScreen::new);

    private static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> MenuEntry<C> register(String name, MenuBuilder.ForgeMenuFactory<C> factory, NonNullSupplier<MenuBuilder.ScreenFactory<C, S>> screenFactory) {
        return REGISTRATE
                .menu(name, factory, screenFactory)
                .register();
    }

    public static void register() {
    }
}
