package com.extra.cosmerecraft.client.particle;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.client.particle.option.TinParticleOption;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ShriekParticleOption;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CosmereCraft.MODID);

    final static Function<ParticleType<TinParticleOption>, Codec<TinParticleOption>> tinParticleCodecFactory = (p_235904_) -> {
        return TinParticleOption.CODEC;
    };
    public static final RegistryObject<ParticleType<TinParticleOption>> TIN_PARTICLE = PARTICLE_TYPES.register("tin_particle", ()-> new ParticleType<>(true, TinParticleOption.DESERIALIZER) {
        @Override
        public Codec<TinParticleOption> codec() {
            return tinParticleCodecFactory.apply(this);
        }
    });

    public static void register(IEventBus eventBus){
        PARTICLE_TYPES.register(eventBus);
    }
}
