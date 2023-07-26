package com.extra.cosmerecraft.event;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.client.KeyBindings;
import com.extra.cosmerecraft.client.gui.AllomancySelectScreen;
import com.extra.cosmerecraft.client.gui.FeruchemySelectScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
        else if(KeyBindings.ALLOMANCY_MENU_KEY.isDown()){
            if(this.mc.screen instanceof AllomancySelectScreen){
                this.mc.setScreen(null);
            }
            else {
                this.mc.setScreen(new AllomancySelectScreen());
            }
        }

    }
    @Mod.EventBusSubscriber(modid = CosmereCraft.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents{
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event){
            event.register(KeyBindings.FERUCHEMY_MENU_KEY);
            event.register(KeyBindings.ALLOMANCY_MENU_KEY);
        }
    }
}
