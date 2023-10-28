package com.extra.cosmerecraft.event;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.client.particle.ModParticles;
import com.extra.cosmerecraft.client.particle.TinParticle;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CosmereCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event){
        Minecraft.getInstance().particleEngine.register(ModParticles.TIN_PARTICLE.get(), TinParticle.Provider::new);
    }
}
