package com.extra.cosmerecraft.client.particle.option;

import com.extra.cosmerecraft.api.enums.Power;
import com.extra.cosmerecraft.client.particle.ModParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.phys.Vec3;

import java.util.Locale;

public class BronzeParticleOption implements ParticleOptions {
    public static final Codec<BronzeParticleOption> CODEC = RecordCodecBuilder.create((p_235978_) -> {
        return p_235978_.group(PositionSource.CODEC.fieldOf("destination").forGetter((p_235982_) -> {
            return p_235982_.destination;
        }), Codec.INT.fieldOf("arrival_in_ticks").forGetter((p_235980_) -> {
            return p_235980_.arrivalInTicks;
        })).apply(p_235978_, BronzeParticleOption::new);
    });
    public static final ParticleOptions.Deserializer<BronzeParticleOption> DESERIALIZER = new ParticleOptions.Deserializer<BronzeParticleOption>() {
        public BronzeParticleOption fromCommand(ParticleType<BronzeParticleOption> p_175859_, StringReader p_175860_) throws CommandSyntaxException {
            p_175860_.expect(' ');
            float f = (float)p_175860_.readDouble();
            p_175860_.expect(' ');
            float f1 = (float)p_175860_.readDouble();
            p_175860_.expect(' ');
            float f2 = (float)p_175860_.readDouble();
            p_175860_.expect(' ');
            int i = p_175860_.readInt();
            p_175860_.expect(' ');
            Power power = Power.getPower(p_175860_.readInt());
            p_175860_.expect(' ');
            int powerOrd = p_175860_.readInt();
            BlockPos blockpos = new BlockPos((double)f, (double)f1, (double)f2);
            return new BronzeParticleOption(new BlockPositionSource(blockpos), i, power, powerOrd);
        }

        public BronzeParticleOption fromNetwork(ParticleType<BronzeParticleOption> p_175862_, FriendlyByteBuf p_175863_) {
            PositionSource positionsource = PositionSourceType.fromNetwork(p_175863_);
            int i = p_175863_.readVarInt();
            Power power = Power.getPower(p_175863_.readVarInt());
            int powerOrd = p_175863_.readVarInt();
            return new BronzeParticleOption(positionsource, i, power, powerOrd);
        }
    };
    private final PositionSource destination;
    private final Power power;
    private final int powerOrdinal;
    private final int arrivalInTicks;

    public BronzeParticleOption(PositionSource p_235975_, int p_235976_, Power pPower, int pPowerOrdinal) {
        this.destination = p_235975_;
        this.arrivalInTicks = p_235976_;
        this.power = pPower;
        this.powerOrdinal = pPowerOrdinal;
    }

    public BronzeParticleOption(PositionSource p_235975_, int p_235976_) {
        this.destination = p_235975_;
        this.arrivalInTicks = p_235976_;
        this.power = Power.ALLOMANCY;
        this.powerOrdinal = 0;
    }

    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        PositionSourceType.toNetwork(this.destination, pBuffer);
        pBuffer.writeVarInt(this.arrivalInTicks);
        pBuffer.writeInt(this.power.getIndex());
        pBuffer.writeInt(this.powerOrdinal);
    }

    public String writeToString() {
        Vec3 vec3 = this.destination.getPosition((Level)null).get();
        double d0 = vec3.x();
        double d1 = vec3.y();
        double d2 = vec3.z();
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %d %d %d", Registry.PARTICLE_TYPE.getKey(this.getType()), d0, d1, d2, this.arrivalInTicks, this.power.getIndex(), this.powerOrdinal);
    }

    public ParticleType<BronzeParticleOption> getType() {
        return ModParticles.BRONZE_PARTICLE.get();
    }

    public PositionSource getDestination() {
        return this.destination;
    }

    public int getArrivalInTicks() {
        return this.arrivalInTicks;
    }

    public Power getPower(){
        return this.power;
    }

    public int getPowerOrdinal(){
        return this.powerOrdinal;
    }
}
