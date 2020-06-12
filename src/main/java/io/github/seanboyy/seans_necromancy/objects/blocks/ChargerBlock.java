package io.github.seanboyy.seans_necromancy.objects.blocks;

import io.github.seanboyy.seans_necromancy.objects.tileentities.ChargerTileEntity;
import net.minecraft.block.ContainerBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class ChargerBlock extends ContainerBlock {

    public ChargerBlock(Properties builder) {
        super(builder);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new ChargerTileEntity();
    }
}
