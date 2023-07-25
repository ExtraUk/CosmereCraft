package com.extra.cosmerecraft.integration;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.recipe.AlloyingFurnaceRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEICosmereCraftPlugin implements IModPlugin {

    public static RecipeType<AlloyingFurnaceRecipe> ALLOYING_TYPE = new RecipeType<>(AlloyingFurnaceRecipeCategory.UID, AlloyingFurnaceRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(CosmereCraft.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new AlloyingFurnaceRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<AlloyingFurnaceRecipe> recipesAlloying = rm.getAllRecipesFor(AlloyingFurnaceRecipe.Type.INSTANCE);
        registration.addRecipes(ALLOYING_TYPE, recipesAlloying);
    }
}
