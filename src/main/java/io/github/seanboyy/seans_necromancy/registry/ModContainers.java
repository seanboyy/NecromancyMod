package io.github.seanboyy.seans_necromancy.registry;

import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.container.inventory.ChargerContainer;
import io.github.seanboyy.seans_necromancy.container.inventory.CorpseBuilderContainer;
import io.github.seanboyy.seans_necromancy.container.inventory.RitualTableContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Necromancy.MOD_ID);

    public static final RegistryObject<ContainerType<RitualTableContainer>> RITUAL_TABLE_CONTAINER = CONTAINERS.register("ritual_table", () -> IForgeContainerType.create(RitualTableContainer::new));
    public static final RegistryObject<ContainerType<CorpseBuilderContainer>> CORPSE_BUILDER_CONTAINER = CONTAINERS.register("corpse_builder", () -> IForgeContainerType.create(CorpseBuilderContainer::new));
    public static final RegistryObject<ContainerType<ChargerContainer>> CHARGER_CONTAINER = CONTAINERS.register("charger", () -> IForgeContainerType.create(ChargerContainer::new));
}
