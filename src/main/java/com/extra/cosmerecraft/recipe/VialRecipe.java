package com.extra.cosmerecraft.recipe;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.item.FlakeItem;
import com.extra.cosmerecraft.item.ModItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class VialRecipe extends CustomRecipe {
    public VialRecipe(ResourceLocation pId) {
        super(pId);
    }

    @Override
    public boolean matches(CraftingContainer pContainer, Level pLevel) {
        boolean hasVial = false;
        for(int i=0; i<pContainer.getContainerSize(); i++){
            ItemStack stack = pContainer.getItem(i);
            if(stack.isEmpty()) continue;
            else if(!hasVial && stack.getItem().equals(ModItems.VIAL.get())){
                hasVial = true;
            }
            else if(stack.getItem() instanceof FlakeItem) continue;
            else return false;
        }
        return hasVial;
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer) {
        int[] flakes = new int[Metal.values().length];
        ItemStack vial = new ItemStack(ModItems.VIAL.get());
        for(int i=0; i<pContainer.getContainerSize(); i++){
            ItemStack stack = pContainer.getItem(i);
            if(stack.getItem() instanceof FlakeItem){
                flakes[((FlakeItem)stack.getItem()).getMetal().getIndex()]++;
            }
            else if(stack.getItem().equals(ModItems.VIAL.get())) vial = stack.copy();
        }
        vial.setCount(1);
        for(Metal metal: Metal.values()){
            if(flakes[metal.getIndex()] > 0){
                vial.getOrCreateTag().putInt(metal.getName(), Math.min(vial.getOrCreateTag().getInt(metal.getName()) + flakes[metal.getIndex()], 16));
            }
        }
        return vial;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pHeight+pWidth >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.VIAL_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<VialRecipe> {

        @Override
        public VialRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            return new VialRecipe(pRecipeId);
        }

        @Override
        public @Nullable VialRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new VialRecipe(pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, VialRecipe pRecipe) {

        }
    }
}
