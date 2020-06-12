package io.github.seanboyy.seans_necromancy.objects.blocks;

import io.github.seanboyy.seans_necromancy.util.enums.LeftRightBlockSide;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

import static io.github.seanboyy.seans_necromancy.util.Constants.*;

public abstract class LeftRightDoubleBlock extends HorizontalBlock {
    public static final EnumProperty<LeftRightBlockSide> SIDE = EnumProperty.create("side", LeftRightBlockSide.class);

    protected LeftRightDoubleBlock(Properties builder) {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(SIDE, LeftRightBlockSide.LEFT));
    }

    protected static Direction getDirectionToOther(LeftRightBlockSide side, Direction facing) {
        return side == LeftRightBlockSide.LEFT ? facing.rotateY() : facing.rotateYCCW();
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        LeftRightBlockSide ritualTablePart = state.get(SIDE);
        BlockPos blockPos = pos.offset(getDirectionToOther(ritualTablePart, state.get(HORIZONTAL_FACING)));
        BlockState blockState = worldIn.getBlockState(blockPos);
        if(blockState.getBlock() == this && blockState.get(SIDE) != ritualTablePart) {
            worldIn.setBlockState(blockPos, Blocks.AIR.getDefaultState(), NO_NEIGHBOR_DROP | SEND_TO_CLIENT | BLOCK_UPDATE);
            worldIn.playEvent(player, Constants.WorldEvents.BREAK_BLOCK_EFFECTS, blockPos, Block.getStateId(blockState));
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
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, SIDE);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        BlockPos blockPos = pos.offset(state.get(HORIZONTAL_FACING).rotateY());
        worldIn.setBlockState(blockPos, state.with(SIDE, LeftRightBlockSide.RIGHT), BLOCK_UPDATE | SEND_TO_CLIENT);
        worldIn.notifyNeighbors(pos, Blocks.AIR);
        worldIn.notifyNeighbors(blockPos, Blocks.AIR);
        state.updateNeighbors(worldIn, pos, BLOCK_UPDATE | SEND_TO_CLIENT);
        state.updateNeighbors(worldIn, blockPos, BLOCK_UPDATE | SEND_TO_CLIENT);
    }

    public static Direction getDirectionToOtherFromBlockState(BlockState blockState) {
        Direction direction = blockState.get(HORIZONTAL_FACING);
        return blockState.get(SIDE) == LeftRightBlockSide.RIGHT ? direction.rotateYCCW() : direction.rotateY();
    }
}
