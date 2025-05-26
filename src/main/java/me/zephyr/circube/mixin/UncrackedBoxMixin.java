package me.zephyr.circube.mixin;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.utility.CreateLang;
import me.zephyr.circube.CirCubeLang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;


@Mixin(SequencedAssemblyRecipe.class)
public class UncrackedBoxMixin {

    private static int getStepFromNBT(ItemStack input) {
        if (!input.hasTag()) return 0;
        CompoundTag tag = input.getTag();
        if (!tag.contains("SequencedAssembly")) return 0;
        return tag.getCompound("SequencedAssembly").getInt("Step");
    }

    @Inject(method = "addToTooltip", at = @At("HEAD"), remap = false, cancellable = true)
    private static void addToTooltip(ItemTooltipEvent event, CallbackInfo ci) {
        ItemStack stack = event.getItemStack();
        if (!stack.hasTag() || !stack.getTag()
                .contains("WrongRecipe"))
            return;
        CompoundTag compound = stack.getTag()
                .getCompound("SequencedAssembly");
        ResourceLocation resourceLocation = ResourceLocation.parse(compound.getString("id"));
        Optional<? extends Recipe<?>> optionalRecipe = Minecraft.getInstance().level.getRecipeManager()
                .byKey(resourceLocation);
        if (!optionalRecipe.isPresent())
            return;
        Recipe<?> recipe = optionalRecipe.get();
        if (!(recipe instanceof SequencedAssemblyRecipe sequencedAssemblyRecipe))
            return;

        int length = sequencedAssemblyRecipe.getSequence().size();
        int step = getStepFromNBT(stack);
        int total = length * sequencedAssemblyRecipe.getLoops();
        List<Component> tooltip = event.getToolTip();

        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(CirCubeLang.translateDirect("recipe.password_cracking")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(CirCubeLang.translateDirect("recipe.password_cracking.progress", step, total)
                .withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(CreateLang.translateDirect("recipe.assembly.next", CirCubeLang.translateDirect("recipe.password_cracking.unknown"))
                .withStyle(ChatFormatting.AQUA));
        ci.cancel();
    }
}
