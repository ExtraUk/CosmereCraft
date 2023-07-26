package com.extra.cosmerecraft.network;

import com.extra.cosmerecraft.allomancy.data.AllomancerCapability;
import com.extra.cosmerecraft.api.data.IAllomancyData;
import com.extra.cosmerecraft.api.data.IFeruchemyData;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class AllomancerDataPacket {
    private final CompoundTag nbt;
    private final UUID uuid;

    /**
     * Packet for sending Allomancy player data to a client
     *
     * @param data   the IAllomancerData data for the player
     * @param player the player
     */
    public AllomancerDataPacket(IAllomancyData data, Player player) {
        this.uuid = player.getUUID();
        this.nbt = (data != null && AllomancerCapability.PLAYER_CAP_ALLOMANCY != null) ? data.save() : new CompoundTag();

    }

    private AllomancerDataPacket(CompoundTag nbt, UUID uuid) {
        this.nbt = nbt;
        this.uuid = uuid;
    }

    public static AllomancerDataPacket decode(FriendlyByteBuf buf) {
        return new AllomancerDataPacket(buf.readNbt(), buf.readUUID());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.nbt);
        buf.writeUUID(this.uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().level.getPlayerByUUID(this.uuid);
            if (player != null && AllomancerCapability.PLAYER_CAP_ALLOMANCY != null) {
                player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(data -> data.load(this.nbt));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
