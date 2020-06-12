package io.github.seanboyy.seans_necromancy.objects.blocks;

import io.github.seanboyy.seans_necromancy.registry.ModTileEntities;
import io.github.seanboyy.seans_necromancy.util.enums.LeftRightBlockSide;
import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathType;
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

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class RitualTableBlock extends LeftRightDoubleBlock {
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
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(state.get(SIDE) == LeftRightBlockSide.RIGHT) {
            pos = pos.offset(getDirectionToOther(LeftRightBlockSide.RIGHT, state.get(HORIZONTAL_FACING)));
        }
        if(!worldIn.isRemote) {
            player.openContainer(state.getContainer(worldIn, pos));
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(SIDE) == LeftRightBlockSide.LEFT;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.RITUAL_TABLE_TILE_ENTITY.get().create();
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.IGNORE;
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = getDirectionToOtherFromBlockState(state).getOpposite();
        switch (direction) {
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
}
