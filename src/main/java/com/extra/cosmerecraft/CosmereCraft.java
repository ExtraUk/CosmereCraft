package com.extra.cosmerecraft;

import com.extra.cosmerecraft.allomancy.data.AllomancerCapability;
import com.extra.cosmerecraft.block.ModBlocks;
import com.extra.cosmerecraft.block.entity.ModBlockEntities;
import com.extra.cosmerecraft.client.KeyBindings;
import com.extra.cosmerecraft.client.gui.AlloyingFurnaceScreen;
import com.extra.cosmerecraft.client.gui.ModMenuTypes;
import com.extra.cosmerecraft.command.CommandSetup;
import com.extra.cosmerecraft.command.FeruchemyPowerType;
import com.extra.cosmerecraft.command.PowerCommand;
import com.extra.cosmerecraft.effect.ModEffects;
import com.extra.cosmerecraft.entity.ModEntities;
import com.extra.cosmerecraft.entity.client.ShadowModel;
import com.extra.cosmerecraft.entity.client.ShadowRenderer;
import com.extra.cosmerecraft.entity.custom.ShadowEntity;
import com.extra.cosmerecraft.event.ClientEvents;
import com.extra.cosmerecraft.event.CommonEventHandler;
import com.extra.cosmerecraft.event.LootTableInjector;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import com.extra.cosmerecraft.item.ModItems;
import com.extra.cosmerecraft.item.VialItem;
import com.extra.cosmerecraft.loot.LootModifierRegistry;
import com.extra.cosmerecraft.network.ModMessages;
import com.extra.cosmerecraft.recipe.ModRecipes;
import com.extra.cosmerecraft.world.feature.ModConfiguredFeatures;
import com.extra.cosmerecraft.world.feature.ModPlacedFeatures;
import com.google.common.graph.Network;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
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
        ModEffects.register(modEventBus);
        LootModifierRegistry.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModConfiguredFeatures.register(modEventBus);
        ModPlacedFeatures.register(modEventBus);
        ModEntities.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(CosmereCraft::init);
        modEventBus.addListener(CosmereCraft::entityAttributes);
        modEventBus.addListener(FeruchemistCapability::registerCapability);
        modEventBus.addListener(AllomancerCapability::registerCapability);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(CommandSetup::registerCommands);
        CommandSetup.register();

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    public void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().size(12).build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BRACELET.getMessageBuilder().size(6).build());
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MinecraftForge.EVENT_BUS.register(new ClientEvents());
            MenuScreens.register(ModMenuTypes.ALLOYING_FURNACE_MENU.get(), AlloyingFurnaceScreen::new);
        }

        @SubscribeEvent
        public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event){
            event.registerEntityRenderer(ModEntities.SHADOW_ENTITY.get(), ShadowRenderer::new);
        }

        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
            event.registerLayerDefinition(ShadowModel.LAYER_LOCATION, ShadowModel::createBodyLayer);
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
            MinecraftForge.EVENT_BUS.register(LootTableInjector.class);
            ItemProperties.register(ModItems.VIAL.get(), new ResourceLocation("cosmerecraft:has_metals"), new VialItem.VialHasMetalsPropertyFunction());
        });
    }

    public static void entityAttributes(final EntityAttributeCreationEvent event){
        event.put(ModEntities.SHADOW_ENTITY.get(), ShadowEntity.getShadowAttributes().build());
    }
}
