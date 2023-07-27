package com.extra.cosmerecraft.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class HeavyEffect extends MobEffect {

    public HeavyEffect(MobEffectCategory category, int color) {
        super(category, color);
        addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "9bc68572-0100-11ee-be56-0242ac120002", 0.2, AttributeModifier.Operation.ADDITION);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "53463080-5a3f-44c0-be18-c34ba0402509", -0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier){
        if(entity.isInFluidType()){
            entity.setDeltaMovement(entity.getDeltaMovement().subtract(0,(amplifier+1)/50.0,0));
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
