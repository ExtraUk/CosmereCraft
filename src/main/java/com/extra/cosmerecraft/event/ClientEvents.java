package com.extra.cosmerecraft.event;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.allomancy.data.AllomancerCapability;
import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.client.KeyBindings;
import com.extra.cosmerecraft.client.gui.AllomancySelectScreen;
import com.extra.cosmerecraft.client.gui.FeruchemySelectScreen;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.animal.Cow;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientEvents {

    /*@Mod.EventBusSubscriber(modid = CosmereCraft.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents{
    }*/
    private final Minecraft mc = Minecraft.getInstance();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event){
        if(KeyBindings.FERUCHEMY_MENU_KEY.isDown()){
            if(this.mc.screen instanceof FeruchemySelectScreen){
                this.mc.setScreen(null);
            }
            else {
                this.mc.setScreen(new FeruchemySelectScreen());
            }
        }
       /* else if(KeyBindings.ALLOMANCY_MENU_KEY.isDown()){
            if(this.mc.screen instanceof AllomancySelectScreen){
                this.mc.setScreen(null);
            }
            else {
                this.mc.setScreen(new AllomancySelectScreen());
            }
        }*/

    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre event){
        if(this.mc.player != null) {
            CompoundTag nbt = event.getEntity().serializeNBT();
            if (nbt.get("player_UUID") != null) {
                if (nbt.getUUID("player_UUID").equals(this.mc.player.getUUID())) {
                    AtomicBoolean cancel = new AtomicBoolean(true);
                    this.mc.player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(data -> {
                        if (data.isBurning(Metal.GOLD)) {
                            cancel.set(false);
                        }
                    });
                    event.setCanceled(cancel.get());
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderFog(ViewportEvent.RenderFog event){
        this.mc.player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
            int tl = data.tappingLevel(Metal.TIN);
            if(tl < 0){
                event.setFogShape(FogShape.SPHERE);
                event.setNearPlaneDistance(0);
                event.setFarPlaneDistance(60+(20*tl));
                event.setCanceled(true);
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderFOV(ViewportEvent.ComputeFov event){
        this.mc.player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
            if(data.tappingLevel(Metal.TIN) > 0){
                event.setFOV(event.getFOV()/(3* data.tappingLevel(Metal.TIN)));
            }
        });
    }


    @Mod.EventBusSubscriber(modid = CosmereCraft.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents{
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event){
            event.register(KeyBindings.FERUCHEMY_MENU_KEY);
            //event.register(KeyBindings.ALLOMANCY_MENU_KEY);
        }
    }
}
