package io.github.seanboyy.seans_necromancy.registry;

import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.objects.container.inventory.RitualTableContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, Necromancy.MOD_ID);

    public static final RegistryObject<ContainerType<RitualTableContainer>> RITUAL_TABLE_CONTAINER = CONTAINERS.register("ritual_table", () -> IForgeContainerType.create(RitualTableContainer::new));
}
