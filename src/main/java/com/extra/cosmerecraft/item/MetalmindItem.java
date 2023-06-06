package com.extra.cosmerecraft.item;

import com.extra.cosmerecraft.api.enums.Metal;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.*;

public class MetalmindItem extends Item implements ICurioItem {

    private int maxCharge;
    private Metal metal;

    public Metal getMetal(){
        return this.metal;
    }

    public int getMaxCharge(){
        return this.maxCharge;
    }

    public MetalmindItem(Properties properties, Metal metal, boolean isRing) {
        super(properties.stacksTo(1));
        this.metal = metal;
        setMaxCharge(metal, isRing);
    }

    public void setMaxCharge(Metal metal, boolean isRing){
        if(metal == Metal.COPPER){
            if(isRing){
                this.maxCharge = 1400;
            }
            else{
                this.maxCharge = 10000;
            }
        }
        else {
            if(isRing){
                this.maxCharge = 24000;
            }
            else{
                this.maxCharge = 168000;
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(this.metal == Metal.COPPER){
            components.add(Component.literal("Charges: " + stack.getOrCreateTag().getInt("charges")));
        }
        else{
            components.add(Component.literal("Charges: " + stack.getOrCreateTag().getInt("charges")/20));
        }
        String key = stack.getOrCreateTag().getString("key");
        if(key.equals("null")){
            components.add(Component.literal("Key: " + "Unkeyed"));
        }
        else if(!key.equals("")){
            components.add(Component.literal("Key: " + key));
        }

        super.appendHoverText(stack, level, components, flag);
    }


}
