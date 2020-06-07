package io.github.seanboyy.seans_necromancy.registry;

import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.objects.items.RitualTableItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Necromancy.MOD_ID);

    public static final RegistryObject<RitualTableItem> RITUAL_TABLE_ITEM = ITEMS.register("ritual_table", () -> new RitualTableItem(ModBlocks.RITUAL_TABLE.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));
}
