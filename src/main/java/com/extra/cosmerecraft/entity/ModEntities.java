package com.extra.cosmerecraft.entity;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.entity.custom.ShadowEntity;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CosmereCraft.MODID);

    public static final RegistryObject<EntityType<ShadowEntity>> SHADOW_ENTITY = ENTITY_TYPES.register("shadow", () ->
            EntityType.Builder.of(ShadowEntity::new, MobCategory.MISC)
                    .sized(1f,1.8f)
                    .build(new ResourceLocation(CosmereCraft.MODID, "shadow").toString()));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
