package com.extra.cosmerecraft.api.data;

import com.extra.cosmerecraft.api.enums.Metal;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public interface IAllomancyData {
    /**
     * Called each worldTick, checking the burn times, abilities, and metal
     * amounts. Then syncs to the client to make sure everyone is on the same
     * page
     *
     * param player the player being checked
     */
    void tickBurn(ServerPlayer player);

    /**
     * Get if the player has the supplied power
     *
     * param metal the Metal to check
     * return true if this capability has the power specified
     */
    boolean hasPower(Metal metal);

    boolean hasPowerNicrosil(Metal metal);

    boolean hasPowerHemalurgy(Metal metal);

    boolean hasPowerDefault(Metal metal);

    /**
     * Get the number of powers the player has
     *
     * return int between 0 and 16, inclusive
     */
    int getPowerCount();

    /**
     * Returns an array of the player's metal abilities
     *
     * return array of Metal
     */
    Metal[] getPowers();

    /**
     * Sets the player as a Mistborn
     */
    void setMistborn();

    /**
     * Check if the player is a Mistborn
     *
     * return true if the player has ALL powers
     */
    boolean isMistborn();

    /**
     * Grant this player the given metal power
     *
     * param metal the Metal to add
     */
    void addPower(Metal metal);

    void addPowerNicrosil(Metal metal);

    void addPowerHemalurgy(Metal metal);

    /**
     * Remove the given metal power from this player
     *
     * param metal the Metal to remove
     */
    void revokePower(Metal metal);

    void revokePowerNicrosil(Metal metal);

    void revokePowerHemalurgy(Metal metal);

    /**
     * Checks if the player is burning the given metal
     *
     * param metal the Metal to check
     *
     */
    boolean isBurning(Metal metal);

    /**
     * Sets the player's burn for the given metal
     *
     * param metal        the Metal to set
     *
     */
    void setBurning(Metal metal, boolean burn);

    //Stops Burning all metals
    void stopBurning(ServerPlayer player);

    int getMetalReserves(Metal metal);

    void adjustMetalReserves(ServerPlayer player, Metal metal);

    //Removes all abilites
    void setUninvested();

    //Checks if player has any ability
    boolean isUninvested();

    //Checks if player ever had any ability
    boolean wasEverInvested();

    void investFirstTime();

    void wipeReserves(ServerPlayer player);

    public void setMetalReserves(Metal metal, int value);

    void load(CompoundTag nbt);

    CompoundTag save();

    void removeEffects(ServerPlayer player, Metal mt);

    Vec3 getDeathLoc();

    void setDeathLoc(int x, int y, int z);

    void setSkin(ResourceLocation skin);

    ResourceLocation getSkin();

    UUID getShadowUUID();

    void setShadowUUID(UUID uuid);
}
