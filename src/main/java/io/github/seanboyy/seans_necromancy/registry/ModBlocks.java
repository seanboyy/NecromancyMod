package io.github.seanboyy.seans_necromancy.registry;

import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.objects.blocks.ChargerBlock;
import io.github.seanboyy.seans_necromancy.objects.blocks.CorpseBuilderBlock;
import io.github.seanboyy.seans_necromancy.objects.blocks.RitualTableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Necromancy.MOD_ID);

    public static final RegistryObject<Block> RITUAL_TABLE = BLOCKS.register("ritual_table", () -> new RitualTableBlock(Block.Properties.from(Blocks.CRAFTING_TABLE)));
    public static final RegistryObject<Block> CORPSE_BUILDER = BLOCKS.register("corpse_builder", () -> new CorpseBuilderBlock(Block.Properties.from(Blocks.STONE_BRICKS)));
    public static final RegistryObject<Block> CHARGER = BLOCKS.register("charger", () -> new ChargerBlock(Block.Properties.from(Blocks.FURNACE)));
}
