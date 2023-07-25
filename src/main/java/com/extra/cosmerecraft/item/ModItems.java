package com.extra.cosmerecraft.item;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.api.enums.OreMetal;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CosmereCraft.MODID);

    public static ArrayList<RegistryObject<Item>> rawOres = new ArrayList<RegistryObject<Item>>();
    public static ArrayList<RegistryObject<Item>> metalIngots = new ArrayList<RegistryObject<Item>>();

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

    static{
        for(OreMetal metal: OreMetal.values()){
            rawOres.add(ITEMS.register("raw_" + metal.getName(), () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB))));
        }

        for(Metal metal: Metal.values()){
            ITEMS.register(metal.getName()+"_ring", () -> new MetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), metal, true));
            ITEMS.register(metal.getName()+"_bracelet", () -> new MetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), metal, false));
        }
    }

    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> TIN_INGOT = ITEMS.register("tin_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> PEWTER_INGOT = ITEMS.register("pewter_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> ZINC_INGOT = ITEMS.register("zinc_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> BRASS_INGOT = ITEMS.register("brass_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> BRONZE_INGOT = ITEMS.register("bronze_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> ALUMINUM_INGOT = ITEMS.register("aluminum_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> DURALUMIN_INGOT = ITEMS.register("duralumin_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> CHROMIUM_INGOT = ITEMS.register("chromium_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> NICROSIL_INGOT = ITEMS.register("nicrosil_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> ELECTRUM_INGOT = ITEMS.register("electrum_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> CADMIUM_INGOT = ITEMS.register("cadmium_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> BENDALLOY_INGOT = ITEMS.register("bendalloy_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));

    public static final RegistryObject<Item> BISMUTH_INGOT = ITEMS.register("bismuth_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> NICKEL_INGOT = ITEMS.register("nickel_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));

    public static final RegistryObject<Item> ATIUM_BEAD = ITEMS.register("atium_bead", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> BANDS_OF_MOURNING = ITEMS.register("bands_of_mourning", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB).stacksTo(1)));
}
