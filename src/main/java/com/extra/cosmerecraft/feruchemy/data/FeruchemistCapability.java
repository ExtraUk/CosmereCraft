package com.extra.cosmerecraft.feruchemy.data;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.api.data.IFeruchemyData;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class FeruchemistCapability {

    public static final Capability<IFeruchemyData> PLAYER_CAP_FERUCHEMY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final ResourceLocation IDENTIFIER = new ResourceLocation(CosmereCraft.MODID, "feruchemy_data");

    public static void registerCapability(final RegisterCapabilitiesEvent event) {
        event.register(IFeruchemyData.class);
    }

}
