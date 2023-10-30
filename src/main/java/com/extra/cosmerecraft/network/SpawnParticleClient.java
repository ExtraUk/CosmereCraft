package com.extra.cosmerecraft.network;

import com.extra.cosmerecraft.allomancy.data.AllomancerCapability;
import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.api.enums.Power;
import com.extra.cosmerecraft.client.particle.option.BronzeParticleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class SpawnParticleClient {
    private final double x;
    private final double y;
    private final double z;
    private final Power power;
    private final int powerOrd;

    public SpawnParticleClient(double x, double y, double z, Power power, int powerOrd) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.power = power;
        this.powerOrd = powerOrd;
    }

    public static SpawnParticleClient decode(FriendlyByteBuf buf) {
        return new SpawnParticleClient(buf.readDouble(), buf.readDouble(),buf.readDouble(), buf.readEnum(Power.class), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeEnum(this.power);
        buf.writeInt(this.powerOrd);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if(player != null) {
                double distance = player.position().distanceTo(new Vec3(this.x, this.y, this.z));
                player.level.addAlwaysVisibleParticle(new BronzeParticleOption(new EntityPositionSource(player, 1.5f), (int) (3 * distance), this.power, this.powerOrd),
                        this.x, this.y+1, this.z,
                        0, 0, 0);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
