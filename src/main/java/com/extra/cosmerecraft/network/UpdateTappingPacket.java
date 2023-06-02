package com.extra.cosmerecraft.network;

import com.extra.cosmerecraft.api.data.IFeruchemyData;
import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.function.Supplier;

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
                if (data.hasPower(this.mt) /*&& data.getAmount(this.mt) > 0*/) {
                    data.setTappingLevel(this.mt, tappingLevel);
                        removeEffects(player, data, mt);
                }
                else {
                    data.setTappingLevel(this.mt, 0);
                    removeEffects(player, data, mt);
                }
                ModMessages.sync(data, player);
            });

        });
        ctx.get().setPacketHandled(true);
    }

    public void removeEffects(ServerPlayer player, IFeruchemyData data, Metal metal){
        if(metal.getName().equals("tin")){
            player.removeEffect(MobEffects.BLINDNESS);
            player.removeEffect(MobEffects.NIGHT_VISION);
        }
        else if(metal.getName().equals("pewter")){
            player.removeEffect(MobEffects.DAMAGE_BOOST);
            player.removeEffect(MobEffects.WEAKNESS);
            player.removeEffect(MobEffects.DIG_SLOWDOWN);
        }
        else if(metal.getName().equals("iron")){
            player.removeEffect(MobEffects.SLOW_FALLING);
            player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        }
        else if(metal.getName().equals("steel")){
            player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            player.removeEffect(MobEffects.MOVEMENT_SPEED);
        }
        else if(metal.getName().equals("chromium")){
            player.removeEffect(MobEffects.LUCK);
            player.removeEffect(MobEffects.UNLUCK);
        }
        else if(metal.getName().equals("duralumin")){
            player.removeEffect(MobEffects.BAD_OMEN);
            player.removeEffect(MobEffects.HERO_OF_THE_VILLAGE);
        }
    }
}
