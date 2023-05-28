package com.extra.cosmerecraft.item;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.item.metalminds.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CosmereCraft.MODID);

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> TIN_INGOT = ITEMS.register("tin_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> PEWTER_INGOT = ITEMS.register("pewter_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> BRONZE_INGOT = ITEMS.register("bronze_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> ELECTRUM_INGOT = ITEMS.register("electrum_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> ZINC_INGOT = ITEMS.register("zinc_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> BRASS_INGOT = ITEMS.register("brass_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> CHROMIUM_INGOT = ITEMS.register("chromium_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> NICROSIL_INGOT = ITEMS.register("nicrosil_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> CADMIUM_INGOT = ITEMS.register("cadmium_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> BENDALLOY_INGOT = ITEMS.register("bendalloy_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> ALUMINUM_INGOT = ITEMS.register("aluminum_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> DURALUMIN_INGOT = ITEMS.register("duralumin_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> BISMUTH_INGOT = ITEMS.register("bismuth_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> NICKEL_INGOT = ITEMS.register("nickel_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));

    public static final RegistryObject<Item> IRON_RING = ITEMS.register("iron_ring", () -> new IronMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> STEEL_RING = ITEMS.register("steel_ring", () -> new SteelMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> TIN_RING = ITEMS.register("tin_ring", () -> new TinMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> PEWTER_RING = ITEMS.register("pewter_ring", () -> new PewterMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> ZINC_RING = ITEMS.register("zinc_ring", () -> new ZincMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> BRASS_RING = ITEMS.register("brass_ring", () -> new BrassMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> GOLD_RING = ITEMS.register("gold_ring", () -> new GoldMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> ELECTRUM_RING = ITEMS.register("electrum_ring", () -> new ElectrumMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> CHROMIUM_RING = ITEMS.register("chromium_ring", () -> new ChromiumMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> NICROSIL_RING = ITEMS.register("nicrosil_ring", () -> new NicrosilMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> CADMIUM_RING = ITEMS.register("cadmium_ring", () -> new CadmiumMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> BENDALLOY_RING = ITEMS.register("bendalloy_ring", () -> new BendalloyMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> COPPER_RING = ITEMS.register("copper_ring", () -> new CopperMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> BRONZE_RING = ITEMS.register("bronze_ring", () -> new BronzeMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> ALUMINUM_RING = ITEMS.register("aluminum_ring", () -> new AluminumMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> DURALUMIN_RING = ITEMS.register("duralumin_ring", () -> new DuraluminMetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
}
