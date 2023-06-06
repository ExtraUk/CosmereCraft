package com.extra.cosmerecraft.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class YoungEffect extends MobEffect {

    public YoungEffect(MobEffectCategory category, int color) {
        super(category, color);
        addAttributeModifier(Attributes.MAX_HEALTH, "3859ca33-36ca-4542-9999-48af245e131c", 6, AttributeModifier.Operation.ADDITION);
        addAttributeModifier(Attributes.ATTACK_SPEED, "b5fd731b-7099-42ef-b56a-52b9f35bca7f", (double)0.5F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "d4ec715f-a823-4dac-a877-18c41c349e00", (double)0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

}