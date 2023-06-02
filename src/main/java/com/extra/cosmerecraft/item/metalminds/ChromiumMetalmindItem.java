package com.extra.cosmerecraft.item.metalminds;

import com.extra.cosmerecraft.item.MetalmindItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class ChromiumMetalmindItem extends MetalmindItem {
    public ChromiumMetalmindItem(Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        slotContext.entity().addEffect(new MobEffectInstance(MobEffects.LUCK, 19, 0, false, false, true));
    }
}
