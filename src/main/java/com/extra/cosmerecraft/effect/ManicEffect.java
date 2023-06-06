package com.extra.cosmerecraft.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ManicEffect extends MobEffect {

    public ManicEffect(MobEffectCategory category, int color) {
        super(category, color);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "07d5e1fc-cc5c-47e6-af23-1aaa95b0f5b3", 0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.ATTACK_SPEED, "035f46bb-0165-406b-8926-875edf89c8e7", 0.3F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

}
