package me.zephyr.circube.event;

import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import static me.zephyr.circube.CirCube.MOD_ID;

public class PasswordCrackingEvents {
    public static String PASSWORD;

    @Mod.EventBusSubscriber(modid = MOD_ID)
    public class PasswordCracking {
        @SubscribeEvent
        public static void onAssemblyTreasure(PasswordCrackingEvent event) {
            if (PASSWORD == null) return;

            ItemStack victim = event.getVictim();
            ItemStack raper = event.getRaper();
            int step = event.getStep();
            ItemStack result = event.getRecipeResult();
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

            event.setReplaced(replaced);
        }
    }

    private static ItemStack getLockedItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return ItemStack.EMPTY;

        String path = "locked_" + BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        ResourceLocation newId = ResourceLocation.fromNamespaceAndPath(MOD_ID, path);

        Item item = BuiltInRegistries.ITEM.get(newId);
        return new ItemStack(item);
    }

    private static String getItemKey(ItemStack stack) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (id == null) return "2";
        return switch (id.getPath()) {
            case "red_key" -> "1";
            case "blue_key" -> "0";
            default -> "2";
        };
    }

    public static class PasswordCrackingEvent extends Event {
        ItemStack victim;
        ItemStack raper;
        ItemStack result;
        ItemStack replaced;
        int step;

        public ItemStack getVictim() {
            return this.victim;
        }

        public ItemStack getRaper() {
            return this.raper;
        }

        public ItemStack getRecipeResult() {
            return this.result;
        }

        public int getStep() {
            return this.step;
        }

        public ItemStack getFinalResult() {
            return this.replaced;
        }

        public void setReplaced(ItemStack other) {
            this.replaced = other;
        }

        public PasswordCrackingEvent(ItemStack victim, ItemStack raper, ItemStack result, int step) {
            this.victim = victim;
            this.raper = raper;
            this.result = result;
            this.step = step;
        }

        private static int getStep(ItemStack input) {
            if (!input.hasTag()) {
                return 0;
            } else {
                CompoundTag tag = input.getTag();
                if (!tag.contains("SequencedAssembly")) {
                    return 0;
                } else {
                    int step = tag.getCompound("SequencedAssembly").getInt("Step");
                    return step;
                }
            }
        }

        @Mod.EventBusSubscriber(modid = MOD_ID)
        public class PasswordCrackingEventHandler {
            @SubscribeEvent(priority = EventPriority.HIGH)
            public static void handleDeployRecipeSearching(DeployerRecipeSearchEvent event) {
                RecipeWrapper inv = event.getInventory();
                if (inv.getContainerSize() != 2) return;

                Recipe<? extends Container> recipe = event.getRecipe();
                if (!(recipe instanceof DeployerApplicationRecipe deployerRecipe)) return;

                ResourceLocation id = recipe.getId();
                if (id == null || !id.getPath().contains("cracking")) return;

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

                PasswordCrackingEvent crackingEvent = new PasswordCrackingEvent(victim, raper, result, step);
                MinecraftForge.EVENT_BUS.post(crackingEvent);

                ItemStack finalResult = crackingEvent.getFinalResult();
                if (finalResult != null) {
                    deployerRecipe.enforceNextResult(() -> finalResult);
                }
            }
        }
    }
}
