package com.extra.cosmerecraft.api.data;

import com.extra.cosmerecraft.api.enums.Metal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface IFeruchemyData {
    /**
     * Called each worldTick, checking the burn times, abilities, and metal
     * amounts. Then syncs to the client to make sure everyone is on the same
     * page
     *
     * param player the player being checked
     */
    void tickTapStore(ServerPlayer player);

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
     * Sets the player as a Feruchemist
     */
    void setFeruchemist();

    /**
     * Check if the player is a Feruchemist
     *
     * return true if the player has ALL powers
     */
    boolean isFeruchemist();

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
     * Checks if the player is tapping/storing the given metal
     *
     * param metal the Metal to check
     * returns int from -1 to 4
     */
    int tappingLevel(Metal metal);

    /**
     * Sets the player's tapping level for the given metal
     *
     * param metal        the Metal to set
     * param tappingLevel the level to set it to
     */
    void setTappingLevel(Metal metal, int tappingLevel);

    void stopTapping(ServerPlayer player);


    void setUninvested();

    boolean isUninvested();

    //returns true if player has a metalmind of param metal equipped
    boolean hasMetalmind(Metal metal, ServerPlayer player);

    //returns true if all metalminds of param metal are full
    boolean areMetalmindsFull(Metal metal, ServerPlayer player);

    //returns charges of the top-most not full metalmind of param metal, if all metalminds are full returns charges of top-most metalmind of param metal
    int getMetalmindCharges(Metal metal, ServerPlayer player);

    //adjusts charges of top-most metalmind of param metal
    void adjustMetalmindCharges(Metal metal, int tappingLevel, ServerPlayer player);

    void load(CompoundTag nbt);

    CompoundTag save();

    void setBronzeSleepingTime(Player player, long dayTime);

    void setBronzeWakeUpCharges(Player player, long dayTime);
}
