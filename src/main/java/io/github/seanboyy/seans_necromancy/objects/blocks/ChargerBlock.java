package io.github.seanboyy.seans_necromancy.objects.blocks;

import io.github.seanboyy.seans_necromancy.objects.tileentities.ChargerTileEntity;
import io.github.seanboyy.seans_necromancy.registry.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class ChargerBlock extends HorizontalBlock {
    private static final VoxelShape BOX = Block.makeCuboidShape(0, 0, 0, 16, 16, 16);
    private static final VoxelShape INSIDE_N = Block.makeCuboidShape(6, 6, 0, 10, 10, 8);
    private static final VoxelShape INSIDE_E = Block.makeCuboidShape(8, 6, 6, 16, 10, 10);
    private static final VoxelShape INSIDE_S = Block.makeCuboidShape(6, 6, 8, 10, 10, 16);
    private static final VoxelShape INSIDE_W = Block.makeCuboidShape(0, 6, 6, 8, 10, 10);
    private static final VoxelShape SHAPE_N = VoxelShapes.combineAndSimplify(BOX, INSIDE_N, IBooleanFunction.ONLY_FIRST);
    private static final VoxelShape SHAPE_W = VoxelShapes.combineAndSimplify(BOX, INSIDE_W, IBooleanFunction.ONLY_FIRST);
    private static final VoxelShape SHAPE_S = VoxelShapes.combineAndSimplify(BOX, INSIDE_S, IBooleanFunction.ONLY_FIRST);
    private static final VoxelShape SHAPE_E = VoxelShapes.combineAndSimplify(BOX, INSIDE_E, IBooleanFunction.ONLY_FIRST);

    public ChargerBlock(Properties builder) {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction facing = context.getPlacementHorizontalFacing();
        return this.getDefaultState().with(HORIZONTAL_FACING, facing.getOpposite());
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.CHARGER_TILE_ENTITY.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof ChargerTileEntity) {
                player.openContainer((ChargerTileEntity)tileEntity);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof ChargerTileEntity) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (ChargerTileEntity)tileEntity);
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction facing = state.get(HORIZONTAL_FACING);
        switch(facing) {
            case EAST:
                return SHAPE_E;
            case WEST:
                return SHAPE_W;
            case SOUTH:
                return SHAPE_S;
            case NORTH:
            default:
                return SHAPE_N;
        }
    }
}
