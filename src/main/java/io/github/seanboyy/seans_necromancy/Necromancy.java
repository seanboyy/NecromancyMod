package io.github.seanboyy.seans_necromancy;

import io.github.seanboyy.seans_necromancy.registry.ModBlocks;
import io.github.seanboyy.seans_necromancy.registry.ModItems;
import io.github.seanboyy.seans_necromancy.registry.ModTileEntities;
import io.github.seanboyy.seans_necromancy.registry.ModContainers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("seans_necromancy")
@Mod.EventBusSubscriber(modid = Necromancy.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Necromancy {
    public static final String MOD_ID = "seans_necromancy";
    public static final Logger LOGGER = LogManager.getLogger();

    public Necromancy() {
        LOGGER.info("Starting up Necromancy");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModTileEntities.TILE_ENTITIES.register(modEventBus);
        ModContainers.CONTAINERS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
