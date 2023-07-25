package com.extra.cosmerecraft.block.entity;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CosmereCraft.MODID);

    public static final RegistryObject<BlockEntityType<AlloyingFurnaceBlockEntity>> ALLOYING_FURNACE = BLOCK_ENTITIES.register("alloying_furnace", () -> BlockEntityType.Builder.of(AlloyingFurnaceBlockEntity::new, ModBlocks.ALLOYING_FURNACE_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
