package me.zephyr.circube.event;

import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import me.zephyr.circube.CirCube;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import static me.zephyr.circube.CirCube.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class PasswordCrackingEvents {
    public static String PASSWORD;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void handleDeployRecipeSearching(DeployerRecipeSearchEvent event) {
        if (PASSWORD == null) return;

        RecipeWrapper inv = event.getInventory();
        if (inv.getContainerSize() != 2) return;

        Recipe<? extends Container> recipe = event.getRecipe();
        if (!(recipe instanceof DeployerApplicationRecipe deployerRecipe)) return;

        ResourceLocation id = recipe.getId();
        if (!id.getPath().contains("cracking")) return;

        Supplier<ItemStack> forcedResultSupplier = null;
        try {
            Field forcedResultField = ProcessingRecipe.class.getDeclaredField("forcedResult");
            forcedResultField.setAccessible(true);
            forcedResultSupplier = (Supplier<ItemStack>) forcedResultField.get(deployerRecipe);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }

        ItemStack result = forcedResultSupplier != null
                ? forcedResultSupplier.get()
                : deployerRecipe.getRollableResults().get(0).getStack();

        ItemStack victim = inv.getItem(0);
        ItemStack raper = inv.getItem(1);
        int step = getStep(victim);

        CirCube.LOGGER.info(String.valueOf(step));

        String expected = PASSWORD.substring(step, step + 1);
        String actual = getItemKey(raper);

        boolean wrong = false;
        CompoundTag victimNbt = victim.getTag();
        if (victimNbt != null && victimNbt.getBoolean("WrongRecipe")) {
            wrong = true;
        }
        if (!wrong && !actual.equals(expected)) {
            wrong = true;
        }

        ItemStack replaced = result.copy();
        CompoundTag outTag = replaced.getOrCreateTag();
        outTag.putBoolean("WrongRecipe", wrong);

        if (step == PASSWORD.length() - 1 && wrong) {
            replaced = getLockedItem(result);
        }

        ItemStack finalReplaced = replaced.copy();
        deployerRecipe.enforceNextResult(() -> finalReplaced);
    }

    private static int getStep(ItemStack input) {
        if (!input.hasTag()) return 0;
        CompoundTag tag = input.getTag();
        if (!tag.contains("SequencedAssembly")) return 0;
        return tag.getCompound("SequencedAssembly").getInt("Step");
    }

    private static String getItemKey(ItemStack stack) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (id == null) return "2";
        return switch (id.getPath()) {
            case "red_key", "magnetic_red_key" -> "1";
            case "blue_key", "magnetic_blue_key" -> "0";
            default -> "2";
        };
    }

    private static ItemStack getLockedItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return ItemStack.EMPTY;

        String path = "locked_" + BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        ResourceLocation newId = ResourceLocation.fromNamespaceAndPath(MOD_ID, path);

        Item item = BuiltInRegistries.ITEM.get(newId);
        return new ItemStack(item);
    }
}
