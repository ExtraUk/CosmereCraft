package com.extra.cosmerecraft.recipe;

import com.extra.cosmerecraft.CosmereCraft;
import net.minecraft.world.item.crafting.FireworkStarRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CosmereCraft.MODID);

    public static final RegistryObject<RecipeSerializer<AlloyingFurnaceRecipe>> ALLOYING_FURNACE_SERIALIZER = SERIALIZERS.register("alloying_furnace", () -> AlloyingFurnaceRecipe.Serializer.INSTANCE);
    public static final RegistryObject<VialRecipe.Serializer> VIAL_SERIALIZER = SERIALIZERS.register("vial_recipe", VialRecipe.Serializer::new);

    public static void register(IEventBus eventBus){
        SERIALIZERS.register(eventBus);
    }
}
