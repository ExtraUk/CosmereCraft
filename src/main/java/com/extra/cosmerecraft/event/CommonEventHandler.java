package com.extra.cosmerecraft.event;

import com.extra.cosmerecraft.allomancy.data.AllomancerCapability;
import com.extra.cosmerecraft.allomancy.data.AllomancerDataProvider;
import com.extra.cosmerecraft.api.data.IAllomancyData;
import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.api.enums.Power;
import com.extra.cosmerecraft.effect.ModEffects;
import com.extra.cosmerecraft.entity.ModEntities;
import com.extra.cosmerecraft.entity.custom.ShadowEntity;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistDataProvider;
import com.extra.cosmerecraft.item.LerasiumBeadItem;
import com.extra.cosmerecraft.network.ModMessages;
import com.extra.cosmerecraft.network.SpawnParticleClient;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;


public class CommonEventHandler {

    @SubscribeEvent
    public static void onAttachCapability(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player p) {
            FeruchemistDataProvider fProvider = new FeruchemistDataProvider();
            AllomancerDataProvider aProvider = new AllomancerDataProvider();
            event.addCapability(FeruchemistCapability.IDENTIFIER, fProvider);
            event.addCapability(AllomancerCapability.IDENTIFIER, aProvider);
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(final LivingDamageEvent event){
        if(event.getSource().getEntity() instanceof Player player){
            player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
                if(data.tappingLevel(Metal.BRASS) > 0){
                    event.getEntity().setSecondsOnFire(2+data.tappingLevel(Metal.BRASS));
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerSleep(final PlayerSleepInBedEvent event){
        event.getEntity().getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
            if(data.tappingLevel(Metal.BRONZE) < 0){
                event.setResult(Event.Result.ALLOW);
            }
        });
    }

    @SubscribeEvent
    public static void onSleepTimeCheck(final SleepingTimeCheckEvent event) {
        event.getEntity().getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
            if (data.tappingLevel(Metal.BRONZE) < 0) {
                long currentTime = event.getEntity().getLevel().getDayTime();
                while (currentTime > 24000){currentTime -= 24000;}
                data.setBronzeSleepingTime(event.getEntity(), currentTime);
                event.setResult(Event.Result.ALLOW);
            }
        });
    }

    @SubscribeEvent
    public static void onSleepFinished(final SleepFinishedTimeEvent event){
        long currentTime = event.getLevel().dayTime();
        while (currentTime > 24000){currentTime -= 24000;}
        if((currentTime < 12542 && !event.getLevel().getLevelData().isThundering()) || (currentTime < 12010 && event.getLevel().getLevelData().isThundering())){
            event.setTimeAddition(event.getLevel().dayTime() + 6000);
        }
    }

    @SubscribeEvent
    public static void onPlayerWake(final PlayerWakeUpEvent event){
        event.getEntity().getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
            if (data.tappingLevel(Metal.BRONZE) < 0) {
                long currentTime = event.getEntity().getLevel().dayTime();
                while (currentTime > 24000){currentTime -= 24000;}
                data.setBronzeWakeUpCharges(event.getEntity(), currentTime);
            }
        });
    }

    @SubscribeEvent
    public static void onMobChangeAttackTarget(final LivingChangeTargetEvent event){
        if(event.getEntity() instanceof Phantom){
            if(event.getNewTarget() != null) {
                event.getNewTarget().getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
                    if (data.tappingLevel(Metal.BRONZE) > 0) {
                        event.setCanceled(true);
                    }
                });
            }
        }
    }


    @SubscribeEvent
    public static void lootingLevel(final LootingLevelEvent event){
        if(event.getDamageSource() != null && event.getDamageSource().getEntity() != null) {
            event.getDamageSource().getEntity().getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
                event.setLootingLevel(event.getLootingLevel() + data.tappingLevel(Metal.CHROMIUM));
            });
        }
    }

    @SubscribeEvent
    public static void breakSpeed(final PlayerEvent.BreakSpeed event){
        if(event.getEntity().hasEffect(ModEffects.MANIC.get())){
            event.setNewSpeed(event.getOriginalSpeed()*((float)1+(((float)event.getEntity().getEffect(ModEffects.MANIC.get()).getAmplifier()+1)/5)));
        }
        else if(event.getEntity().hasEffect(ModEffects.DEPRESSED.get())){
            event.setNewSpeed(event.getOriginalSpeed()*((float)1-(((float)event.getEntity().getEffect(ModEffects.DEPRESSED.get()).getAmplifier()+1)/5)));
        }
    }

    @SubscribeEvent
    public static void onAnimalTame(final AnimalTameEvent event){
        event.getTamer().getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
            if (data.tappingLevel(Metal.DURALUMIN) < 0) {
                if(Math.random() < 0.5 * -data.tappingLevel(Metal.DURALUMIN)) {
                    event.setCanceled(true);
                }
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerInteract(final PlayerInteractEvent.EntityInteractSpecific event){
        if(!event.getLevel().isClientSide){
            if(event.getTarget() instanceof TamableAnimal){
                event.getEntity().getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
                    if (data.tappingLevel(Metal.DURALUMIN) > 2) {
                        ((TamableAnimal) event.getTarget()).tame(event.getEntity());
                    }
                });
            }
            else if(event.getTarget() instanceof AbstractHorse){
                event.getEntity().getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
                    if (data.tappingLevel(Metal.DURALUMIN) > 2) {
                        ((AbstractHorse) event.getTarget()).setTamed(true);
                        ((AbstractHorse) event.getTarget()).setOwnerUUID(event.getEntity().getUUID());
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(final LivingAttackEvent event){
        event.getEntity().getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
            int tappingLevel = data.tappingLevel(Metal.ZINC);
            if (tappingLevel < 0) {
                if(event.getSource() != DamageSource.OUT_OF_WORLD) {
                    if (Math.random() > 0.5/-tappingLevel){
                        event.getEntity().hurt(event.getSource(), event.getAmount()/2);
                    }
                }
            }
            else if(tappingLevel > 0){
                if(event.getSource() != DamageSource.OUT_OF_WORLD) {
                    if (Math.random() > 0.5/tappingLevel){
                        event.setCanceled(true);
                        System.out.println(event.getSource().getDirectEntity());
                        if(event.getSource().getDirectEntity() instanceof Arrow){
                            if(event.getEntity().getMainHandItem() == ItemStack.EMPTY){
                                event.getEntity().setItemInHand(InteractionHand.MAIN_HAND, Items.ARROW.getDefaultInstance());
                                event.getSource().getDirectEntity().kill();
                            }
                            else if(event.getEntity().getOffhandItem() == ItemStack.EMPTY){
                                event.getEntity().setItemInHand(InteractionHand.OFF_HAND, Items.ARROW.getDefaultInstance());
                                event.getSource().getDirectEntity().kill();
                            }
                        }
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void onEntityJump(final LivingEvent.LivingJumpEvent event){
        if(event.getEntity().hasEffect(ModEffects.HEAVY.get())){
            event.getEntity().setDeltaMovement(event.getEntity().getDeltaMovement().x, event.getEntity().getDeltaMovement().y - ((float)event.getEntity().getEffect(ModEffects.HEAVY.get()).getAmplifier()+1)/20, event.getEntity().getDeltaMovement().z);
        }
    }

    @SubscribeEvent
    public static void onEntityFinishUsingItem(final LivingEntityUseItemEvent.Finish event){
        if(event.getItem().getItem() instanceof LerasiumBeadItem bead){
            event.getEntity().getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(data -> {
                if(bead.isPureLerasium()){
                    data.setMistborn();
                }
                else{
                    data.addPower(bead.getMetal());
                }
            });
        }
    }

    @SubscribeEvent
    public static void onJoinWorld(final PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
                if (!data.wasEverInvested()) {
                    Random rand = new Random();
                    data.investFirstTime();
                    if(rand.nextInt(16) == 0){
                        data.setFeruchemist();
                    }
                    else{
                        data.addPower(Metal.getMetal(rand.nextInt(Metal.values().length)));
                    }
                }
            });

            player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(data -> {
                if (!data.wasEverInvested()) {
                    Random rand = new Random();
                    data.investFirstTime();
                    if(rand.nextInt(16) == 0){
                        data.setMistborn();
                    }
                    else{
                        data.addPower(Metal.getMetal(rand.nextInt(Metal.values().length)));
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
            player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(data -> event.getOriginal().getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(oldData -> {
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
            player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(data -> {
                BlockPos pos = player.getLastDeathLocation().get().pos();
                data.setDeathLoc(pos.getX(), pos.getY(), pos.getZ());
            });
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
        curPlayer.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(data -> {
            if (curPlayer instanceof ServerPlayer player) {
                data.tickBurn(player);
            }

            if(data.isBurning(Metal.ALUMINUM)){
                if(curPlayer instanceof ServerPlayer) {
                    data.wipeReserves((ServerPlayer) curPlayer);
                }
            }
            if(data.isBurning(Metal.TIN)){
                curPlayer.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 206, 5, false, false, true));
            }
            if(data.isBurning(Metal.PEWTER)){
                curPlayer.addEffect(new MobEffectInstance(ModEffects.ALLO_PEWTER.get(), 202, 0, false, false, true));
            }
            if(data.isBurning(Metal.GOLD)){
                handleGoldAllomancy(curPlayer, data);
            }
            if(data.isBurning(Metal.ELECTRUM)){
                handleElectrumAllomancy(curPlayer, data);
            }
            if(data.isBurning(Metal.BRONZE)){
                handleBronzeAllomancy(curPlayer, data);
            }
            if(data.isBurning(Metal.COPPER)){
                data.resetHiddenAllomancyDuration();
                updateCopperCloud(curPlayer, data);
            }
        });
        curPlayer.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
            if (curPlayer instanceof ServerPlayer player) {
                data.tickTapStore(player);
            }

            int tl = data.tappingLevel(Metal.IRON);
            if(tl != 0){
                if(tl == -1){
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.JUMP, 202, 0, false, false, true));
                }
                else if(tl < -1){
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 202, 0, false, false, true));
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.JUMP, 202, -tl-1, false, false, true));
                }
                else{
                    curPlayer.addEffect(new MobEffectInstance(ModEffects.HEAVY.get(), 202, tl-1, false, false, true));
                }
            }

            tl = data.tappingLevel(Metal.STEEL);
            if(tl != 0){
                if(tl < 0){
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 202, -tl-1, false, false, true));
                }
                else{
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 202, tl-1, false, false, true));
                }
            }

            tl = data.tappingLevel(Metal.PEWTER);
            if(tl != 0){
                if(tl < 0){
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 202, -tl-1, false, false, true));
                }
                else{
                    curPlayer.addEffect(new MobEffectInstance(ModEffects.FERU_PEWTER.get(), 202, tl-1, false, false, true));
                }
            }

            tl = data.tappingLevel(Metal.COPPER);
            if(tl != 0){
                curPlayer.giveExperiencePoints(tl);
            }

            tl = data.tappingLevel(Metal.BRONZE);
            if(tl < 0){
                curPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 202, 0, false, false, true));
                curPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 202, 2, false, false, true));
            }

            tl = data.tappingLevel(Metal.BRASS);
            if(tl != 0){
                if(tl == -1){
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 202, 0, false, false, true));
                    curPlayer.setSecondsOnFire(0);
                    if(level.getBiome(curPlayer.blockPosition()).containsTag(Tags.Biomes.IS_COLD)){
                        curPlayer.setTicksFrozen(100);
                    }
                }
                else if(tl < -1){
                    if(!level.getBiome(curPlayer.blockPosition()).containsTag(Tags.Biomes.IS_HOT) && !curPlayer.isOnFire()){
                        curPlayer.setTicksFrozen(100);
                    }
                    if(level.getBiome(curPlayer.blockPosition()).containsTag(Tags.Biomes.IS_COLD)){
                        curPlayer.setTicksFrozen(110);
                    }
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 202, 0, false, false, true));
                    curPlayer.setSecondsOnFire(0);
                }
                else{
                    curPlayer.setTicksFrozen(0);
                    if(level.getBiome(curPlayer.blockPosition()).containsTag(Tags.Biomes.IS_HOT)){
                        curPlayer.setSecondsOnFire(5);
                    }
                }
            }

            tl = data.tappingLevel(Metal.GOLD);
            if(tl > 0){
                if(curPlayer.getHealth() < curPlayer.getMaxHealth()) {
                    curPlayer.heal(tl * (float) 0.05);
                }
            }

            tl = data.tappingLevel(Metal.ELECTRUM);
            if(tl != 0){
                if(tl < 0){
                    curPlayer.addEffect(new MobEffectInstance(ModEffects.DEPRESSED.get(), 202, -tl-1, false, false, true));
                }
                else{
                    curPlayer.addEffect(new MobEffectInstance(ModEffects.MANIC.get(), 202, tl-1, false, false, true));
                }
            }

            tl = data.tappingLevel(Metal.CADMIUM);
            if(tl != 0){
                if(tl < 0){
                    if(curPlayer.getAirSupply() <= 0){
                        curPlayer.hurt(DamageSource.DROWN, 1);
                    }
                    else{
                        curPlayer.setAirSupply(curPlayer.getAirSupply()+(5*tl));
                    }
                }
                else{
                    if(curPlayer.getAirSupply() < curPlayer.getMaxAirSupply()){
                        curPlayer.setAirSupply(curPlayer.getAirSupply()+tl);
                    }
                }
            }

            tl = data.tappingLevel(Metal.CHROMIUM);
            if(tl != 0){
                if(tl < 0){
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 202, -tl-1, false, false, true));
                    if(Math.random() > 0.999+((tl+1)*0.001)) {
                        LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                        lightning.setPos(curPlayer.position());
                        level.addFreshEntity(lightning);
                    }
                }
                else{
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.LUCK, 202, tl-1, false, false, true));
                }
            }

            tl = data.tappingLevel(Metal.DURALUMIN);
            if(tl != 0){
                if(tl < 0){
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 202, -tl-1, false, false, true));
                }
                else{
                    curPlayer.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 202, tl-1, false, false, true));
                }
            }

            tl = data.tappingLevel(Metal.ATIUM);
            if(tl != 0){
                if(tl < 0){
                    curPlayer.addEffect(new MobEffectInstance(ModEffects.OLD.get(), 202, -tl-1, false, false, true));
                }
                else{
                    curPlayer.addEffect(new MobEffectInstance(ModEffects.YOUNG.get(), 202, tl-1, false, false, true));
                }
            }
        });
    }

    private static void updateCopperCloud(Player curPlayer, IAllomancyData data) {
        if(data.getCopperCloudCooldown() <= 0){
            data.resetCopperCloudCooldown();
            for(Player player: curPlayer.level.players()){
                if(curPlayer.position().distanceTo(player.position()) < 7){
                    player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(playerData -> {
                        playerData.resetHiddenAllomancyDuration();
                    });
                }
            }
        }
    }

    private static void handleElectrumAllomancy(Player curPlayer, IAllomancyData data) {
        if(data.getElectrumCooldown() <= 0){
            BlockPos pos = curPlayer.blockPosition();
            ResourceLocation skin = data.getSkin();
            ShadowEntity electrumShadow = ModEntities.SHADOW_ENTITY.get().create(curPlayer.getLevel());
            Vec3 vectorPosition = new Vec3(pos.getX(), pos.getY(), pos.getZ());
            Vec3 mov = vectorPosition.subtract(data.getPreviousPos());
            data.setPreviousPos(vectorPosition);
            electrumShadow.setUUID(UUID.fromString(curPlayer.getStringUUID().replaceAll("^.{3}", "321")));
            electrumShadow.setPlayerUUID(curPlayer.getUUID());
            electrumShadow.setPos(vectorPosition);
            electrumShadow.setSkin(skin);
            electrumShadow.setDeathTimer(20);
            electrumShadow.setMetal(Metal.ELECTRUM);
            curPlayer.getLevel().addFreshEntity(electrumShadow);
            Vec3 newPos = vectorPosition.add(mov);
            electrumShadow.moveTo(newPos.x, newPos.y, newPos.z, curPlayer.getYRot(),curPlayer.getXRot());
            electrumShadow.setYHeadRot(curPlayer.getYHeadRot());

            data.setElectrumCooldown(21);
            if(!data.getShadowUUID().equals(electrumShadow.getPlayerUUID())){
                data.setShadowUUID(electrumShadow.getPlayerUUID());
                ModMessages.sync(data, (ServerPlayer) curPlayer);
            }
        }
    }

    private static void handleBronzeAllomancy(Player curPlayer, IAllomancyData data){
        if (data.getBronzeCooldown() <= 0) {
            for(Player player : curPlayer.level.players()){
                AtomicBoolean bool = new AtomicBoolean(true);
                player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(playerData -> {
                            bool.set(playerData.getHiddenAllomancyDuration() <= 0);});
                if(bool.get()) {
                    Vec3 playerPos = player.position();
                    double distance = playerPos.distanceTo(curPlayer.position());
                    if (distance < 30 && distance > 0) {
                        for (Metal metal : Metal.values()) {
                            player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(playerData -> {
                                if (playerData.isBurning(metal)) {
                                    ModMessages.sendToPlayer(new SpawnParticleClient(playerPos.x, playerPos.y, playerPos.z, Power.ALLOMANCY, metal.getIndex()), (ServerPlayer) curPlayer);
                                }
                            });
                            player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(playerData -> {
                                if (playerData.tappingLevel(metal) > 0) {
                                    ModMessages.sendToPlayer(new SpawnParticleClient(playerPos.x, playerPos.y, playerPos.z, Power.FERUCHEMY, metal.getIndex()), (ServerPlayer) curPlayer);
                                }
                            });
                        }
                    }
                }
            }
            data.resetBronzeCooldown();
        }
    }

    private static void handleGoldAllomancy(Player curPlayer, IAllomancyData data) {
        BlockPos pos = curPlayer.blockPosition();
        ResourceLocation skin = data.getSkin();
        for (Entity entity : curPlayer.getLevel().getEntities(null, new AABB(pos.offset(-6, -6, -6), pos.offset(6, 6, 6)))) {
            if (entity.getUUID().equals(UUID.fromString(curPlayer.getUUID().toString().replaceAll("^.{3}", "123")))) {
                entity.discard();
            }
        }
        ShadowEntity goldShadow = ModEntities.SHADOW_ENTITY.get().create(curPlayer.getLevel());
        Vec3 deathLoc = new Vec3(data.getDeathLoc().x, curPlayer.position().y, data.getDeathLoc().z);
        Vec3 deathPointingVector = curPlayer.position().add(deathLoc.subtract(curPlayer.position()).normalize().scale(3)); //Increible algebra de vectores
        goldShadow.setUUID(UUID.fromString(curPlayer.getStringUUID().replaceAll("^.{3}", "123")));
        goldShadow.setPlayerUUID(curPlayer.getUUID());
        goldShadow.setPos(deathPointingVector);
        goldShadow.setSkin(skin);
        goldShadow.setMetal(Metal.GOLD);
        curPlayer.getLevel().addFreshEntity(goldShadow);

        if(!data.getShadowUUID().equals(goldShadow.getPlayerUUID())){
            data.setShadowUUID(goldShadow.getPlayerUUID());
            ModMessages.sync(data, (ServerPlayer) curPlayer);
        }
    }

}
