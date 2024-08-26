package me.zephyr.circube.content.stabilizer;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StabilizerItem extends Item {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public StabilizerItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!world.isClientSide()) {
            if (player.isCrouching()) {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                BlockPos spawnLocation = serverPlayer.getRespawnPosition();
                if (spawnLocation != null) {
                    return doTeleport(itemStack, world, serverPlayer, spawnLocation, true);
                }
            } else {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                BlockPos spawnLocation = serverPlayer.getRespawnPosition();
                if (spawnLocation != null) {
                    return doTeleport(itemStack, world, serverPlayer, spawnLocation, false);
                }
            }
        }
        return InteractionResultHolder.fail(itemStack);
    }

    private InteractionResultHolder<ItemStack> doTeleport (ItemStack itemStack, Level world, ServerPlayer player, BlockPos pos, boolean consume) {
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
}
