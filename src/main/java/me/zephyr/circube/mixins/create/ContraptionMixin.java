package me.zephyr.circube.mixins.create;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.*;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.content.contraptions.pulley.PulleyBlock;
import com.simibubi.create.content.contraptions.pulley.PulleyBlockEntity;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.NBTProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.MutablePair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(value = Contraption.class, remap = false)
public abstract class ContraptionMixin {

    @Shadow
    public boolean disassembled;

    @Shadow
    protected Map<BlockPos, StructureTemplate.StructureBlockInfo> blocks;

    @Shadow
    protected MountedStorageManager storage;

    @Shadow
    protected abstract boolean shouldUpdateAfterMovement(StructureTemplate.StructureBlockInfo info);

    @Shadow
    protected List<AABB> superglue;

    @Shadow
    protected abstract boolean customBlockPlacement(LevelAccessor world, BlockPos pos, BlockState state);

    @Shadow
    public abstract List<ItemStack> getDisabledActors();

    @Shadow
    public abstract List<MutablePair<StructureTemplate.StructureBlockInfo, MovementContext>> getActors();

    @Shadow
    protected abstract void disableActorOnStart(MovementContext context);

    @Shadow
    protected List<ItemStack> disabledActors;

    @Shadow
    protected List<MutablePair<StructureTemplate.StructureBlockInfo, MovementContext>> actors;

    @Shadow
    protected ContraptionWorld world;

    @Shadow
    public abstract void stop(Level world);

    /**
     * @author zephyr
     * @reason ban superfast block break
     */
    @Overwrite
    public void addBlocksToWorld(Level world, StructureTransform transform) {
        if (disassembled)
            return;
        disassembled = true;
        boolean destroied = false;

        for (boolean nonBrittles : Iterate.trueAndFalse) {
            for (StructureTemplate.StructureBlockInfo block : blocks.values()) {
                if (nonBrittles == BlockMovementChecks.isBrittle(block.state()))
                    continue;

                BlockPos targetPos = transform.apply(block.pos());
                BlockState state = transform.apply(block.state());

                if (this.customBlockPlacement(world, targetPos, state))
                    continue;

                if (nonBrittles)
                    for (Direction face : Iterate.directions)
                        state = state.updateShape(face, world.getBlockState(targetPos.relative(face)), world, targetPos,
                                targetPos.relative(face));

                BlockState blockState = world.getBlockState(targetPos);
                if (blockState.getDestroySpeed(world, targetPos) == -1 || (state.getCollisionShape(world, targetPos)
                        .isEmpty()
                        && !blockState.getCollisionShape(world, targetPos)
                        .isEmpty())) {
                    if (targetPos.getY() == world.getMinBuildHeight())
                        targetPos = targetPos.above();
                    world.levelEvent(2001, targetPos, Block.getId(state));
                    Block.dropResources(state, world, targetPos, null);
                    continue;
                }
                if (state.getBlock() instanceof SimpleWaterloggedBlock
                        && state.hasProperty(BlockStateProperties.WATERLOGGED)) {
                    FluidState FluidState = world.getFluidState(targetPos);
                    state = state.setValue(BlockStateProperties.WATERLOGGED, FluidState.getType() == Fluids.WATER);
                }

                if (blockState.getDestroySpeed(world, targetPos) > 0F
                        && world.destroyBlock(targetPos, true)) destroied = true;

                if (AllBlocks.SHAFT.has(state))
                    state = ShaftBlock.pickCorrectShaftType(state, world, targetPos);
                if (state.hasProperty(SlidingDoorBlock.VISIBLE))
                    state = state.setValue(SlidingDoorBlock.VISIBLE, !state.getValue(SlidingDoorBlock.OPEN))
                            .setValue(SlidingDoorBlock.POWERED, false);
                // Stop Sculk shriekers from getting "stuck" if moved mid-shriek.
                if (state.is(Blocks.SCULK_SHRIEKER)) {
                    state = Blocks.SCULK_SHRIEKER.defaultBlockState();
                }

                world.setBlock(targetPos, state, Block.UPDATE_MOVE_BY_PISTON | Block.UPDATE_ALL);

                boolean verticalRotation = transform.rotationAxis == null || transform.rotationAxis.isHorizontal();
                verticalRotation = verticalRotation && transform.rotation != Rotation.NONE;
                if (verticalRotation) {
                    if (state.getBlock() instanceof PulleyBlock.RopeBlock || state.getBlock() instanceof PulleyBlock.MagnetBlock
                            || state.getBlock() instanceof DoorBlock)
                        world.destroyBlock(targetPos, true);
                }

                BlockEntity blockEntity = world.getBlockEntity(targetPos);

                CompoundTag tag = block.nbt();

                // Temporary fix: Calling load(CompoundTag tag) on a Sculk sensor causes it to not react to vibrations.
                if (state.is(Blocks.SCULK_SENSOR) || state.is(Blocks.SCULK_SHRIEKER))
                    tag = null;

                if (blockEntity != null)
                    tag = NBTProcessors.process(blockEntity, tag, false);
                if (blockEntity != null && tag != null) {
                    tag.putInt("x", targetPos.getX());
                    tag.putInt("y", targetPos.getY());
                    tag.putInt("z", targetPos.getZ());

                    if (verticalRotation && blockEntity instanceof PulleyBlockEntity) {
                        tag.remove("Offset");
                        tag.remove("InitialOffset");
                    }

                    if (blockEntity instanceof IMultiBlockEntityContainer && tag.contains("LastKnownPos"))
                        tag.put("LastKnownPos", NbtUtils.writeBlockPos(BlockPos.ZERO.below(Integer.MAX_VALUE - 1)));

                    blockEntity.load(tag);
                    this.storage.addStorageToWorld(block, blockEntity);
                }

                transform.apply(blockEntity);

                if (destroied) {
                    world.destroyBlock(targetPos, true);
                    destroied = false;
                }
            }
        }

        for (StructureTemplate.StructureBlockInfo block : blocks.values()) {
            if (!this.shouldUpdateAfterMovement(block))
                continue;
            BlockPos targetPos = transform.apply(block.pos());
            world.markAndNotifyBlock(targetPos, world.getChunkAt(targetPos), block.state(), block.state(),
                    Block.UPDATE_MOVE_BY_PISTON | Block.UPDATE_ALL, 512);
        }

        for (AABB box : this.superglue) {
            box = new AABB(transform.apply(new Vec3(box.minX, box.minY, box.minZ)),
                    transform.apply(new Vec3(box.maxX, box.maxY, box.maxZ)));
            if (!world.isClientSide)
                world.addFreshEntity(new SuperGlueEntity(world, box));
        }

        storage.clear();
    }

    /**
     * @author zephyr
     * @reason fuel
     */
    @Inject(method = "startMoving", at = @At("TAIL"))
    public void stopMoving(Level world, CallbackInfo ci) {
        this.stop(world);
    }
}
