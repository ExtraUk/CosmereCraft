package com.extra.cosmerecraft.event;

import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistDataProvider;
import com.extra.cosmerecraft.network.ModMessages;
import com.google.common.graph.Network;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonEventHandler {

    @SubscribeEvent
    public static void onAttachCapability(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player p) {
            FeruchemistDataProvider provider = new FeruchemistDataProvider();
            event.addCapability(FeruchemistCapability.IDENTIFIER, provider);
        }
    }

    @SubscribeEvent
    public static void onJoinWorld(final PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
                if (data.isUninvested()) {
                    for(Metal metal: Metal.values()) {
                        data.addPower(metal);
                    }
                }
            });

            //Sync cap to client
            ModMessages.sync(player);
        }

    }

    @SubscribeEvent
    public static void onPlayerClone(final PlayerEvent.Clone event) {
        if (!event.getEntity().level.isClientSide() && event.getEntity() instanceof ServerPlayer player) {


            event.getOriginal().reviveCaps();
            player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> event.getOriginal().getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(oldData -> {
                if (!oldData.isUninvested()) { // make sure the new player has the same power status
                    for (Metal mt : Metal.values()) {
                        if (oldData.hasPower(mt)) {
                            data.addPower(mt);
                        }
                    }
                }
            }));
            event.getOriginal().getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).invalidate();
            event.getOriginal().invalidateCaps();

            ModMessages.sync(player);
        }
    }

    @SubscribeEvent
    public static void onRespawn(final PlayerEvent.PlayerRespawnEvent event) {
        if (!event.getEntity().getCommandSenderWorld().isClientSide() && event.getEntity() instanceof ServerPlayer player) {
            ModMessages.sync(player);
        }
    }

    @SubscribeEvent
    public static void onWorldTick(final TickEvent.LevelTickEvent event){
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Level level = event.level;
        var list = level.players();
        for (int i = list.size() - 1; i >= 0; i--) {
            Player curPlayer = list.get(i);
            playerPowerTick(curPlayer, level);
        }

    }

    private static void playerPowerTick(Player curPlayer, Level level) {
        curPlayer.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
            if (curPlayer instanceof ServerPlayer player) {
                data.tickTapStore(player);
            }
            int tl = data.tappingLevel(Metal.TIN);
            if(tl != 0){
                if(tl == -1){
                    curPlayer.removeEffect(MobEffects.NIGHT_VISION);
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 204, 0, false, false, true));
                }
                else{
                    curPlayer.removeEffect(MobEffects.BLINDNESS);
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 204, 0, false, false, true));
                }
            }

            tl = data.tappingLevel(Metal.PEWTER);
            if(tl != 0){
                if(tl == -1){
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 204, 0, false, false, true));
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 204, 0, false, false, true));
                    curPlayer.removeEffect(MobEffects.DAMAGE_BOOST);
                }
                else{
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 204, tl-1, false, false, true));
                    curPlayer.removeEffect(MobEffects.WEAKNESS);
                    curPlayer.removeEffect(MobEffects.DIG_SLOWDOWN);
                }
            }

            tl = data.tappingLevel(Metal.STEEL);
            if(tl != 0){
                if(tl == -1){
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 204, 0, false, false, true));
                    curPlayer.removeEffect(MobEffects.MOVEMENT_SPEED);
                }
                else{
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 204, tl-1, false, false, true));
                    curPlayer.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                }
            }

            tl = data.tappingLevel(Metal.IRON);
            if(tl != 0){
                if(tl == -1){
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 204, 0, false, false, true));
                    curPlayer.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                }
                else{
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 204, tl-1, false, false, true));
                    curPlayer.removeEffect(MobEffects.SLOW_FALLING);
                }
            }

            tl = data.tappingLevel(Metal.CHROMIUM);
            if(tl != 0){
                if(tl == -1){
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 204, 0, false, false, true));
                    curPlayer.removeEffect(MobEffects.LUCK);
                }
                else{
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.LUCK, 204, tl-1, false, false, true));
                    curPlayer.removeEffect(MobEffects.UNLUCK);
                }
            }

            tl = data.tappingLevel(Metal.DURALUMIN);
            if(tl != 0){
                if(tl == -1){
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 204, 0, false, false, true));
                    curPlayer.removeEffect(MobEffects.HERO_OF_THE_VILLAGE);
                }
                else{
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 204, tl-1, false, false, true));
                    curPlayer.removeEffect(MobEffects.BAD_OMEN);
                }
            }


        });
    }

}
