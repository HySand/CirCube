package me.zephyr.circube.content.stabilizer;

import me.zephyr.circube.CirCubeGuiTypes;
import net.minecraft.client.Minecraft;
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
                    return doTeleport(itemStack, world, serverPlayer, spawnLocation, true);
                }
            } else {
                ItemStack heldItem = player.getItemInHand(hand);
                NetworkHooks.openScreen(serverPlayer, this, buf -> {
                    buf.writeItem(heldItem);
                });
            }
        }
        return InteractionResultHolder.fail(itemStack);
    }

    private InteractionResultHolder<ItemStack> doTeleport(ItemStack itemStack, Level world, ServerPlayer player, BlockPos pos, boolean consume) {
        scheduler.schedule(() -> {
            player.teleportTo(pos.getX(), pos.getY() + 0.5, pos.getZ());
            world.playSound(null, player.blockPosition(), SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 1.0F, 1.0F);
        }, 1500, TimeUnit.MILLISECONDS);
        MobEffectInstance blind = new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false);
        player.addEffect(blind);
        world.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
        player.getCooldowns().addCooldown(this, 100);
        if (consume) {
            itemStack.shrink(1);
            Minecraft.getInstance().gameRenderer.displayItemActivation(itemStack);
        }
        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        ItemStack heldItem = player.getMainHandItem();
        return new StabilizerMenu(CirCubeGuiTypes.STABILIZER_MENU.get(), id, inv, heldItem);
    }

    @Override
    public Component getDisplayName() {
        return getDescription();
    }
}
