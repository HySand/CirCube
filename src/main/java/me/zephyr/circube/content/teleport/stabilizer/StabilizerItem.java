package me.zephyr.circube.content.teleport.stabilizer;

import me.zephyr.circube.CirCubeMenuTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StabilizerItem extends Item implements MenuProvider {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public StabilizerItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!world.isClientSide()) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            if (player.isCrouching()) {
                BlockPos spawnLocation = serverPlayer.getRespawnPosition();
                if (spawnLocation != null) {
                    return doTeleport(itemStack, world, serverPlayer, spawnLocation);
                } else {
                    player.displayClientMessage(Component.translatable("block.minecraft.spawn.not_valid").withStyle(ChatFormatting.YELLOW), true);
                }
            } else {
                ItemStack heldItem = player.getItemInHand(hand);
                NetworkHooks.openScreen(serverPlayer, this, buf -> {
                    buf.writeItem(heldItem);
                });
            }
        }
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }

    private InteractionResultHolder<ItemStack> doTeleport(ItemStack itemStack, Level world, ServerPlayer player, BlockPos pos) {
        scheduler.schedule(() -> {
            player.teleportTo(pos.getX(), pos.getY() + 0.5, pos.getZ());
            world.playSound(null, player.blockPosition(), SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 1.0F, 1.0F);
        }, 1500, TimeUnit.MILLISECONDS);
        MobEffectInstance blind = new MobEffectInstance(MobEffects.DARKNESS, 60, 0, true, false);
        player.addEffect(blind);
        world.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
        player.getCooldowns().addCooldown(this, 100);
        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        ItemStack heldItem = player.getMainHandItem();
        return new StabilizerMenu(CirCubeMenuTypes.STABILIZER_MENU.get(), id, inv, heldItem);
    }

    @Override
    public Component getDisplayName() {
        return getDescription();
    }
}
