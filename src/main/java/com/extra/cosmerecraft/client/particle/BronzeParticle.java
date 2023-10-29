package com.extra.cosmerecraft.client.particle;

import com.extra.cosmerecraft.api.enums.Power;
import com.extra.cosmerecraft.client.particle.option.BronzeParticleOption;
import com.extra.cosmerecraft.client.particle.option.TinParticleOption;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;
import java.util.function.Consumer;

public class BronzeParticle extends TextureSheetParticle {
    private final PositionSource target;
    private float yRot;
    private float yRotO;

    public BronzeParticle(ClientLevel pLevel, double pX, double pY, double pZ, PositionSource pTarget, int pLifetime, Power power, int powerOrd) {
        super(pLevel, pX, pY, pZ);
        this.quadSize = 0.3F;
        this.target = pTarget;
        this.lifetime = pLifetime;

        this.setColor(power.getRed(), power.getGreen(), power.getGreen());
        //set sprite using powerOrd
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }


    public int getLightColor(float pPartialTick) {
        return 240;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            Optional<Vec3> optional = this.target.getPosition(this.level);
            if (optional.isEmpty()) {
                this.remove();
            } else {
                int i = this.lifetime - this.age;
                double d0 = 1.0D / (double)i;
                Vec3 vec3 = optional.get();
                this.x = Mth.lerp(d0, this.x, vec3.x());
                this.y = Mth.lerp(d0, this.y, vec3.y());
                this.z = Mth.lerp(d0, this.z, vec3.z());
                this.setPos(this.x, this.y, this.z); // FORGE: update the particle's bounding box
                this.yRotO = this.yRot;
                this.yRot = (float)Mth.atan2(this.x - vec3.x(), this.z - vec3.z());
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<BronzeParticleOption> {
        private final SpriteSet sprite;

        public Provider(SpriteSet pSprites) {
            this.sprite = pSprites;
        }

        public Particle createParticle(BronzeParticleOption pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            BronzeParticle bronzeParticle = new BronzeParticle(pLevel, pX, pY, pZ, pType.getDestination(), pType.getArrivalInTicks(), pType.getPower(), pType.getPowerOrdinal());
            bronzeParticle.pickSprite(this.sprite);
            bronzeParticle.setAlpha(1.0F);
            return bronzeParticle;
        }
    }
}
