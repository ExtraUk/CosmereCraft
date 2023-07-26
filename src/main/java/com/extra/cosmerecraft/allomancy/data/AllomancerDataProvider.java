package com.extra.cosmerecraft.allomancy.data;

import com.extra.cosmerecraft.api.data.IAllomancyData;
import com.extra.cosmerecraft.api.data.IFeruchemyData;
import com.extra.cosmerecraft.feruchemy.data.DefaultFeruchemistData;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AllomancerDataProvider implements ICapabilitySerializable<CompoundTag> {

    private final DefaultAllomancerData data = new DefaultAllomancerData();
    private final LazyOptional<IAllomancyData> dataOptional = LazyOptional.of(() -> this.data);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return AllomancerCapability.PLAYER_CAP_ALLOMANCY.orEmpty(cap, this.dataOptional.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        if (AllomancerCapability.PLAYER_CAP_ALLOMANCY == null) {
            return new CompoundTag();
        } else {
            return this.data.save();
        }
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (AllomancerCapability.PLAYER_CAP_ALLOMANCY != null) {
            this.data.load(nbt);
        }
    }

    public void invalidate() {
        this.dataOptional.invalidate();
    }
}
