package io.github.seanboyy.seans_necromancy.objects.blocks;

import io.github.seanboyy.seans_necromancy.registry.ModTileEntities;
import io.github.seanboyy.seans_necromancy.util.enums.RitualTableSide;
import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

import static io.github.seanboyy.seans_necromancy.util.Constants.*;

@SuppressWarnings("deprecation")
public class RitualTableBlock extends HorizontalBlock {
    public static final EnumProperty<RitualTableSide> SIDE = EnumProperty.create("side", RitualTableSide.class);
    public static final VoxelShape TABLE_TOP = Block.makeCuboidShape(0D, 12D, 0D, 16D, 16D, 16D);
    public static final VoxelShape LEGS_LEFT_NS = VoxelShapes.or(
            Block.makeCuboidShape(1D, 0D, 1D, 3D, 2D, 15D),
            Block.makeCuboidShape(1D, 2D, 3D, 3D, 12D, 5D),
            Block.makeCuboidShape(1D, 2D, 11D, 3D, 12D, 13D));
    public static final VoxelShape LEGS_RIGHT_NS = VoxelShapes.or(
            Block.makeCuboidShape(13D, 0D, 1D, 15D, 2D, 15D),
            Block.makeCuboidShape(13D, 2D, 3D, 15D, 12D, 5D),
            Block.makeCuboidShape(13D, 2D, 11D, 15D, 12D, 13D));
    public static final VoxelShape LEGS_LEFT_WE = VoxelShapes.or(
            Block.makeCuboidShape(1D, 0D, 1D, 15D, 2D, 3D),
            Block.makeCuboidShape(11D, 2D, 1D, 13D, 12D, 3D),
            Block.makeCuboidShape(3D, 2D, 1D, 5D, 12D, 3D));
    public static final VoxelShape LEGS_RIGHT_WE = VoxelShapes.or(
            Block.makeCuboidShape(1D, 0D, 13D, 15D, 2D, 15D),
            Block.makeCuboidShape(11D, 2D, 13D, 13D, 12D, 15D),
            Block.makeCuboidShape(3D, 2D, 13D, 5D, 12D, 15D));
    public static final VoxelShape TABLE_LEFT_NS = VoxelShapes.or(TABLE_TOP, LEGS_LEFT_NS);
    public static final VoxelShape TABLE_RIGHT_NS = VoxelShapes.or(TABLE_TOP, LEGS_RIGHT_NS);
    public static final VoxelShape TABLE_LEFT_WE = VoxelShapes.or(TABLE_TOP, LEGS_LEFT_WE);
    public static final VoxelShape TABLE_RIGHT_WE = VoxelShapes.or(TABLE_TOP, LEGS_RIGHT_WE);

    public RitualTableBlock(Properties builder) {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(SIDE, RitualTableSide.LEFT));
    }

    //if i place the block facing north, this should return east
    private static Direction getDirectionToOther(RitualTableSide side, Direction facing) {
        return side == RitualTableSide.LEFT ? facing.rotateY() : facing.rotateYCCW();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        RitualTableSide ritualTablePart = state.get(SIDE);
        BlockPos blockPos = pos.offset(getDirectionToOther(ritualTablePart, state.get(HORIZONTAL_FACING)));
        BlockState blockState = worldIn.getBlockState(blockPos);
        if(blockState.getBlock() == this && blockState.get(SIDE) != ritualTablePart) {
            worldIn.setBlockState(blockPos, Blocks.AIR.getDefaultState(), NO_NEIGHBOR_DROP | SEND_TO_CLIENT | BLOCK_UPDATE);
            worldIn.playEvent(player, Constants.WorldEvents.BREAK_BLOCK_EFFECTS, blockPos, Block.getStateId(blockState));
            if(!worldIn.isRemote && !player.isCreative()) {
                ItemStack itemStack = player.getHeldItemMainhand();
                //spawnDrops(state, worldIn, pos, null, player, itemStack);
                //spawnDrops(blockState, worldIn, blockPos, null, player, itemStack);
            }
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getPlacementHorizontalFacing();
        BlockPos blockPos = context.getPos();
        BlockPos blockPos1 = blockPos.offset(direction.rotateY());
        return context.getWorld().getBlockState(blockPos1).isReplaceable(context) ? this.getDefaultState().with(HORIZONTAL_FACING, direction) : null;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.RITUAL_TABLE_TILE_ENTITY.get().create();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, SIDE);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        BlockPos blockPos = pos.offset(state.get(HORIZONTAL_FACING).rotateY());
            worldIn.setBlockState(blockPos, state.with(SIDE, RitualTableSide.RIGHT), BLOCK_UPDATE | SEND_TO_CLIENT);
            worldIn.notifyNeighbors(pos, Blocks.AIR);
            state.updateNeighbors(worldIn, pos, BLOCK_UPDATE | SEND_TO_CLIENT);
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.IGNORE;
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    @Override //TODO: verify this
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = getDirectionFromBlockState(state).getOpposite();
        switch(direction) {
            case NORTH:
                return TABLE_LEFT_WE;
            case SOUTH:
                return TABLE_RIGHT_WE;
            case WEST:
                return TABLE_LEFT_NS;
            case EAST:
            default:
                return TABLE_RIGHT_NS;
        }
    }

    public static Direction getDirectionFromBlockState(BlockState blockState) {
        Direction direction = blockState.get(HORIZONTAL_FACING);
        return blockState.get(SIDE) == RitualTableSide.RIGHT ? direction.rotateYCCW() : direction.rotateY();
    }

    /*
    //I don't think I'll need this, this is only if something about the BlockState changes post placement
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if(facing == getDirectionToOther(stateIn.get(SIDE), stateIn.get(HORIZONTAL_FACING))) {
            return facingState.getBlock() == this && facingState.get(SIDE) != stateIn.get(SIDE) ? stateIn.with
        }
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
     */
}
