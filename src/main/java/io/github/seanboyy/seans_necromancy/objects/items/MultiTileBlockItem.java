package io.github.seanboyy.seans_necromancy.objects.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;

import static io.github.seanboyy.seans_necromancy.util.Constants.*;

public class MultiTileBlockItem extends BlockItem {
    public MultiTileBlockItem(Block blockIn, Item.Properties properties) {
        super(blockIn, properties);
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        return context.getWorld().setBlockState(context.getPos(), state, NO_NEIGHBOR_ACTION | FORCE_RE_RENDER_ON_MAIN | SEND_TO_CLIENT);
    }
}
