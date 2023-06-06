package com.extra.cosmerecraft.item;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.api.enums.Metal;
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

    static{
        for(Metal metal: Metal.values()){
            if(!metal.getName().equals("iron") && !metal.getName().equals("gold") && !metal.getName().equals("copper") && !metal.getName().equals("atium")) {
                ITEMS.register(metal.getName() + "_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
            }
        }
        final RegistryObject<Item> BISMUTH_INGOT = ITEMS.register("bismuth_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
        final RegistryObject<Item> NICKEL_INGOT = ITEMS.register("nickel_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
        final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));
        final RegistryObject<Item> ATIUM_BEAD = ITEMS.register("atium_bead", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB)));

        for(Metal metal: Metal.values()){
            ITEMS.register(metal.getName()+"_ring", () -> new MetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), metal, true));
            ITEMS.register(metal.getName()+"_bracelet", () -> new MetalmindItem(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB), metal, false));
        }
    }
    public static final RegistryObject<Item> BANDS_OF_MOURNING = ITEMS.register("bands_of_mourning", () -> new Item(new Item.Properties().tab(CosmereCraft.SCADRIAL_TAB).stacksTo(1)));
}
