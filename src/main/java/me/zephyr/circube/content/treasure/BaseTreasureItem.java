package me.zephyr.circube.content.treasure;

import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.compat.kubejs.util.TimelessItemType;
import com.tacz.guns.resource.index.CommonGunIndex;
import me.xjqsh.lrtactical.init.ModItems;
import net.createmod.catnip.data.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseTreasureItem extends Item {
    private final List<Pair<ItemStack, Integer>> pool = new ArrayList<>();

    public BaseTreasureItem(Item.Properties builder) {
        super(builder);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);

        if (!world.isClientSide) {

            world.playSound(null, player.blockPosition(), SoundEvents.CHEST_OPEN, SoundSource.PLAYERS, 1.0f, 1.0f);

            addReward();
            ItemStack reward = getRandomReward(world.random);

            if (!player.isCreative()) {
                held.shrink(1);
            }
            player.getInventory().placeItemBackInInventory(reward);
        }

        return InteractionResultHolder.success(held);
    }

    abstract protected void addReward();

    private ItemStack getRandomReward(RandomSource random) {
        int totalWeight = pool.stream().mapToInt(Pair::getSecond).sum();
        int choice = random.nextInt(totalWeight);
        int cumulative = 0;

        for (Pair<ItemStack, Integer> entry : pool) {
            cumulative += entry.getSecond();
            if (choice < cumulative) {
                return entry.getFirst().copy();
            }
        }

        return ItemStack.EMPTY;
    }

    protected void addMelee(String meleeId, int weight) {
        ItemStack melee = new ItemStack(ModItems.MELEE.get());
        melee.getOrCreateTag().putString("MeleeWeaponId", "cs2_wt:" + meleeId);

        pool.add(Pair.of(melee, weight));
    }

    protected void addGun(String namespace, String gunId, int weight) {
        Optional<CommonGunIndex> optional = TimelessAPI.getCommonGunIndex(ResourceLocation.fromNamespaceAndPath("tacz", gunId));
        if (optional.isPresent()) {
            CommonGunIndex index = optional.get();
            String mode = index.getGunData().getFireModeSet().get(0).name();

            ItemStack normal = new ItemStack(TimelessItemType.MODERN_KINETIC_GUN.getItem());
            normal.getOrCreateTag().putString("GunId", namespace + gunId);
            normal.getOrCreateTag().putString("GunFireMode", mode);

            pool.add(Pair.of(normal, weight));
        }
    }

    protected void addGun(String gunId, int weight, int weightGold) {
        Optional<CommonGunIndex> optional = TimelessAPI.getCommonGunIndex(ResourceLocation.fromNamespaceAndPath("tacz", gunId));
        if (optional.isPresent()) {
            CommonGunIndex index = optional.get();
            String mode = index.getGunData().getFireModeSet().get(0).name();

            ItemStack normal = new ItemStack(TimelessItemType.MODERN_KINETIC_GUN.getItem());
            normal.getOrCreateTag().putString("GunId", "tacz:" + gunId);
            normal.getOrCreateTag().putString("GunFireMode", mode);

            ItemStack gold = new ItemStack(TimelessItemType.MODERN_KINETIC_GUN.getItem());
            gold.getOrCreateTag().putString("GunId", "cgp:" + gunId + "_gold");
            gold.getOrCreateTag().putString("GunFireMode", mode);

            pool.add(Pair.of(normal, weight));
            pool.add(Pair.of(gold, weightGold));
        }
    }

    protected void addAttachment(String namespace, String attachmentId, int weight) {
        ItemStack normal = new ItemStack(TimelessItemType.ATTACHMENT.getItem());
        normal.getOrCreateTag().putString("AttachmentId", namespace + attachmentId);

        pool.add(Pair.of(normal, weight));
    }

    protected void addAttachment(String attachmentId, int weight, int weightGold) {
        ItemStack normal = new ItemStack(TimelessItemType.ATTACHMENT.getItem());
        normal.getOrCreateTag().putString("AttachmentId", "tacz:" + attachmentId);

        ItemStack gold = new ItemStack(TimelessItemType.ATTACHMENT.getItem());
        gold.getOrCreateTag().putString("AttachmentId", "cgp:" + attachmentId + "_gold");

        pool.add(Pair.of(normal, weight));
        pool.add(Pair.of(gold, weightGold));
    }
}
