package com.extra.cosmerecraft.command;

import com.extra.cosmerecraft.CosmereCraft;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CommandSetup {

    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, CosmereCraft.MODID);
    private static final RegistryObject<SingletonArgumentInfo<FeruchemyPowerType>> CONTAINER_CLASS_FERUCHEMY = COMMAND_ARGUMENT_TYPES.register("feruchemy_power",
            () -> ArgumentTypeInfos.registerByClass(
                    FeruchemyPowerType.class,
                    SingletonArgumentInfo.contextFree(
                            FeruchemyPowerType::feruchemyPowerType)));
    private static final RegistryObject<SingletonArgumentInfo<AllomancyPowerType>> CONTAINER_CLASS_ALLOMANCY = COMMAND_ARGUMENT_TYPES.register("allomancy_power",
            () -> ArgumentTypeInfos.registerByClass(
                    AllomancyPowerType.class,
                    SingletonArgumentInfo.contextFree(
                            AllomancyPowerType::allomancyPowerType)));

    public static void registerCommands(final RegisterCommandsEvent e) {
        PowerCommand.register(e.getDispatcher());
    }

    public static void register() {
        COMMAND_ARGUMENT_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
