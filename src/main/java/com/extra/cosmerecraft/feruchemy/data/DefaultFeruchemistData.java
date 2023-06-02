package com.extra.cosmerecraft.feruchemy.data;

import com.extra.cosmerecraft.api.data.IFeruchemyData;
import com.extra.cosmerecraft.api.enums.Metal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;

public class DefaultFeruchemistData implements IFeruchemyData {

    private final boolean[] feruchemical_powers;
    private final int[] tapping_metals;

    public DefaultFeruchemistData(){
        int powers = Metal.values().length;
        this.feruchemical_powers = new boolean[powers];
        Arrays.fill(this.feruchemical_powers, false);

        this.tapping_metals = new int[powers];
        Arrays.fill(this.tapping_metals, 0);
    }

    @Override
    public void tickTapStore(ServerPlayer player) {
        boolean sync = false;
        for (Metal metal : Metal.values()) {
            if (this.tappingLevel(metal) != 0) {
                if (!this.hasPower(metal)) {
                    this.setTappingLevel(metal, 0);
                    sync = true;
                }
                else {
                    //Add or remove charges from a metalmind
                    /*this.setBurnTime(metal, this.getBurnTime(metal) - 1);
                    if (this.getBurnTime(metal) <= 0) {
                        if (this.getAmount(metal) <= 0) {
                            this.setBurning(metal, false);
                        } else {
                            this.setAmount(metal, this.getAmount(metal) - 1);
                        }
                        sync = true;
                        this.setBurnTime(metal, MAX_BURN_TIME[metal.getIndex()]);
                    }*/
                }
            }
        }
        /*if (sync) {
            Network.sync(this, player);
        }*/
    }

    @Override
    public boolean hasPower(Metal metal) {
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
    public void revokePower(Metal metal) {
        this.feruchemical_powers[metal.getIndex()] = false;
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

    @Override
    public void load(CompoundTag nbt) {
        CompoundTag abilities = (CompoundTag) nbt.get("abilities");
        for (Metal mt : Metal.values()) {
            if (abilities.getBoolean(mt.getName())) {
                this.addPower(mt);
            } else {
                this.revokePower(mt);
            }
        }

        CompoundTag metal_tapping = (CompoundTag) nbt.get("metal_tapping");
        for (Metal mt : Metal.values()) {
            this.setTappingLevel(mt, metal_tapping.getInt(mt.getName()));
        }
    }

    @Override
    public CompoundTag save() {
        CompoundTag data = new CompoundTag();

        CompoundTag abilities = new CompoundTag();
        for (Metal mt : Metal.values()) {
            abilities.putBoolean(mt.getName(), this.hasPower(mt));
        }
        data.put("abilities", abilities);

        CompoundTag metal_tapping = new CompoundTag();
        for (Metal mt : Metal.values()) {
            metal_tapping.putInt(mt.getName(), this.tappingLevel(mt));
        }
        data.put("metal_tapping", metal_tapping);

        return data;
    }
}
