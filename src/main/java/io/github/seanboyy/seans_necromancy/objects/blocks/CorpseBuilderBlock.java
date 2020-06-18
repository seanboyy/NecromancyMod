package io.github.seanboyy.seans_necromancy.objects.blocks;


import io.github.seanboyy.seans_necromancy.objects.tileentities.CorpseBuilderTileEntity;
import io.github.seanboyy.seans_necromancy.registry.ModTileEntities;
import io.github.seanboyy.seans_necromancy.util.enums.LeftRightBlockSide;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CorpseBuilderBlock extends LeftRightDoubleBlock {
    public static final EnumProperty<LeftRightBlockSide> SIDE = EnumProperty.create("side", LeftRightBlockSide.class);
    public static final IntegerProperty SKELETON_LEVEL = IntegerProperty.create("skeleton_level", 0, 6);
    public static final IntegerProperty FLESH_LEVEL = IntegerProperty.create("flesh_level", 0, 6);
    public static final VoxelShape TABLE_TOP = Block.makeCuboidShape(0, 4, 0, 16, 7, 16);
    public static final VoxelShape TABLE_FOOT_LEFT_NS = Block.makeCuboidShape(2, 0, 2, 16, 4, 14);
    public static final VoxelShape TABLE_FOOT_RIGHT_NS = Block.makeCuboidShape(0, 0, 2, 14, 4, 14);
    public static final VoxelShape TABLE_FOOT_LEFT_WE = Block.makeCuboidShape(2, 0, 2, 14, 4, 16);
    public static final VoxelShape TABLE_FOOT_RIGHT_WE = Block.makeCuboidShape(2, 0, 0, 14, 4, 14);
    public static final VoxelShape RIM_LEFT_NS = VoxelShapes.or(
            Block.makeCuboidShape(0, 7, 0, 1, 8, 16),
            Block.makeCuboidShape(1, 7, 0, 16, 8, 1),
            Block.makeCuboidShape(1, 7, 15, 16, 8, 16));
    public static final VoxelShape RIM_RIGHT_NS = VoxelShapes.or(
            Block.makeCuboidShape(15, 7, 0, 16, 8, 16),
            Block.makeCuboidShape(0, 7, 0, 15, 8, 1),
            Block.makeCuboidShape(0, 7, 15, 15, 7, 16));
    public static final VoxelShape RIM_LEFT_WE = VoxelShapes.or(
            Block.makeCuboidShape(0, 7, 0, 16, 8, 1),
            Block.makeCuboidShape(0, 7, 1, 1, 8, 16),
            Block.makeCuboidShape(15, 7, 1, 16, 8, 16));
    public static final VoxelShape RIM_RIGHT_WE = VoxelShapes.or(
            Block.makeCuboidShape(0, 7, 15, 16, 8, 16),
            Block.makeCuboidShape(0, 7, 0, 1, 8, 15),
            Block.makeCuboidShape(15, 7, 0, 16, 8, 15));

    public static final VoxelShape TABLE_LEFT_NS = VoxelShapes.or(TABLE_TOP, TABLE_FOOT_LEFT_NS, RIM_LEFT_NS);
    public static final VoxelShape TABLE_RIGHT_NS = VoxelShapes.or(TABLE_TOP, TABLE_FOOT_RIGHT_NS, RIM_RIGHT_NS);
    public static final VoxelShape TABLE_LEFT_WE = VoxelShapes.or(TABLE_TOP, TABLE_FOOT_LEFT_WE, RIM_LEFT_WE);
    public static final VoxelShape TABLE_RIGHT_WE = VoxelShapes.or(TABLE_TOP, TABLE_FOOT_RIGHT_WE, RIM_RIGHT_WE);

    public CorpseBuilderBlock(Properties builder) {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(SIDE, LeftRightBlockSide.LEFT).with(SKELETON_LEVEL, 0).with(FLESH_LEVEL, 0));
    }

    @Override
    public void tryOpenContainer(World worldIn, BlockPos pos, PlayerEntity playerIn) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity instanceof CorpseBuilderTileEntity) {
            playerIn.openContainer((CorpseBuilderTileEntity)tileEntity);
        }
    }

    @Override
    public void tryDropItems(TileEntity tileEntityIn, World worldIn, BlockPos pos) {
        if(tileEntityIn instanceof CorpseBuilderTileEntity) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (CorpseBuilderTileEntity)tileEntityIn);
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.CORPSE_BUILDER_TILE_ENTITY.get().create();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(SKELETON_LEVEL, FLESH_LEVEL);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = getDirectionToOtherFromBlockState(state).getOpposite();
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
}
