package com.extra.cosmerecraft.network;

import com.extra.cosmerecraft.api.data.IFeruchemyData;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class FeruchemistDataPacket {
    private final CompoundTag nbt;
    private final UUID uuid;

    /**
     * Packet for sending Allomancy player data to a client
     *
     * @param data   the IAllomancerData data for the player
     * @param player the player
     */
    public FeruchemistDataPacket(IFeruchemyData data, Player player) {
        this.uuid = player.getUUID();
        this.nbt = (data != null && FeruchemistCapability.PLAYER_CAP_FERUCHEMY != null) ? data.save() : new CompoundTag();

    }

    private FeruchemistDataPacket(CompoundTag nbt, UUID uuid) {
        this.nbt = nbt;
        this.uuid = uuid;
    }

    public static FeruchemistDataPacket decode(FriendlyByteBuf buf) {
        return new FeruchemistDataPacket(buf.readNbt(), buf.readUUID());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.nbt);
        buf.writeUUID(this.uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().level.getPlayerByUUID(this.uuid);
            if (player != null && FeruchemistCapability.PLAYER_CAP_FERUCHEMY != null) {
                player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> data.load(this.nbt));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
