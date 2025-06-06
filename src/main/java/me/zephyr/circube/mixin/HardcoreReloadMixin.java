package me.zephyr.circube.mixin;

import com.tacz.guns.api.item.gun.AbstractGunItem;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import com.tacz.guns.resource.index.CommonGunIndex;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import com.tacz.guns.util.AttachmentDataUtils;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModernKineticGunScriptAPI.class)
public class HardcoreReloadMixin {

    @Shadow(remap = false)
    private ItemStack itemStack;
    @Shadow(remap = false)
    private CommonGunIndex gunIndex;
    @Shadow(remap = false)
    private AbstractGunItem abstractGunItem;

    @Inject(method = "getNeededAmmoAmount", at = @At("HEAD"), remap = false, cancellable = true)
    public void getNeededAmmoAmount(CallbackInfoReturnable<Integer> cir) {
        GunData gunData = gunIndex.getGunData();
        int maxAmmoCount = AttachmentDataUtils.getAmmoCountWithAttachment(itemStack, gunData);
        int currentAmmoCount = abstractGunItem.getCurrentAmmoCount(itemStack);
        if ((gunData.getScriptParam() != null && gunData.getScriptParam().containsKey("loop")) || maxAmmoCount == 2) {
            cir.setReturnValue(maxAmmoCount - currentAmmoCount);
        } else {
            cir.setReturnValue(maxAmmoCount);
        }
    }
}
