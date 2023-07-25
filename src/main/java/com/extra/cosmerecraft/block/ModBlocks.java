package com.extra.cosmerecraft.block;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.api.enums.OreMetal;
import com.extra.cosmerecraft.item.ModItems;
import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static net.minecraft.world.level.block.Blocks.AMETHYST_CLUSTER;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CosmereCraft.MODID);
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, CosmereCraft.MODID);
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, CosmereCraft.MODID);

    private static final List<PlacementModifier> highPlacement = List.of(CountPlacement.of(7), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(32), VerticalAnchor.aboveBottom(128)), BiomeFilter.biome());
    private static final List<PlacementModifier> lowPlacement = List.of(CountPlacement.of(7), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(96)), BiomeFilter.biome());


    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
        CONFIGURED_FEATURES.register(eventBus);
        PLACED_FEATURES.register(eventBus);
    }
    public static List<RegistryObject<Block>> metalOreBlocks = new ArrayList<>();

    static{
        for(OreMetal metal: OreMetal.values()){ //Ore blocks + WorldGen
            String name = metal.getName();
            final RegistryObject<Block> block = BLOCKS.register(name + "_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
            metalOreBlocks.add(block);
            ModItems.ITEMS.register(name + "_ore", () -> new BlockItem(block.get(), new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
            final RegistryObject<Block>  block1 = BLOCKS.register("deepslate_" + name + "_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));
            metalOreBlocks.add(block1);
            ModItems.ITEMS.register( "deepslate_" + name + "_ore", () -> new BlockItem(block1.get(), new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));

            final Supplier<List<OreConfiguration.TargetBlockState>> ORES = Suppliers.memoize(() -> List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, block.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, block1.get().defaultBlockState())));
            final RegistryObject<ConfiguredFeature<?, ?>> ORE = CONFIGURED_FEATURES.register(name + "_ore",
                    () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(ORES.get(),7)));
            if(name.equals("tin") || name.equals("zinc") || name.equals("nickel") || name.equals("bauxite")) {
                final RegistryObject<PlacedFeature> ORE_PLACED = PLACED_FEATURES.register(name + "_ore_placed",
                        () -> new PlacedFeature(ORE.getHolder().get(),
                                List.of(CountPlacement.of(7), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(32), VerticalAnchor.aboveBottom(128)), BiomeFilter.biome())));
            }
            else{
                final RegistryObject<PlacedFeature> ORE_PLACED = PLACED_FEATURES.register(name + "_ore_placed",
                        () -> new PlacedFeature(ORE.getHolder().get(),
                                List.of(CountPlacement.of(5), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(96)), BiomeFilter.biome())));
            }
        }
    }
    public static final RegistryObject<Block> ATIUM_GEODE_BLOCK = BLOCKS.register("atium_geode_block", () -> new AmethystBlock(BlockBehaviour.Properties.of(Material.AMETHYST, MaterialColor.COLOR_GREEN).strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> ALLOYING_FURNACE_BLOCK = BLOCKS.register("alloying_furnace", () -> new AlloyingFurnaceBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).lightLevel(litBlockEmission(13))));
    public static final RegistryObject<Block> BUDDING_ATIUM_BLOCK = BLOCKS.register("budding_atium_block", () -> new BuddingAtiumBlock(BlockBehaviour.Properties.of(Material.AMETHYST).randomTicks().strength(1.5F).sound(SoundType.AMETHYST).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> ATIUM_CLUSTER = BLOCKS.register("atium_cluster", () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.of(Material.AMETHYST).noOcclusion().randomTicks().sound(SoundType.AMETHYST_CLUSTER).strength(1.5F).lightLevel((p_152632_) -> {return 7;})));
    public static final RegistryObject<Block> SMALL_ATIUM_BUD = BLOCKS.register("small_atium_bud", () -> new AmethystClusterBlock(3, 4, BlockBehaviour.Properties.copy(AMETHYST_CLUSTER).sound(SoundType.SMALL_AMETHYST_BUD).lightLevel((cuack) -> {return 1;})));
    public static final RegistryObject<Block> MEDIUM_ATIUM_BUD = BLOCKS.register("medium_atium_bud", () -> new AmethystClusterBlock(4, 3, BlockBehaviour.Properties.copy(AMETHYST_CLUSTER).sound(SoundType.MEDIUM_AMETHYST_BUD).lightLevel((cuack) -> {return 2;})));
    public static final RegistryObject<Block> LARGE_ATIUM_BUD = BLOCKS.register("large_atium_bud", () -> new AmethystClusterBlock(5, 3, BlockBehaviour.Properties.copy(AMETHYST_CLUSTER).sound(SoundType.LARGE_AMETHYST_BUD).lightLevel((cuack) -> {return 3;})));


    public static final RegistryObject<Item> ATIUM_GEODE_BLOCK_ITEM = ModItems.ITEMS.register("atium_geode_block", () -> new BlockItem(ATIUM_GEODE_BLOCK.get(), new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> BUDDING_ATIUM_BLOCK_ITEM = ModItems.ITEMS.register("budding_atium_block", () -> new BlockItem(BUDDING_ATIUM_BLOCK.get(), new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> ALLOYING_FURNACE_BLOCK_ITEM = ModItems.ITEMS.register("alloying_furnace", () -> new BlockItem(ALLOYING_FURNACE_BLOCK.get(), new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));


    private static ToIntFunction<BlockState> litBlockEmission(int pLightValue) {
        return (p_50763_) -> {
            return p_50763_.getValue(BlockStateProperties.LIT) ? pLightValue : 0;
        };
    }
}
