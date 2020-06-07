package io.github.seanboyy.seans_necromancy.registry;

import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.objects.tileentities.RitualTableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Necromancy.MOD_ID);

    public static final RegistryObject<TileEntityType<RitualTableTileEntity>> RITUAL_TABLE_TILE_ENTITY = TILE_ENTITIES.register("ritual_table", () -> TileEntityType.Builder.create(RitualTableTileEntity::new, ModBlocks.RITUAL_TABLE.get()).build(null));
}
