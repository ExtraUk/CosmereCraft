package com.extra.cosmerecraft.event;

import com.extra.cosmerecraft.CosmereCraft;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collections;
import java.util.List;

public class LootTableInjector {
    @SubscribeEvent
    public static void onLootLoad(final LootTableLoadEvent evt) {
        if (evt.getName().toString().contains("minecraft:chests/") && !evt.getName().toString().contains("village")) {
            evt.getTable().addPool(LootPool.lootPool().name("lerasium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/lerasium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("lerasferium_inject")
                .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/lerasferium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("lerascarboferium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/lerascarboferium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("lerastinium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/lerastinium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("lerastincupium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/lerastincupium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("leraszincium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/leraszincium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("leraszincupium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/leraszincupium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("lerascupium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/lerascupium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("lerascuptinium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/lerascuptinium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("lerasaurium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/lerasaurium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("lerasargaurium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/lerasargaurium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("lerascadium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/lerascadium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("lerasbicadium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/lerasbicadium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("lerasalium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/lerasalium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("lerasalcupium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/lerasalcupium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("leraschrium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/leraschrium"))).build());
            evt.getTable().addPool(LootPool.lootPool().name("lerasnichrium_inject")
                    .add(LootTableReference.lootTableReference(new ResourceLocation(CosmereCraft.MODID, "inject/lerasnichrium"))).build());
        }
    }
}
