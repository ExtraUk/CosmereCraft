package com.extra.cosmerecraft.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FeruchemyPewterEffect extends MobEffect {

    public FeruchemyPewterEffect(MobEffectCategory category, int color) {
        super(category, color);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, "c3dc3437-3eaa-4aa1-a1e8-55db11e2c588", 3, AttributeModifier.Operation.ADDITION);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "716ab99e-0c45-476c-ae90-06be6c93005b", -0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
