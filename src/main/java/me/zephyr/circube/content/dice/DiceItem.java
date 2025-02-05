package me.zephyr.circube.content.dice;

import me.zephyr.circube.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class DiceItem extends Item {

    public DiceItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack dice = player.getItemInHand(hand);
        ItemStack copy = dice.copy();
        if (!world.isClientSide()) {
            int count = dice.getCount();
            int randomNum = 0;
            Random random = new Random();
            Component hover = Component.empty();
            switch (ForgeRegistries.ITEMS.getKey(dice.getItem()).toString()) {
                case "circube:four_sided_die":
                    for (int i = 0; i < count; i++) {
                        int num = random.nextInt(4) + 1;
                        randomNum += num;
                        hover = hover.copy().append(num + " ");
                    }
                    break;
                case "circube:six_sided_die":
                    for (int i = 0; i < count; i++) {
                        int num = random.nextInt(6) + 1;
                        randomNum += num;
                        hover = hover.copy().append(num + " ");
                    }
                    break;
                case "circube:eight_sided_die":
                    for (int i = 0; i < count; i++) {
                        int num = random.nextInt(8) + 1;
                        randomNum += num;
                        hover = hover.copy().append(num + " ");
                    }
                    break;
                case "circube:ten_sided_die":
                    for (int i = 0; i < count; i++) {
                        int num = random.nextInt(10) + 1;
                        randomNum += num;
                        hover = hover.copy().append(num + " ");
                    }
                    break;
                case "circube:twelve_sided_die":
                    for (int i = 0; i < count; i++) {
                        int num = random.nextInt(12) + 1;
                        randomNum += num;
                        hover = hover.copy().append(num + " ");
                    }
                    break;
                case "circube:twenty_sided_die":
                    for (int i = 0; i < count; i++) {
                        int num = random.nextInt(20) + 1;
                        randomNum += num;
                        hover = hover.copy().append(num + " ");
                    }
            }
            player.getCooldowns().addCooldown(this, 40);

            String hoverText = hover.getString().replaceAll("\\s+$", "");
            hover = Component.literal(hoverText);
            broadcastDiceMessage(player, count, randomNum, dice, hover);

            player.drop(copy, false);
            dice.shrink(count);

        }
        return InteractionResultHolder.success(dice);
    }

    private void broadcastDiceMessage(Player player, int count, int randomNum, ItemStack dice, Component hover) {
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover);
        MutableComponent diceMessage = Lang
                .translateDirect("dice.message", player.getName(), count, dice.getHoverName(), Component.literal(String.valueOf(randomNum)).withStyle(ChatFormatting.AQUA))
                .withStyle(style -> style.withHoverEvent(hoverEvent).withColor(ChatFormatting.GRAY));
        BlockPos pos = player.getOnPos();
        for (Player nearbyPlayer : player.level().players()) {
            if (nearbyPlayer.blockPosition().closerThan(pos, 128)) {
                nearbyPlayer.sendSystemMessage(diceMessage);
            }
        }
    }
}
