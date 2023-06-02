package com.extra.cosmerecraft;

import com.extra.cosmerecraft.client.KeyBindings;
import com.extra.cosmerecraft.command.CommandSetup;
import com.extra.cosmerecraft.command.FeruchemyPowerType;
import com.extra.cosmerecraft.command.PowerCommand;
import com.extra.cosmerecraft.event.ClientEvents;
import com.extra.cosmerecraft.event.CommonEventHandler;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import com.extra.cosmerecraft.item.ModItems;
import com.extra.cosmerecraft.network.ModMessages;
import com.google.common.graph.Network;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(CosmereCraft.MODID)
public class CosmereCraft
{
    public static final String MODID = "cosmerecraft";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CosmereCraft()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(CosmereCraft::init);
        modEventBus.addListener(FeruchemistCapability::registerCapability);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(CommandSetup::registerCommands);
        CommandSetup.register();

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    public void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().size(12).build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BRACELET.getMessageBuilder().size(4).build());
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MinecraftForge.EVENT_BUS.register(new ClientEvents());
        }
    }

    public static final CreativeModeTab SCADRIAL_TAB = new CreativeModeTab("scadrial_tab") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return Items.IRON_INGOT.getDefaultInstance();
        }
    };

    public static void init(final FMLCommonSetupEvent e) {
        e.enqueueWork(ModMessages::registerPackets);
        e.enqueueWork(() -> {
            MinecraftForge.EVENT_BUS.register(CommonEventHandler.class);
        });
    }
}
