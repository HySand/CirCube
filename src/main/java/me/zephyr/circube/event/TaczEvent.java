package me.zephyr.circube.event;

import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.resource.index.CommonGunIndex;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static me.zephyr.circube.CirCube.MOD_ID;

public class TaczEvent {
    @Mod.EventBusSubscriber(modid = MOD_ID)
    public class GunWeightEvent {
        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            Player player = event.player;
            if (player.tickCount % 100 != 0) return;
            int gunCount = 0;
            List<ItemStack> allItems = new ArrayList<>(player.getInventory().items);
            allItems.add(player.getOffhandItem());
            for (ItemStack stack : allItems) {
                if (!stack.isEmpty()) {
                    CompoundTag tag = stack.getTag();
                    if (tag != null && tag.contains("GunId")) {
                        String location = tag.getString("GunId");
                        Optional<CommonGunIndex> optional = TimelessAPI.getCommonGunIndex(ResourceLocation.parse(location));
                        if (optional.isPresent()) {
                            CommonGunIndex index = optional.get();
                            if ("pistol".equals(index.getType())) {
                                gunCount += 2;
                            } else if ("smg".equals(index.getType())) {
                                gunCount += 3;
                            } else {
                                gunCount += 4;
                            }

                        }
                    }
                    if (gunCount > 6) {
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 2));
                        return;
                    }
                }
            }
        }
    }
}
