package io.github.seanboyy.seans_necromancy;

import io.github.seanboyy.seans_necromancy.crafting.ChargingRecipe;
import io.github.seanboyy.seans_necromancy.crafting.RecipeTypeCharging;
import io.github.seanboyy.seans_necromancy.network.NetworkHandler;
import io.github.seanboyy.seans_necromancy.registry.*;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
        NetworkHandler networkHandler = new NetworkHandler();
        commonStart(modEventBus, networkHandler);
        clientStart(modEventBus, networkHandler);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModTileEntities.TILE_ENTITIES.register(modEventBus);
        ModContainers.CONTAINERS.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static void commonStart(IEventBus modEventBus, NetworkHandler networkHandler) {
        modEventBus.addListener(EventPriority.NORMAL, false, FMLCommonSetupEvent.class, event -> networkHandler.createServerPacketHandler());
    }

    private static void clientStart(IEventBus modEventBus, NetworkHandler networkHandler) {
        modEventBus.addListener(EventPriority.NORMAL, false, FMLClientSetupEvent.class, event -> networkHandler.createClientPacketHandler());
    }
}
