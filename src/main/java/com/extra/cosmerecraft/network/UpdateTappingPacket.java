package com.extra.cosmerecraft.network;

import com.extra.cosmerecraft.api.data.IFeruchemyData;
import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.effect.ModEffects;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.function.Supplier;

import static com.extra.cosmerecraft.feruchemy.data.DefaultFeruchemistData.removeEffects;

public class UpdateTappingPacket {
    private final Metal mt;
    private final int level;
    private final int mouseButton;

    public UpdateTappingPacket(Metal mt, int level, int mouseButton) {
        this.mt = mt;
        this.level = level;
        this.mouseButton = mouseButton;
    }

    public static UpdateTappingPacket decode(FriendlyByteBuf buf) {
        return new UpdateTappingPacket(buf.readEnum(Metal.class), buf.readInt(), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.mt);
        buf.writeInt(this.level);
        buf.writeInt(this.mouseButton);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();

            player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
                int tappingLevel = this.level+this.mouseButton;
                if (data.hasPower(this.mt)){
                    data.setTappingLevel(this.mt, tappingLevel);
                    if(this.mt == Metal.ATIUM) {
                        player.hurt(DamageSource.GENERIC, (float) 0.001);
                    }
                    if(this.mt == Metal.CADMIUM && tappingLevel < 0){
                        player.setAirSupply(player.getAirSupply()+tappingLevel);
                    }
                }
                else {
                    data.setTappingLevel(this.mt, 0);
                }
                removeEffects(player, mt);
                ModMessages.sync(data, player);
            });

        });
        ctx.get().setPacketHandled(true);
    }
}
