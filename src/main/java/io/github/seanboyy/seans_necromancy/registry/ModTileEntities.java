package io.github.seanboyy.seans_necromancy.registry;

import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.objects.tileentities.ChargerTileEntity;
import io.github.seanboyy.seans_necromancy.objects.tileentities.CorpseBuilderTileEntity;
import io.github.seanboyy.seans_necromancy.objects.tileentities.RitualTableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Necromancy.MOD_ID);

    public static final RegistryObject<TileEntityType<RitualTableTileEntity>> RITUAL_TABLE_TILE_ENTITY = TILE_ENTITIES.register("ritual_table", () -> TileEntityType.Builder.create(RitualTableTileEntity::new, ModBlocks.RITUAL_TABLE.get()).build(null));
    public static final RegistryObject<TileEntityType<CorpseBuilderTileEntity>> CORPSE_BUILDER_TILE_ENTITY = TILE_ENTITIES.register("corpse_builder", () -> TileEntityType.Builder.create(CorpseBuilderTileEntity::new, ModBlocks.CORPSE_BUILDER.get()).build(null));
    public static final RegistryObject<TileEntityType<ChargerTileEntity>> CHARGER_TILE_ENTITY = TILE_ENTITIES.register("charger", () -> TileEntityType.Builder.create(ChargerTileEntity::new, ModBlocks.CHARGER.get()).build(null));
}
