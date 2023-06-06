package com.extra.cosmerecraft.loot;

import com.extra.cosmerecraft.CosmereCraft;
import com.mojang.serialization.Codec;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class LootModifierRegistry {

    public static void register(IEventBus eventBus){
        LOOT_MODIFIERS.register(eventBus);
    }

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, CosmereCraft.MODID);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> CHROMIUM_FORTUNE = LOOT_MODIFIERS.register("chromium_fortune", ChromiumFortuneModifier.CODEC);
}
