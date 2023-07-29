package com.extra.cosmerecraft.network;

import com.extra.cosmerecraft.allomancy.data.AllomancerCapability;
import com.extra.cosmerecraft.api.enums.Metal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class UpdateSkinPacket {
    private final ResourceLocation skin;
    public UpdateSkinPacket(ResourceLocation skin) {
        this.skin = skin;
    }

    public static UpdateSkinPacket decode(FriendlyByteBuf buf) {
        return new UpdateSkinPacket(buf.readResourceLocation());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.skin);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();

            player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(data -> {
               data.setSkin(this.skin);
                ModMessages.sync(data, player);
            });

        });
        ctx.get().setPacketHandled(true);
    }
}
