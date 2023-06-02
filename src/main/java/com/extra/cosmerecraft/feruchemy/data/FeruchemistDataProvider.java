package com.extra.cosmerecraft.feruchemy.data;

import com.extra.cosmerecraft.api.data.IFeruchemyData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FeruchemistDataProvider implements ICapabilitySerializable<CompoundTag> {

    private final DefaultFeruchemistData data = new DefaultFeruchemistData();
    private final LazyOptional<IFeruchemyData> dataOptional = LazyOptional.of(() -> this.data);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return FeruchemistCapability.PLAYER_CAP_FERUCHEMY.orEmpty(cap, this.dataOptional.cast());
    }

    @Override
    public CompoundTag serializeNBT() {
        if (FeruchemistCapability.PLAYER_CAP_FERUCHEMY == null) {
            return new CompoundTag();
        } else {
            return this.data.save();
        }
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (FeruchemistCapability.PLAYER_CAP_FERUCHEMY != null) {
            this.data.load(nbt);
        }
    }

    public void invalidate() {
        this.dataOptional.invalidate();
    }
}
