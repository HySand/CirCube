package me.zephyr.circube;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static me.zephyr.circube.CirCube.MOD_ID;
import static me.zephyr.circube.CirCubeItems.KINETIC_MECHANISM;

public class CirCubeCreativeTabs {
    private static final CreateRegistrate REGISTRATE = CirCube.getRegistrate();

    private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final RegistryObject<CreativeModeTab> TAB =
            REGISTER.register("circube",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.circube"))
                            .withTabsBefore(ResourceLocation.bySeparator("create:palettes", ':'))
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
}
