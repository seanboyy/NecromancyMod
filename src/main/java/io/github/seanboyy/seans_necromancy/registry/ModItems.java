package io.github.seanboyy.seans_necromancy.registry;

import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.objects.items.ChargedItem;
import io.github.seanboyy.seans_necromancy.objects.items.CorpseItem;
import io.github.seanboyy.seans_necromancy.objects.items.MultiTileBlockItem;
import io.github.seanboyy.seans_necromancy.objects.items.SpiritItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Necromancy.MOD_ID);

    public static final RegistryObject<MultiTileBlockItem> RITUAL_TABLE_ITEM = ITEMS.register("ritual_table", () -> new MultiTileBlockItem(ModBlocks.RITUAL_TABLE.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<MultiTileBlockItem> CORPSE_BUILDER_ITEM = ITEMS.register("corpse_builder", () -> new MultiTileBlockItem(ModBlocks.CORPSE_BUILDER.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<BlockItem> CHARGER_ITEM = ITEMS.register("charger", () -> new BlockItem(ModBlocks.CHARGER.get(), new Item.Properties().group(ItemGroup.MISC)));

    public static final RegistryObject<SpiritItem> SPIRIT_ITEM = ITEMS.register("spirit_item", () -> new SpiritItem(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ChargedItem> CHARGED_DIAMOND = ITEMS.register("charged_diamond", () -> new ChargedItem(new Item.Properties().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<CorpseItem> SKELETON = ITEMS.register("skeleton", () -> new CorpseItem(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<CorpseItem> ZOMBIE = ITEMS.register("zombie", () -> new CorpseItem(new Item.Properties().group(ItemGroup.MISC)));
}
