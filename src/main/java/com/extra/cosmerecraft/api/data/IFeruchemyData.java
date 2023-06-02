package com.extra.cosmerecraft.api.data;

import com.extra.cosmerecraft.api.enums.Metal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

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

    /**
     * Remove the given metal power from this player
     *
     * param metal the Metal to remove
     */
    void revokePower(Metal metal);

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


    void setUninvested();

    boolean isUninvested();
    /**
     * Sets the players amount of Metal to the given value
     *
     * param metal the Metal to set
     * param amt   the amount stored
     */
    /*void setAmount(Metal metal, int amt);

    /**
     * Gets the players stored amount of the given metal
     *
     * param metal the Metal to check
     * return the amount stored

    int getAmount(Metal metal);

    /**
     * Drain all specified metals
     *
     * param metals all metals to drain
     *
    void drainMetals(Metal... metals);

    void decEnhanced();

    boolean isEnhanced();

    void setEnhanced(int time);*/

    void load(CompoundTag nbt);

    CompoundTag save();
}
