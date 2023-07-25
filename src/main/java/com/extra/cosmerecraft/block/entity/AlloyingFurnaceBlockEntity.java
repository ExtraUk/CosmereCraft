package com.extra.cosmerecraft.block.entity;

import com.extra.cosmerecraft.api.enums.AllMetal;
import com.extra.cosmerecraft.block.AlloyingFurnaceBlock;
import com.extra.cosmerecraft.client.gui.AlloyingFurnaceMenu;
import com.extra.cosmerecraft.item.ModItems;
import com.extra.cosmerecraft.recipe.AlloyingFurnaceRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AlloyingFurnaceBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(6){
        @Override
        protected void onContentsChanged(int slot){
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private final int MAX_PROGRESS = 200;
    private int fuelRemaining = 0;
    private int maxFuel = 0;

    public AlloyingFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALLOYING_FURNACE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0 -> AlloyingFurnaceBlockEntity.this.progress;
                    case 1 -> AlloyingFurnaceBlockEntity.this.MAX_PROGRESS;
                    case 2 -> AlloyingFurnaceBlockEntity.this.fuelRemaining;
                    case 3 -> AlloyingFurnaceBlockEntity.this.maxFuel;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AlloyingFurnaceBlockEntity.this.progress = value;
                    case 2 -> AlloyingFurnaceBlockEntity.this.fuelRemaining = value;
                    default -> AlloyingFurnaceBlockEntity.this.progress = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockstate.alloying_furnace");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new AlloyingFurnaceMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.putInt("alloying_furnace.progress", this.progress);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.progress = nbt.getInt("alloying_furnace.progress");
    }

    public void dropContents(){
        SimpleContainer inv = new SimpleContainer(itemStackHandler.getSlots());
        for(int i=0; i < itemStackHandler.getSlots(); i++){
            inv.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, AlloyingFurnaceBlockEntity pEntity) {
        if(level.isClientSide()){
            return;
        }

        updateFuel(pEntity);

        if(hasRecipe(pEntity) && hasFuel(pEntity)){
            startFuel(pEntity);
            pEntity.progress++;
            setChanged(level, blockPos, state);
            level.setBlock(blockPos, state.setValue(AlloyingFurnaceBlock.LIT, true), 3);


            if(pEntity.progress >= pEntity.MAX_PROGRESS){
                craftItem(pEntity);
                pEntity.resetProgress();
            }
        }
        else{
            pEntity.resetProgress();
            level.setBlock(blockPos, state.setValue(AlloyingFurnaceBlock.LIT, false), 3);
            setChanged(level, blockPos, state);
        }

    }

    private static void startFuel(AlloyingFurnaceBlockEntity pEntity) {
        if(ForgeHooks.getBurnTime(pEntity.itemStackHandler.getStackInSlot(0), RecipeType.SMELTING) > 0) {
            if (pEntity.fuelRemaining <= 0) {
                pEntity.fuelRemaining = ForgeHooks.getBurnTime(pEntity.itemStackHandler.getStackInSlot(0), RecipeType.SMELTING);
                pEntity.maxFuel = pEntity.fuelRemaining;
                pEntity.itemStackHandler.extractItem(0, 1, false);
            }
        }
    }

    private static void updateFuel(AlloyingFurnaceBlockEntity pEntity) {
        if (pEntity.fuelRemaining > 0){
            pEntity.fuelRemaining--;
        }
    }

    private static boolean hasFuel(AlloyingFurnaceBlockEntity pEntity) {
        if(pEntity.fuelRemaining <= 0){
            return ForgeHooks.getBurnTime(pEntity.itemStackHandler.getStackInSlot(0), RecipeType.SMELTING) > 0;
        }
        else{
            return true;
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(AlloyingFurnaceBlockEntity pEntity) {
        Level level = pEntity.level;
        SimpleContainer inv = new SimpleContainer(pEntity.itemStackHandler.getSlots());
        for(int i=2; i<pEntity.itemStackHandler.getSlots(); i++){
            inv.setItem(i, pEntity.itemStackHandler.getStackInSlot(i));
        }
        Optional<AlloyingFurnaceRecipe> recipe = level.getRecipeManager().getRecipeFor(AlloyingFurnaceRecipe.Type.INSTANCE, inv, level);
        if(hasRecipe(pEntity)){
            pEntity.itemStackHandler.extractItem(2,1,false);
            pEntity.itemStackHandler.extractItem(3,1,false);
            pEntity.itemStackHandler.extractItem(4,1,false);
            pEntity.itemStackHandler.extractItem(5,1,false);
            pEntity.itemStackHandler.setStackInSlot(1, new ItemStack(recipe.get().getResultItem().getItem(), pEntity.itemStackHandler.getStackInSlot(1).getCount() + recipe.get().getResultItem().getCount()));
        }
    }

    private static boolean hasRecipe(AlloyingFurnaceBlockEntity pEntity) {
        Level level = pEntity.level;
        SimpleContainer ingredients = new SimpleContainer(pEntity.itemStackHandler.getSlots()-2);
        for(int i=2; i<pEntity.itemStackHandler.getSlots(); i++){
            ingredients.setItem(i-2, pEntity.itemStackHandler.getStackInSlot(i));
        }
        SimpleContainer inv = new SimpleContainer(pEntity.itemStackHandler.getSlots());
        for(int i=0; i<pEntity.itemStackHandler.getSlots(); i++){
            inv.setItem(i, pEntity.itemStackHandler.getStackInSlot(i));
        }
        Optional<AlloyingFurnaceRecipe> recipe = level.getRecipeManager().getRecipeFor(AlloyingFurnaceRecipe.Type.INSTANCE, ingredients, level);

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inv, recipe.get().getResultItem().getCount()) && canInsertItemIntoOutputSlot(inv, recipe.get().getResultItem());
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inv, ItemStack stack) {
        return inv.getItem(1).getItem().equals(stack.getItem()) || inv.getItem(1).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inv, int i) {
        return (inv.getItem(1).getMaxStackSize() >= inv.getItem(1).getCount() + i) || inv.getItem(1).isEmpty();
    }
}
