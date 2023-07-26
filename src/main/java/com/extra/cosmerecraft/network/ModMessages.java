package com.extra.cosmerecraft.network;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.allomancy.data.AllomancerCapability;
import com.extra.cosmerecraft.api.data.IAllomancyData;
import com.extra.cosmerecraft.api.data.IFeruchemyData;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static final String VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(CosmereCraft.MODID, "networking"), () -> VERSION, VERSION::equals,
            VERSION::equals);

    private static int packetId = 0;
    private static int id(){
        return packetId++;
    }

    public static void registerPackets() {
        INSTANCE.registerMessage(id(), FeruchemistDataPacket.class, FeruchemistDataPacket::encode, FeruchemistDataPacket::decode, FeruchemistDataPacket::handle);
        INSTANCE.registerMessage(id(), UpdateTappingPacket.class, UpdateTappingPacket::encode, UpdateTappingPacket::decode, UpdateTappingPacket::handle);
        INSTANCE.registerMessage(id(), AllomancerDataPacket.class, AllomancerDataPacket::encode, AllomancerDataPacket::decode, AllomancerDataPacket::handle);
        INSTANCE.registerMessage(id(), UpdateBurnPacket.class, UpdateBurnPacket::encode, UpdateBurnPacket::decode, UpdateBurnPacket::handle);
    }

    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static void sync(IFeruchemyData cap, ServerPlayer player) {
        sync(new FeruchemistDataPacket(cap, player), player);
    }

    public static void sync(IAllomancyData cap, ServerPlayer player){
        sync(new AllomancerDataPacket(cap, player), player);
    }

    public static void sync(ServerPlayer player) {
        player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> sync(data, player));
        player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(data -> sync(data, player));
    }

    public static void sync(Object msg, ServerPlayer player) {
        sendTo(msg, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player));
    }

    public static void sendTo(Object msg, PacketDistributor.PacketTarget target) {
        INSTANCE.send(target, msg);
    }
}
