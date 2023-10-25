package com.extra.cosmerecraft.item;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.api.enums.OreMetal;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
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

    public static final RegistryObject<Item> BISMUTH_INGOT = ITEMS.register("bismuth_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> NICKEL_INGOT = ITEMS.register("nickel_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));

    public static final RegistryObject<Item> ATIUM_BEAD = ITEMS.register("atium_bead", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
    public static final RegistryObject<Item> BANDS_OF_MOURNING = ITEMS.register("bands_of_mourning", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB).stacksTo(1)));

    public static final RegistryObject<Item> LERASIUM_BEAD = ITEMS.register("lerasium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), null, true));
    public static final RegistryObject<Item> LERASFERIUM_BEAD = ITEMS.register("lerasferium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.IRON, false));
    public static final RegistryObject<Item> LERASCARBOFERIUM_BEAD = ITEMS.register("lerascarboferium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.STEEL, false));
    public static final RegistryObject<Item> LERASTINIUM_BEAD = ITEMS.register("lerastinium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.TIN, false));
    public static final RegistryObject<Item> LERASTINCUPIUM_BEAD = ITEMS.register("lerastincupium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.PEWTER, false));
    public static final RegistryObject<Item> LERASZINCIUM_BEAD = ITEMS.register("leraszincium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.ZINC, false));
    public static final RegistryObject<Item> LERASZINCUPIUM_BEAD = ITEMS.register("leraszincupium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.BRASS, false));
    public static final RegistryObject<Item> LERASCUPIUM_BEAD = ITEMS.register("lerascupium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.COPPER, false));
    public static final RegistryObject<Item> LERASCUPTINIUM_BEAD = ITEMS.register("lerascuptinium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.BRONZE, false));
    public static final RegistryObject<Item> LERASALIUM_BEAD = ITEMS.register("lerasalium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.ALUMINUM, false));
    public static final RegistryObject<Item> LERASALCUPIUM_BEAD = ITEMS.register("lerasalcupium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.DURALUMIN, false));
    public static final RegistryObject<Item> LERASCHRIUM_BEAD = ITEMS.register("leraschrium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.CHROMIUM, false));
    public static final RegistryObject<Item> LERASNICHRIUM_BEAD = ITEMS.register("lerasnichrium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.NICROSIL, false));
    public static final RegistryObject<Item> LERASAURIUM_BEAD = ITEMS.register("lerasaurium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.GOLD, false));
    public static final RegistryObject<Item> LERASARGAURIUM_BEAD = ITEMS.register("lerasargaurium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.ELECTRUM, false));
    public static final RegistryObject<Item> LERASCADIUM_BEAD = ITEMS.register("lerascadium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.CADMIUM, false));
    public static final RegistryObject<Item> LERASBICADIUM_BEAD = ITEMS.register("lerasbicadium_bead", () -> new LerasiumBeadItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), Metal.BENDALLOY, false));

    public static final RegistryObject<Item> VIAL = ITEMS.register("vial", () -> new VialItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB).stacksTo(16)));

    static{
        for(OreMetal metal: OreMetal.values()){
            rawOres.add(ITEMS.register("raw_" + metal.getName(), () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB))));
        }

        for(Metal metal: Metal.values()){
            ITEMS.register(metal.getName()+"_ring", () -> new MetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), metal, true));
            ITEMS.register(metal.getName()+"_bracelet", () -> new MetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), metal, false));
            if(!metal.equals(Metal.ATIUM)) {
                if(!metal.isVanilla()) {
                    ITEMS.register(metal.getName() + "_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
                }
                ITEMS.register(metal.getName()+"_flakes", () -> new FlakeItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), metal));
            }
        }
    }
}
