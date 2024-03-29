package com.extra.cosmerecraft.feruchemy.data;

import com.extra.cosmerecraft.api.data.IFeruchemyData;
import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.effect.ModEffects;
import com.extra.cosmerecraft.item.MetalmindItem;
import com.extra.cosmerecraft.item.ModItems;
import com.extra.cosmerecraft.network.ModMessages;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Arrays;
import java.util.List;

public class DefaultFeruchemistData implements IFeruchemyData {

    private final boolean[] feruchemical_powers;
    private final boolean[] feruchemical_powers_hemalurgy;
    private final boolean[] feruchemical_powers_nicrosil;
    private final int[] tapping_metals;
    private boolean wasTappingNicrosil = false;
    private boolean flag = false;
    private boolean wasInvested = false;

    public DefaultFeruchemistData(){
        int powers = Metal.values().length;
        this.feruchemical_powers = new boolean[powers];
        this.feruchemical_powers_nicrosil = new boolean[powers];
        this.feruchemical_powers_hemalurgy = new boolean[powers];
        Arrays.fill(this.feruchemical_powers, false);
        Arrays.fill(this.feruchemical_powers_nicrosil, false);
        Arrays.fill(this.feruchemical_powers_hemalurgy, false);

        this.tapping_metals = new int[powers];
        Arrays.fill(this.tapping_metals, 0);
    }

    @Override
    public void tickTapStore(ServerPlayer player) {
        boolean sync = false;
        if(!hasBandsOfMourning(player)) {
            if(this.tappingLevel(Metal.NICROSIL) >= 0) {
                for (Metal metal : Metal.values()) {
                    int tappingLevel = this.tappingLevel(metal);
                    if (metal != Metal.NICROSIL || tappingLevel > 0) {
                        int charges = this.getMetalmindCharges(metal, player);
                        if (tappingLevel != 0) {
                            if (!this.hasPower(metal) || !this.hasMetalmind(metal, player) || (tappingLevel < 0 && ((metal == Metal.COPPER && player.totalExperience <= 0 && player.experienceLevel <= 0) || metal == Metal.BENDALLOY && player.getFoodData().getFoodLevel() <= 0))) {
                                this.setTappingLevel(metal, 0);
                                removeEffects(player, metal);
                                sync = true;
                            } else {
                                if ((tappingLevel > 0 && charges <= 0) || (tappingLevel < 0 && this.areMetalmindsFull(metal, player))) {
                                    this.setTappingLevel(metal, 0);
                                    removeEffects(player, metal);
                                    sync = true;
                                } else if (charges <= 0) {
                                    if (metal == Metal.GOLD) this.adjustGold(player, tappingLevel);
                                    if (metal == Metal.BENDALLOY) this.adjustBendalloy(player, tappingLevel);
                                    adjustMetalmindCharges(metal, tappingLevel, player);
                                    sync = true;
                                } else {
                                    if(metal == Metal.NICROSIL){
                                        adjustNicrosil(player, tappingLevel);
                                        if(!wasTappingNicrosil){
                                            sync = true;
                                            wasTappingNicrosil = true;
                                        }
                                    }
                                    else {
                                        if (metal == Metal.GOLD) this.adjustGold(player, tappingLevel);
                                        if (metal == Metal.BENDALLOY) this.adjustBendalloy(player, tappingLevel);
                                        adjustMetalmindCharges(metal, tappingLevel, player);
                                    }
                                }
                            }
                        }
                        if (sync) {
                            ModMessages.sync(this, player);
                        }
                    }
                    else if(wasTappingNicrosil){
                        Arrays.fill(this.feruchemical_powers_nicrosil, false);
                        wasTappingNicrosil = false;
                        ModMessages.sync(this, player);
                    }
                }
            }
            else {
                if (!this.hasPower(Metal.NICROSIL) || !this.hasMetalmind(Metal.NICROSIL, player) || this.areMetalmindsFull(Metal.NICROSIL, player)){
                    this.setTappingLevel(Metal.NICROSIL, 0);
                    ModMessages.sync(this, player);
                }
                else {
                    this.stopTapping(player);
                    this.setTappingLevel(Metal.NICROSIL, -1);
                    adjustNicrosil(player, -1);
                }
            }
        }
        else{
            if(tappingLevel(Metal.BENDALLOY) > 0){
                player.getFoodData().eat(1,0);
            }
        }
    }

    public void adjustGold(ServerPlayer player, int tappingLevel){
        List<SlotResult> curios = CuriosApi.getCuriosHelper().findCurios(player, "ring", "bracelet");
        if(tappingLevel < 0) {
            for (SlotResult slot : curios) {
                if (slot.stack().getItem() instanceof MetalmindItem && ((MetalmindItem) slot.stack().getItem()).getMetal() == Metal.GOLD) {
                    if(slot.stack().getTag().getInt("cooldown") < 20/(-tappingLevel)) {
                        slot.stack().getTag().putInt("cooldown", slot.stack().getTag().getInt("cooldown") + 1);
                    }
                    else{
                        player.hurt(DamageSource.OUT_OF_WORLD, 1);
                        slot.stack().getTag().putInt("cooldown", 1);
                    }
                    return;
                }
            }
        }
        else{
            for (SlotResult slot : curios) {
                if (slot.stack().getItem() instanceof MetalmindItem && ((MetalmindItem) slot.stack().getItem()).getMetal() == Metal.GOLD) {
                    if(!(player.getHealth() < player.getMaxHealth())) {
                        slot.stack().getTag().putInt("charges", slot.stack().getTag().getInt("charges") + tappingLevel);
                    }
                }
            }
        }
    }

    public void adjustBendalloy(ServerPlayer player, int tappingLevel){
        List<SlotResult> curios = CuriosApi.getCuriosHelper().findCurios(player, "ring", "bracelet");
        if(tappingLevel < 0) {
            for (SlotResult slot : curios) {
                if (slot.stack().getItem() instanceof MetalmindItem && ((MetalmindItem) slot.stack().getItem()).getMetal() == Metal.BENDALLOY) {
                    if(slot.stack().getTag().getInt("cooldown") < 20/(-tappingLevel)) {
                        slot.stack().getTag().putInt("cooldown", slot.stack().getTag().getInt("cooldown") + 1);
                    }
                    else{
                        player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel()-1);
                        slot.stack().getTag().putInt("cooldown", 1);
                    }
                    return;
                }
            }
        }
        else{
            for (SlotResult slot : curios) {
                if (slot.stack().getItem() instanceof MetalmindItem && ((MetalmindItem) slot.stack().getItem()).getMetal() == Metal.BENDALLOY) {
                    if(!player.getFoodData().needsFood()){
                        slot.stack().getTag().putInt("charges", slot.stack().getTag().getInt("charges") + tappingLevel);
                    }
                    if (slot.stack().getTag().getInt("cooldown") < 20 / tappingLevel) {
                        slot.stack().getTag().putInt("cooldown", slot.stack().getTag().getInt("cooldown") + 1);
                    } else {
                        if(player.getFoodData().needsFood()) {
                            player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 1);
                        }
                        slot.stack().getTag().putInt("cooldown", 1);
                    }
                }
            }
        }
    }

    public void adjustNicrosil(ServerPlayer player, int tappingLevel){
        List<SlotResult> curios = CuriosApi.getCuriosHelper().findCurios(player, "ring", "bracelet");
        if(tappingLevel < 0) {
            for (SlotResult slot : curios) {
                if (slot.stack().getItem() instanceof MetalmindItem && ((MetalmindItem) slot.stack().getItem()).getMetal() == Metal.NICROSIL) {
                    for(Metal metal : getPowers()){
                        slot.stack().getOrCreateTag().putInt(metal.getName()+"_feruchemy", slot.stack().getTag().getInt(metal.getName()+"_feruchemy")-tappingLevel);
                    }
                    slot.stack().getTag().putInt("charges", slot.stack().getTag().getInt("charges")-tappingLevel);
                    return;
                }
            }
        }
        else{
            for (SlotResult slot : curios) {
                boolean flag2 = false;
                if(flag){
                    flag2 = true;
                    flag = false;
                }
                if (slot.stack().getItem() instanceof MetalmindItem && ((MetalmindItem) slot.stack().getItem()).getMetal() == Metal.NICROSIL) {
                    for(Metal metal : Metal.values()){
                        int metalCharge = slot.stack().getOrCreateTag().getInt(metal.getName()+"_feruchemy");
                        if(metalCharge > 0 && !this.hasPowerDefault(metal)){
                            this.addPowerNicrosil(metal);
                            slot.stack().getTag().putInt(metal.getName()+"_feruchemy", metalCharge-tappingLevel);
                            if(metalCharge == 1) flag = true;
                        }
                        else{
                            this.revokePowerNicrosil(metal);
                        }
                    }
                    slot.stack().getTag().putInt("charges", slot.stack().getTag().getInt("charges")-tappingLevel);
                    return;
                }
            }
        }
    }

    @Override
    public void setBronzeSleepingTime(Player player, long currentTime){
        List<SlotResult> curios = CuriosApi.getCuriosHelper().findCurios(player, "ring", "bracelet");
        for(SlotResult slot: curios) {
            if (slot.stack().getItem() instanceof MetalmindItem && ((MetalmindItem) slot.stack().getItem()).getMetal() == Metal.BRONZE) {
                slot.stack().getTag().putInt("time", (int)currentTime);
            }
        }
    }

    @Override
    public void setBronzeWakeUpCharges(Player player, long dayTime){
        List<SlotResult> curios = CuriosApi.getCuriosHelper().findCurios(player, "ring", "bracelet");
        for(SlotResult slot: curios) {
            int currentCharges = slot.stack().getTag().getInt("charges");
            if (slot.stack().getItem() instanceof MetalmindItem && ((MetalmindItem) slot.stack().getItem()).getMetal() == Metal.BRONZE && currentCharges < ((MetalmindItem) slot.stack().getItem()).getMaxCharge()) {
                int diff = (int)dayTime - slot.stack().getTag().getInt("time");
                if(diff >= 0){
                    slot.stack().getTag().putInt("charges", Math.min(currentCharges + diff, ((MetalmindItem) slot.stack().getItem()).getMaxCharge()));
                }
                else{
                    slot.stack().getTag().putInt("charges", Math.min(currentCharges + 24000-(int)dayTime, ((MetalmindItem) slot.stack().getItem()).getMaxCharge()));
                }
            }
        }
    }

    @Override
    public int getAllCharges(Metal metal, LocalPlayer player) {
        int ret = 0;
        List<SlotResult> curios = CuriosApi.getCuriosHelper().findCurios(player, "ring", "bracelet");
        for(SlotResult slot: curios){
            if(slot.stack().getItem() instanceof MetalmindItem){
                if(((MetalmindItem) slot.stack().getItem()).getMetal() == metal) {
                    if(slot.stack().getOrCreateTag().getString("key").equals(player.getName().getString()) || slot.stack().getOrCreateTag().getString("key").equals("null")) {
                        ret += slot.stack().getTag().getInt("charges");
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public boolean hasMetalmind(Metal metal, ServerPlayer player) {
        List<SlotResult> curios = CuriosApi.getCuriosHelper().findCurios(player, "ring", "bracelet");
        for(SlotResult slot: curios){
            if(slot.stack().getItem() instanceof MetalmindItem){
                if(((MetalmindItem) slot.stack().getItem()).getMetal() == metal) {
                    if(slot.stack().getOrCreateTag().getString("key").equals(player.getName().getString()) || slot.stack().getOrCreateTag().getString("key").equals("null")) {
                        return true;
                    }
                    else if(slot.stack().getOrCreateTag().get("key") == null){
                        if(tappingLevel(Metal.ALUMINUM) < 0){
                            slot.stack().getOrCreateTag().putString("key", "null");
                        }
                        else{
                            slot.stack().getOrCreateTag().putString("key", player.getName().getString());
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean areMetalmindsFull(Metal metal, ServerPlayer player) {
        List<SlotResult> curios = CuriosApi.getCuriosHelper().findCurios(player, "ring", "bracelet");
        for(SlotResult slot: curios){
            if(slot.stack().getItem() instanceof MetalmindItem && ((MetalmindItem) slot.stack().getItem()).getMetal() == metal){
                if(slot.stack().getOrCreateTag().contains("charges") && slot.stack().getOrCreateTag().getInt("charges") < ((MetalmindItem) slot.stack().getItem()).getMaxCharge()) return false;
            }
        }
        return true;
    }

    @Override
    public int getMetalmindCharges(Metal metal, ServerPlayer player) {
        boolean flag = true;
        int charges = 0;
        List<SlotResult> curios = CuriosApi.getCuriosHelper().findCurios(player, "ring", "bracelet");
        for(SlotResult slot: curios){
            if(slot.stack().getItem() instanceof MetalmindItem && ((MetalmindItem) slot.stack().getItem()).getMetal() == metal){
                if(flag && slot.stack().getOrCreateTag().contains("charges")){
                    charges = slot.stack().getOrCreateTag().getInt("charges");
                    flag = false;
                }
                if(slot.stack().getOrCreateTag().contains("charges")){
                    if(slot.stack().getOrCreateTag().getInt("charges") < ((MetalmindItem) slot.stack().getItem()).getMaxCharge()){
                        return slot.stack().getOrCreateTag().getInt("charges");
                    }
                }
                else{
                    slot.stack().getTag().putInt("charges", 0);
                }
            }
        }
        return charges;
    }

    @Override
    public void adjustMetalmindCharges(Metal metal, int tappingLevel, ServerPlayer player) {
        List<SlotResult> curios = CuriosApi.getCuriosHelper().findCurios(player, "ring", "bracelet");
        if(tappingLevel > 0){
            for(SlotResult slot: curios){
                if(slot.stack().getItem() instanceof MetalmindItem  && ((MetalmindItem) slot.stack().getItem()).getMetal() == metal){
                    if(slot.stack().getOrCreateTag().contains("charges")){
                        int charges = slot.stack().getOrCreateTag().getInt("charges");
                        if (charges > 0){
                            slot.stack().getTag().putInt("charges", Math.max(charges - tappingLevel, 0));
                            return;
                        }
                    }
                    else{
                        slot.stack().getTag().putInt("charges", 0);
                        return;
                    }
                }
            }
        }
        else{
            for(SlotResult slot: curios){
                if(slot.stack().getItem() instanceof MetalmindItem && ((MetalmindItem) slot.stack().getItem()).getMetal() == metal){
                    if(slot.stack().getOrCreateTag().contains("charges")){
                        int charges = slot.stack().getOrCreateTag().getInt("charges");
                        if (charges < ((MetalmindItem) slot.stack().getItem()).getMaxCharge()){
                            slot.stack().getTag().putInt("charges", charges - tappingLevel);
                            return;
                        }
                    }
                    else{
                        slot.stack().getTag().putInt("charges", 1);
                        return;
                    }
                }
            }
        }

    }

    public static void removeEffects(ServerPlayer player, Metal metal){
        if(metal.getName().equals("pewter")){
            player.removeEffect(ModEffects.FERU_PEWTER.get());
            player.removeEffect(MobEffects.WEAKNESS);
        }
        else if(metal.getName().equals("iron")){
            player.removeEffect(MobEffects.SLOW_FALLING);
            player.removeEffect(ModEffects.HEAVY.get());
            player.removeEffect(MobEffects.JUMP);
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
        else if(metal.getName().equals("brass")){
            player.removeEffect(MobEffects.FIRE_RESISTANCE);
        }
        else if(metal.getName().equals("electrum")){
            player.removeEffect(ModEffects.DEPRESSED.get());
            player.removeEffect(ModEffects.MANIC.get());
        }
        else if(metal.getName().equals("atium")){
            player.removeEffect(ModEffects.YOUNG.get());
            player.removeEffect(ModEffects.OLD.get());
        }
        else if(metal.getName().equals("bronze")){
            player.removeEffect(MobEffects.BLINDNESS);
            player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        CompoundTag defaultMetals = (CompoundTag) nbt.get("defaultMetals");
        CompoundTag nicrosilMetals = (CompoundTag) nbt.get("nicrosilMetals");
        CompoundTag hemalurgyMetals = (CompoundTag) nbt.get("hemalurgyMetals");
        for (Metal mt : Metal.values()) {
            if (defaultMetals.getBoolean(mt.getName())) {
                this.addPower(mt);
            } else {
                this.revokePower(mt);
            }
            if (nicrosilMetals.getBoolean(mt.getName())) {
                this.addPowerNicrosil(mt);
            } else {
                this.revokePowerNicrosil(mt);
            }
            if (hemalurgyMetals.getBoolean(mt.getName())) {
                this.addPowerHemalurgy(mt);
            } else {
                this.revokePowerHemalurgy(mt);
            }
        }

        CompoundTag metal_tapping = (CompoundTag) nbt.get("metal_tapping");
        for (Metal mt : Metal.values()) {
            this.setTappingLevel(mt, metal_tapping.getInt(mt.getName()));
        }

        this.wasInvested = nbt.getBoolean("wasInvested");
    }

    @Override
    public CompoundTag save() {
        CompoundTag data = new CompoundTag();

        CompoundTag defaultMetals = new CompoundTag();
        CompoundTag nicrosilMetals = new CompoundTag();
        CompoundTag hemalurgyMetals = new CompoundTag();
        for (Metal mt : Metal.values()) {
            defaultMetals.putBoolean(mt.getName(), this.hasPowerDefault(mt));
            nicrosilMetals.putBoolean(mt.getName(), this.hasPowerNicrosil(mt));
            hemalurgyMetals.putBoolean(mt.getName(), this.hasPowerHemalurgy(mt));
        }
        data.put("defaultMetals", defaultMetals);
        data.put("nicrosilMetals", nicrosilMetals);
        data.put("hemalurgyMetals", hemalurgyMetals);

        CompoundTag metal_tapping = new CompoundTag();
        for (Metal mt : Metal.values()) {
            metal_tapping.putInt(mt.getName(), this.tappingLevel(mt));
        }
        data.put("metal_tapping", metal_tapping);
        data.putBoolean("wasInvested", this.wasEverInvested());
        return data;
    }

    @Override
    public boolean hasPower(Metal metal) {
        return this.hasPowerDefault(metal) || this.hasPowerNicrosil(metal) || this.hasPowerHemalurgy(metal);
    }

    @Override
    public boolean hasPowerNicrosil(Metal metal) {
        return this.feruchemical_powers_nicrosil[metal.getIndex()];
    }

    @Override
    public boolean hasPowerHemalurgy(Metal metal) {
        return this.feruchemical_powers_hemalurgy[metal.getIndex()];
    }

    @Override
    public boolean hasPowerDefault(Metal metal) {
        return this.feruchemical_powers[metal.getIndex()];
    }

    @Override
    public int getPowerCount() {
        int count = 0;
        for (boolean power : this.feruchemical_powers) {
            if (power) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Metal[] getPowers() {
        return Arrays.stream(Metal.values()).filter(this::hasPower).toArray(Metal[]::new);
    }

    @Override
    public void setFeruchemist() {
        Arrays.fill(this.feruchemical_powers, true);
    }

    @Override
    public boolean isFeruchemist() {
        for (boolean power : this.feruchemical_powers) {
            if (!power) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void addPower(Metal metal) {
        this.feruchemical_powers[metal.getIndex()] = true;
    }

    @Override
    public void addPowerNicrosil(Metal metal) {
        this.feruchemical_powers_nicrosil[metal.getIndex()] = true;
    }

    @Override
    public void addPowerHemalurgy(Metal metal) {
        this.feruchemical_powers_hemalurgy[metal.getIndex()] = true;
    }

    @Override
    public void revokePower(Metal metal) {
        this.feruchemical_powers[metal.getIndex()] = false;
    }

    @Override
    public void revokePowerNicrosil(Metal metal) {
        this.feruchemical_powers_nicrosil[metal.getIndex()] = false;
    }

    @Override
    public void revokePowerHemalurgy(Metal metal) {
        this.feruchemical_powers_hemalurgy[metal.getIndex()] = false;
    }

    @Override
    public int tappingLevel(Metal metal) {
        return this.tapping_metals[metal.getIndex()];
    }

    @Override
    public void setTappingLevel(Metal metal, int tappingLevel) {
        this.tapping_metals[metal.getIndex()] = tappingLevel;
    }

    @Override
    public void stopTapping(ServerPlayer player) {
        for(Metal metal: Metal.values()){
            this.tapping_metals[metal.getIndex()] = 0;
            removeEffects(player, metal);
        }
    }

    @Override
    public void setUninvested() {
        Arrays.fill(this.feruchemical_powers, false);
    }

    @Override
    public boolean isUninvested() {
        for (boolean power : this.feruchemical_powers) {
            if (power) {
                return false;
            }
        }
        return true;
    }

    public boolean hasBandsOfMourning(ServerPlayer player) {
        ItemStack offHand = player.getOffhandItem();
        ItemStack hand = player.getMainHandItem();
        return offHand.getItem().equals(ModItems.BANDS_OF_MOURNING.get()) || hand.getItem().equals(ModItems.BANDS_OF_MOURNING.get());
    }

    @Override
    public boolean wasEverInvested(){
        return wasInvested;
    }

    @Override
    public void investFirstTime(){
        wasInvested = true;
    }
}
