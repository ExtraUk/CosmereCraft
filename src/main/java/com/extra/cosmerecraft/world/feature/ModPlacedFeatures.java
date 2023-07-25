package com.extra.cosmerecraft.world.feature;

import com.extra.cosmerecraft.CosmereCraft;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModPlacedFeatures {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, CosmereCraft.MODID);

    public static final RegistryObject<PlacedFeature> ATIUM_GEODE_PLACED = PLACED_FEATURES.register("atium_geode_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.ATIUM_GEODE.getHolder().get(), List.of(
                    RarityFilter.onAverageOnceEvery(200), InSquarePlacement.spread(),
                    HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.aboveBottom(20)),
                    BiomeFilter.biome())));

    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
    }
}
