package io.github.seanboyy.seans_necromancy.util;

import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.client.gui.inventory.ChargerScreen;
import io.github.seanboyy.seans_necromancy.client.gui.inventory.CorpseBuilderScreen;
import io.github.seanboyy.seans_necromancy.registry.ModBlocks;
import io.github.seanboyy.seans_necromancy.registry.ModContainers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Necromancy.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainers.CORPSE_BUILDER_CONTAINER.get(), CorpseBuilderScreen::new);
        RenderTypeLookup.setRenderLayer(ModBlocks.CORPSE_BUILDER.get(), RenderType.getCutout());
        ScreenManager.registerFactory(ModContainers.CHARGER_CONTAINER.get(), ChargerScreen::new);
    }
}
