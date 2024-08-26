package me.zephyr.circube.content.beacon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import me.zephyr.circube.CirCubeItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class MechanicalBeaconRenderer extends KineticBlockEntityRenderer<MechanicalBeaconBlockEntity> {

    public MechanicalBeaconRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void renderSafe(MechanicalBeaconBlockEntity entity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        ItemStack stack;
        if (entity.getBlockState().getValue(MechanicalBeaconBlock.ACTIVE)) {
            stack = new ItemStack(CirCubeItems.STABILIZER);
        } else {
            stack = new ItemStack(CirCubeItems.INCOMPLETE_STABILIZER);
        }

        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(1f, 4f, 1f);

        poseStack.mulPose(Axis.YN.rotationDegrees(entity.lookingRotR));
        poseStack.mulPose(Axis.YP.rotationDegrees(90));

        int lightAbove = LevelRenderer.getLightColor(entity.getLevel(), entity.getBlockPos().above());
        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack, ItemDisplayContext.FIXED, lightAbove, overlay,
                poseStack, multiBufferSource, entity.getLevel(), (int) entity.getBlockPos().asLong()
        );

        poseStack.popPose();
    }
}
