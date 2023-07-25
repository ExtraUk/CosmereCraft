package com.extra.cosmerecraft.integration;


import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.block.ModBlocks;
import com.extra.cosmerecraft.recipe.AlloyingFurnaceRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.SlotItemHandler;

public class AlloyingFurnaceRecipeCategory implements IRecipeCategory<AlloyingFurnaceRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(CosmereCraft.MODID, "alloying_furnace");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(CosmereCraft.MODID, "textures/gui/alloying_furnace_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public AlloyingFurnaceRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.ALLOYING_FURNACE_BLOCK.get()));
    }

    @Override
    public RecipeType<AlloyingFurnaceRecipe> getRecipeType() {
        return JEICosmereCraftPlugin.ALLOYING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.alloying_furnace");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlloyingFurnaceRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 36, 9).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 54, 9).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 36, 27).addIngredients(recipe.getIngredients().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 54, 27).addIngredients(recipe.getIngredients().get(3));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 117, 36).addItemStack(recipe.getResultItem());
    }
}
