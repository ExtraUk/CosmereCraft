package com.extra.cosmerecraft.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class OldEffect extends MobEffect {

    public OldEffect(MobEffectCategory category, int color) {
        super(category, color);
        addAttributeModifier(Attributes.MAX_HEALTH, "fb58bc51-3f4d-4b1a-8ec8-661aa8a1127a", -6, AttributeModifier.Operation.ADDITION);
        addAttributeModifier(Attributes.ATTACK_SPEED, "cdb26f8a-1a94-4a70-a190-b3c452c2caf1", (double)-0.3F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "13545545-638a-44ac-851c-3f1ad3f1ac97", (double)-0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

}
