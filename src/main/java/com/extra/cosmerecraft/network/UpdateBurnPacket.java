package com.extra.cosmerecraft.network;

import com.extra.cosmerecraft.allomancy.data.AllomancerCapability;
import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.extra.cosmerecraft.feruchemy.data.DefaultFeruchemistData.removeEffects;

public class UpdateBurnPacket {
    private final Metal mt;
    private final boolean burn;
    public UpdateBurnPacket(Metal mt, boolean burn) {
        this.mt = mt;
        this.burn = burn;
    }

    public static UpdateBurnPacket decode(FriendlyByteBuf buf) {
        return new UpdateBurnPacket(buf.readEnum(Metal.class), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.mt);
        buf.writeBoolean(this.burn);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();

            player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(data -> {
                boolean burn = this.burn;
                if (data.hasPower(this.mt)){
                    data.setBurning(this.mt, burn);
                }
                else {
                    data.setBurning(this.mt, false);
                }
                ModMessages.sync(data, player);
            });

        });
        ctx.get().setPacketHandled(true);
    }
}
