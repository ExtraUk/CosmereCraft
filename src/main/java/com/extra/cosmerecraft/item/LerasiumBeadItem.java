package com.extra.cosmerecraft.item;

import com.extra.cosmerecraft.api.enums.Metal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LerasiumBeadItem extends Item {
    Metal metal;
    boolean allMetals;

    public LerasiumBeadItem(Properties pProperties, @Nullable Metal metal, boolean allMetals) {
        super(pProperties.stacksTo(16));
        this.metal = metal;
        this.allMetals = allMetals;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.EAT;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 16;
    }

    public boolean isPureLerasium(){
        return this.allMetals;
    }

    public Metal getMetal(){
        return this.metal;
    }
}
