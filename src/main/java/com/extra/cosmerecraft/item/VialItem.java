package com.extra.cosmerecraft.item;

import com.extra.cosmerecraft.allomancy.data.AllomancerCapability;
import com.extra.cosmerecraft.api.enums.Metal;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class VialItem extends Item {

    public VialItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public boolean useOnRelease(ItemStack pStack) {
        return super.useOnRelease(pStack);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 16;
    }

    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        super.finishUsingItem(pStack, pLevel, pEntityLiving);
        if (!pLevel.isClientSide) {
            chargeMetals(pStack, (ServerPlayer) pEntityLiving);
            if (pStack.isEmpty()) {
                return new ItemStack(ModItems.VIAL.get());
            } else {
                if (pEntityLiving instanceof Player && !((Player) pEntityLiving).getAbilities().instabuild) {
                    ItemStack itemstack = new ItemStack(ModItems.VIAL.get());
                    Player player = (Player) pEntityLiving;
                    if (!player.getInventory().add(itemstack)) {
                        player.drop(itemstack, false);
                    }
                    pStack.shrink(1);
                }
            }
        }
        return pStack;
    }

    public void chargeMetals(ItemStack pStack, ServerPlayer player){
        player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(data -> {
            for(Metal metal : Metal.values()){
                data.setMetalReserves(metal, Math.min(data.getMetalMaxReserves(metal), data.getMetalReserves(metal) + pStack.getOrCreateTag().getInt(metal.getName())*1500));
            }
        });
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        for(Metal metal : Metal.values()){
            int metalCharge = stack.getOrCreateTag().getInt(metal.getName());
            if(metalCharge > 0){
                components.add(Component.translatable("metals.cosmerecraft."+metal.getName()).append(": "+metalCharge*75));
            }
        }

        super.appendHoverText(stack, level, components, flag);
    }

    public static class VialHasMetalsPropertyFunction implements ItemPropertyFunction{
        @Override
        public float call(ItemStack pStack, @org.jetbrains.annotations.Nullable ClientLevel pLevel, @org.jetbrains.annotations.Nullable LivingEntity pEntity, int pSeed) {
            for(Metal metal : Metal.values()){
                if(pStack.getOrCreateTag().getInt(metal.getName()) > 0) return 1;
            }
            return 0;
        }
    }
}
