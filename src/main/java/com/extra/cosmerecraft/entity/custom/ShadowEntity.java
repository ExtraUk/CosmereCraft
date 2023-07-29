package com.extra.cosmerecraft.entity.custom;


import com.extra.cosmerecraft.CosmereCraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.util.UUID;

public class ShadowEntity extends Mob implements IEntityAdditionalSpawnData {
    private ResourceLocation skin = new ResourceLocation(CosmereCraft.MODID, "textures/entities/shadow_entity.png");
    private int lifeTimer = 1;
    private UUID playerUUID = this.uuid;

    public ShadowEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
       /* lifeTimer--;
        if(lifeTimer <= 0){
            this.discard();
        }*/
    }

    @Override
    protected void registerGoals() { }

    public static AttributeSupplier.Builder getShadowAttributes(){
        return Mob.createMobAttributes().add(ForgeMod.ENTITY_GRAVITY.get(), 8).add(Attributes.MAX_HEALTH, 10000).add(Attributes.MOVEMENT_SPEED, 0);
    }

    public void setSkin(ResourceLocation skin){
        this.skin = skin;
    }

    public ResourceLocation getSkin(){
        return this.skin;
    }

    public void setPlayerUUID(UUID uuid){
        this.playerUUID = uuid;
    }

    public UUID getPlayerUUID(){
        return this.playerUUID;
    }


    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.playerUUID);
        buffer.writeResourceLocation(this.skin);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.playerUUID = additionalData.readUUID();
        this.skin = additionalData.readResourceLocation();
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
