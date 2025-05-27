package me.zephyr.circube.content.teleport.beacon;

import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import me.zephyr.circube.CirCubeBlocks;
import me.zephyr.circube.CirCubeShapes;
import me.zephyr.circube.compact.CirCubeXaerosMinimap;
import me.zephyr.circube.util.DataManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class MechanicalBeaconBlock extends KineticBlock implements IBE<MechanicalBeaconBlockEntity> {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");


    public MechanicalBeaconBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER).setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF).add(ACTIVE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public boolean canSurvive(BlockState pState, @NotNull LevelReader pLevel, @NotNull BlockPos pPos) {
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER || pLevel.getBlockState(pPos.below()).is(this);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (pos.getY() < world.getHeight() - 1) {
            return defaultBlockState();
        }
        return null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            MechanicalBeaconBlockEntity be;
            if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
                be = (MechanicalBeaconBlockEntity) level.getBlockEntity(pos.below());
                pos = pos.below();
            } else {
                be = (MechanicalBeaconBlockEntity) level.getBlockEntity(pos);
            }
            if (player.isCrouching() && be.getOwner().equals(player.getUUID())) {
                Block block = player.level().getBlockState(pos).getBlock();
                ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(block);
                withBlockEntityDo(level, pos,
                        beacon -> NetworkHooks.openScreen((ServerPlayer) player, beacon, buf -> {
                            buf.writeUtf(beacon.getBeaconName());
                            buf.writeBlockPos(beacon.getBlockPos());
                            buf.writeBoolean(blockId.getPath().equals("brass_beacon"));
                            CompoundTag iconTag = new CompoundTag();
                            beacon.getIconItemStack().save(iconTag);
                            buf.writeNbt(iconTag);
                            buf.writeEnum(beacon.getPositionMode());
                        }));
            } else {
                String beaconId = be.getBeaconId();
                if (DataManager.addBeaconToPlayerData((ServerPlayer) player, beaconId)) {
                    level.playSound(null, be.getBlockPos(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.BLOCKS, 0.5F, 2F);
                    DataManager.addBeaconToMemory((ServerPlayer) player, beaconId);
                    CirCubeXaerosMinimap.addWaypoint(be.getBeaconName(), be.getBlockPos(), (ServerPlayer) player);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        //if (state.getValue(HALF) == DoubleBlockHalf.UPPER)
        //    return null;
        return IBE.super.newBlockEntity(pos, state);
    }

    @Override
    public Class<MechanicalBeaconBlockEntity> getBlockEntityClass() {
        return MechanicalBeaconBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MechanicalBeaconBlockEntity> getBlockEntityType() {
        return CirCubeBlocks.MECHANICAL_BEACON_ENTITY.get();
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? CirCubeShapes.BEACON_BOTTOM : CirCubeShapes.BEACON_TOP;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!world.isClientSide && blockEntity instanceof MechanicalBeaconBlockEntity mechanicalBeaconBlockEntity && placer instanceof Player) {
            mechanicalBeaconBlockEntity.initBlockEntity((Player) placer);
            DataManager.saveBeaconData((ServerLevel) world, mechanicalBeaconBlockEntity);
        }

        world.setBlockAndUpdate(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER));
    }

    @Override
    public void playerWillDestroy(Level world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        DoubleBlockHalf half = state.getValue(HALF);
        BlockPos offset = half == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
        world.destroyBlock(offset, false, player);
        if (!world.isClientSide) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MechanicalBeaconBlockEntity mechanicalBeaconBlockEntity) {
                DataManager.removeBeaconData((ServerLevel) world, mechanicalBeaconBlockEntity.getBeaconId());
            }
        }

        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.FAIL;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        if (world instanceof ServerLevel) {
            DoubleBlockHalf half = state.getValue(HALF);
            BlockPos offset = half == DoubleBlockHalf.LOWER ? context.getClickedPos().above() : context.getClickedPos().below();
            world.destroyBlock(offset, false);
        }
        super.onSneakWrenched(state, context);
        return InteractionResult.SUCCESS;
    }
}
