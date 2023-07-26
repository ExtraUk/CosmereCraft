package com.extra.cosmerecraft.allomancy.data;

import com.extra.cosmerecraft.api.data.IAllomancyData;
import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import com.extra.cosmerecraft.item.ModItems;
import com.extra.cosmerecraft.network.ModMessages;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultAllomancerData implements IAllomancyData {

    private final boolean[] allomantic_powers;
    private final boolean[] allomantic_powers_nicrosil;
    private final boolean[] allomantic_powers_hemalurgy;
    private final boolean[] burning_metals;
    private final int[] allomantic_reserves;
    private final int[] max_allomantic_reserves;
    private boolean wasInvested = false;

    public DefaultAllomancerData(){
        int powers = Metal.values().length;
        this.allomantic_powers = new boolean[powers];
        this.allomantic_powers_nicrosil = new boolean[powers];
        this.allomantic_powers_hemalurgy = new boolean[powers];
        Arrays.fill(this.allomantic_powers, false);
        Arrays.fill(this.allomantic_powers_nicrosil, false);
        Arrays.fill(this.allomantic_powers_hemalurgy, false);

        this.burning_metals = new boolean[powers];
        Arrays.fill(this.burning_metals, false);

        this.allomantic_reserves = new int[powers];
        Arrays.fill(this.allomantic_reserves, 0);

        this.max_allomantic_reserves = new int[powers];
        Arrays.fill(this.max_allomantic_reserves, 24000);
    }

    @Override
    public void tickBurn(ServerPlayer player) {
        AtomicInteger FeruchemicalNicrosilTappingLevel = new AtomicInteger();
        FeruchemicalNicrosilTappingLevel.set(0);
        player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
            FeruchemicalNicrosilTappingLevel.set(data.tappingLevel(Metal.NICROSIL));
        });
        boolean sync = false;
        if(!hasBandsOfMourning(player)) {
            if(FeruchemicalNicrosilTappingLevel.get() >= 0) {
                for (Metal metal : Metal.values()) {
                    boolean isBurning = this.isBurning(metal);
                    if (isBurning) {
                        int reserves = this.getMetalReserves(metal);
                        if (!this.hasPower(metal) || reserves <= 0) {
                            this.setBurning(metal, false);
                            sync = true;
                        }
                        else {
                            adjustMetalReserves(player, metal);
                            sync = true;
                        }
                    }
                    if (sync) {
                        ModMessages.sync(this, player);
                    }
                }
            }
            else {
                this.stopBurning(player);
            }
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

        CompoundTag max_allomantic_reserves = (CompoundTag) nbt.get("max_allomantic_reserves");
        for (Metal mt : Metal.values()) {
            this.max_allomantic_reserves[mt.getIndex()] = max_allomantic_reserves.getInt(mt.getName());
        }

        CompoundTag allomantic_reserves = (CompoundTag) nbt.get("allomantic_reserves");
        for (Metal mt : Metal.values()) {
            this.setMetalReserves(mt, allomantic_reserves.getInt(mt.getName()));
        }

        CompoundTag metal_burning = (CompoundTag) nbt.get("metal_burning");
        for (Metal mt : Metal.values()) {
            this.setBurning(mt, metal_burning.getBoolean(mt.getName()));
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

        CompoundTag allomantic_reserves = new CompoundTag();
        for (Metal mt : Metal.values()) {
            allomantic_reserves.putInt(mt.getName(), this.getMetalReserves(mt));
        }
        data.put("allomantic_reserves", allomantic_reserves);

        CompoundTag max_allomantic_reserves = new CompoundTag();
        for (Metal mt : Metal.values()) {
            max_allomantic_reserves.putInt(mt.getName(), this.getMetalReserves(mt));
        }
        data.put("max_allomantic_reserves", max_allomantic_reserves);

        CompoundTag metal_burning = new CompoundTag();
        for (Metal mt : Metal.values()) {
            metal_burning.putBoolean(mt.getName(), this.isBurning(mt));
        }
        data.put("metal_burning", metal_burning);
        data.putBoolean("wasInvested", this.wasEverInvested());

        return data;
    }

    @Override
    public boolean hasPower(Metal metal) {
        return this.hasPowerDefault(metal) || this.hasPowerNicrosil(metal) || this.hasPowerHemalurgy(metal);
    }

    @Override
    public boolean hasPowerNicrosil(Metal metal) {
        return this.allomantic_powers_nicrosil[metal.getIndex()];
    }

    @Override
    public boolean hasPowerHemalurgy(Metal metal) {
        return this.allomantic_powers_hemalurgy[metal.getIndex()];
    }

    @Override
    public boolean hasPowerDefault(Metal metal) {
        return this.allomantic_powers[metal.getIndex()];
    }

    @Override
    public int getPowerCount() {
        int count = 0;
        for (boolean power : this.allomantic_powers) {
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
    public void setMistborn() {
        Arrays.fill(this.allomantic_powers, true);
    }

    @Override
    public boolean isMistborn() {
        for (boolean power : this.allomantic_powers) {
            if (!power) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void addPower(Metal metal) {
        this.allomantic_powers[metal.getIndex()] = true;
    }

    @Override
    public void addPowerNicrosil(Metal metal) {
        this.allomantic_powers_nicrosil[metal.getIndex()] = true;
    }

    @Override
    public void addPowerHemalurgy(Metal metal) {
        this.allomantic_powers_hemalurgy[metal.getIndex()] = true;
    }

    @Override
    public void revokePower(Metal metal) {
        this.allomantic_powers[metal.getIndex()] = false;
    }

    @Override
    public void revokePowerNicrosil(Metal metal) {
        this.allomantic_powers_nicrosil[metal.getIndex()] = false;
    }

    @Override
    public void revokePowerHemalurgy(Metal metal) {
        this.allomantic_powers_hemalurgy[metal.getIndex()] = false;
    }

    @Override
    public boolean isBurning(Metal metal) {
        return this.burning_metals[metal.getIndex()];
    }

    @Override
    public void setBurning(Metal metal, boolean burn) {
        this.burning_metals[metal.getIndex()] = burn;
    }

    @Override
    public void stopBurning(ServerPlayer player) {
        for(Metal metal: Metal.values()){
            this.burning_metals[metal.getIndex()] = false;
        }
    }

    @Override
    public int getMetalReserves(Metal metal) {
        return this.allomantic_reserves[metal.getIndex()];
    }

    @Override
    public void adjustMetalReserves(ServerPlayer player, Metal metal) {
        int newReserves = getMetalReserves(metal) - metal.getAllomanticDrain();
        if(newReserves < 0){
            this.allomantic_reserves[metal.getIndex()] = 0;
        }
        else{
            this.allomantic_reserves[metal.getIndex()] = newReserves;
        }
    }

    @Override
    public void setUninvested() {
        Arrays.fill(this.allomantic_powers, false);
    }

    @Override
    public boolean isUninvested() {
        for (boolean power : this.allomantic_powers) {
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

    @Override
    public void wipeReserves() {
        Arrays.fill(this.allomantic_reserves, 0);
    }

    @Override
    public void setMetalReserves(Metal metal, int value) {
        int i = metal.getIndex();
        if(value > this.max_allomantic_reserves[i]){
            this.allomantic_reserves[i] = this.max_allomantic_reserves[i];
        }
        else{
            this.allomantic_reserves[i] = value;
        }
    }
}
