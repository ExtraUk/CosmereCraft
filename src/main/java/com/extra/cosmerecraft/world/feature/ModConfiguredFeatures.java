package com.extra.cosmerecraft.world.feature;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.api.enums.OreMetal;
import com.extra.cosmerecraft.block.ModBlocks;
import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModConfiguredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, CosmereCraft.MODID);
    public static final List<RegistryObject<ConfiguredFeature<?, ?>>> configuredFeatureOres = new ArrayList<>();
    public static final List<String> configuredFeatureOreNames = new ArrayList<>();

    /*static {
        for(OreMetal metal: OreMetal.values()){
            final Supplier<List<OreConfiguration.TargetBlockState>> ORES;
            OreConfiguration.TargetBlockState deepslate = null;
            OreConfiguration.TargetBlockState stone = null;
            for (RegistryObject<DropExperienceBlock> block: ModBlocks.metalOreBlocks) {
                if(block.get().getName().contains(Component.literal(metal.getName()))){
                    if(block.get().getName().contains(Component.literal("deepslate"))) {
                        deepslate = OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, block.get().defaultBlockState());
                    }
                    else{
                        stone = OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, block.get().defaultBlockState());
                    }
                }
            }
            if(stone != null){
                OreConfiguration.TargetBlockState finalStone = stone;
                OreConfiguration.TargetBlockState finalDeepslate = deepslate;
                ORES = Suppliers.memoize(() -> List.of(finalStone, finalDeepslate));

                final RegistryObject<ConfiguredFeature<?, ?>> ORE = CONFIGURED_FEATURES.register(metal.getName() + "_ore", () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(ORES.get(), 70)));
                configuredFeatureOres.add(ORE);
                configuredFeatureOreNames.add(metal.getName() + "_ore");
            }
        }
    }*/


    public static void register(IEventBus eventBus){
        CONFIGURED_FEATURES.register(eventBus);
    }
}
