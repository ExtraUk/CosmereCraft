package com.extra.cosmerecraft.item;

import com.extra.cosmerecraft.api.enums.Metal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class FlakeItem extends Item {
    Metal metal;
    public FlakeItem(Properties pProperties, Metal pMetal) {
        super(pProperties);
        this.metal = pMetal;
    }

    public Metal getMetal(){
        return this.metal;
    }
}
