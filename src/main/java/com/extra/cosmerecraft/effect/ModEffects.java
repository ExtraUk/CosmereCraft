package com.extra.cosmerecraft.effect;

import com.extra.cosmerecraft.CosmereCraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CosmereCraft.MODID);

    public static final RegistryObject<MobEffect> HEAVY = MOB_EFFECTS.register("heavy_effect", () -> new HeavyEffect(MobEffectCategory.NEUTRAL, 0x63636));
    public static final RegistryObject<MobEffect> DEPRESSED = MOB_EFFECTS.register("depressed_effect", () -> new DepressedEffect(MobEffectCategory.HARMFUL, 0x191e47));
    public static final RegistryObject<MobEffect> MANIC = MOB_EFFECTS.register("manic_effect", () -> new ManicEffect(MobEffectCategory.BENEFICIAL, 0xf0be41));
    public static final RegistryObject<MobEffect> YOUNG = MOB_EFFECTS.register("young_effect", () -> new YoungEffect(MobEffectCategory.BENEFICIAL, 0xe1f28a));
    public static final RegistryObject<MobEffect> OLD = MOB_EFFECTS.register("old_effect", () -> new OldEffect(MobEffectCategory.HARMFUL, 0x511a99));

    public static void register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }
}
