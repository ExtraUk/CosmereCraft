package com.extra.cosmerecraft.allomancy.data;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.api.data.IAllomancyData;
import com.extra.cosmerecraft.api.data.IFeruchemyData;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class AllomancerCapability {

    public static final Capability<IAllomancyData> PLAYER_CAP_ALLOMANCY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final ResourceLocation IDENTIFIER = new ResourceLocation(CosmereCraft.MODID, "allomancy_data");

    public static void registerCapability(final RegisterCapabilitiesEvent event) {
        event.register(IAllomancyData.class);
    }

}
