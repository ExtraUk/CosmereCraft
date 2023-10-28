package com.extra.cosmerecraft.event;

import com.extra.cosmerecraft.CosmereCraft;
import com.extra.cosmerecraft.allomancy.data.AllomancerCapability;
import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.client.KeyBindings;
import com.extra.cosmerecraft.client.gui.AllomancySelectScreen;
import com.extra.cosmerecraft.client.gui.FeruchemySelectScreen;
import com.extra.cosmerecraft.client.particle.option.TinParticleOption;
import com.extra.cosmerecraft.entity.client.ShadowModel;
import com.extra.cosmerecraft.entity.custom.ShadowEntity;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import com.extra.cosmerecraft.network.ModMessages;
import com.extra.cosmerecraft.network.UpdateSkinPacket;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.PlayLevelSoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientEvents {

    private final Minecraft mc = Minecraft.getInstance();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event){
        if(KeyBindings.FERUCHEMY_MENU_KEY.isDown()){
            if(this.mc.screen instanceof FeruchemySelectScreen){
                this.mc.setScreen(null);
            }
            else {
                this.mc.setScreen(new FeruchemySelectScreen());
            }
        }
        else if(KeyBindings.ALLOMANCY_MENU_KEY.isDown()){
            if(this.mc.screen instanceof AllomancySelectScreen){
                this.mc.setScreen(null);
            }
            else {
                this.mc.setScreen(new AllomancySelectScreen());

                ModMessages.sendToServer(new UpdateSkinPacket(this.mc.player.getSkinTextureLocation())); //Si funciona no esta mal dijo un sabio
                this.mc.player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(data ->{
                    data.setSkin(this.mc.player.getSkinTextureLocation());
                });
            }
        }

    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onPlaySound(PlayLevelSoundEvent.AtPosition event){
        SoundSource source = event.getSource();
        Vec3 soundPosition = event.getPosition();
        short radius = 40;
        if(source == SoundSource.HOSTILE || source == SoundSource.PLAYERS || source == SoundSource.NEUTRAL || source == SoundSource.VOICE || source == SoundSource.BLOCKS) {
            for (Entity entity : event.getLevel().getEntities(null, new AABB(soundPosition.subtract(radius, radius, radius), soundPosition.add(radius, radius, radius)))) {
                if (entity instanceof Player player) {
                    if(soundPosition.distanceTo(player.position()) > 1)
                    player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(data -> {
                        if (data.isBurning(Metal.TIN)) {
                            event.getLevel().addAlwaysVisibleParticle(
                                new TinParticleOption(new EntityPositionSource(player, 1.5f), (int)(3*soundPosition.distanceTo(player.position())), source),
                                soundPosition.x, soundPosition.y, soundPosition.z,
                                0, 0, 0);
                        }
                    });
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre<ShadowEntity, ShadowModel> event){
        if(this.mc.player != null) {
            if(event.getEntity() instanceof ShadowEntity shadow) {
                AtomicBoolean cancel = new AtomicBoolean(true);
                this.mc.player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(data -> {
                    if(data.getShadowUUID().equals(shadow.getPlayerUUID())) {
                        if (data.isBurning(shadow.getMetal())) {
                            cancel.set(false);
                        }
                    }
                });
                event.setCanceled(cancel.get());
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderFog(ViewportEvent.RenderFog event){
        this.mc.player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
            int tl = data.tappingLevel(Metal.TIN);
            if(tl < 0){
                event.setFogShape(FogShape.SPHERE);
                event.setNearPlaneDistance(0);
                event.setFarPlaneDistance(60+(20*tl));
                event.setCanceled(true);
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderFOV(ViewportEvent.ComputeFov event){
        this.mc.player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
            if(data.tappingLevel(Metal.TIN) > 0){
                event.setFOV(event.getFOV()/(3* data.tappingLevel(Metal.TIN)));
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onRenderLevelStage(final RenderLevelStageEvent event){
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }

        Player player = this.mc.player;
        if (player == null || !player.isAlive() || this.mc.options.getCameraType().isMirrored()) {
            return;
        }
        player.getCapability(AllomancerCapability.PLAYER_CAP_ALLOMANCY).ifPresent(data -> {
            if(data.isBurning(Metal.IRON) || data.isBurning(Metal.STEEL)){
                PoseStack stack = setupPoseStack(event);
                double rho = 1; //Se agradece
                float theta = (float) ((this.mc.player.getViewYRot(event.getPartialTick()) + 90) * Math.PI / 180);
                float phi = Mth.clamp((float) ((this.mc.player.getViewXRot(event.getPartialTick()) + 90) * Math.PI / 180), 0.0001F, 3.14F);

                Vec3 playerPos = this.mc.cameraEntity
                        .getEyePosition(event.getPartialTick())
                        .add(rho * Mth.sin(phi) * Mth.cos(theta), rho * Mth.cos(phi) - 0.35F, rho * Mth.sin(phi) * Mth.sin(theta));
                drawLine(stack, playerPos, new Vec3(0,0,0), 0,0,1);
                teardownPoseStack(stack);
            }
        });

    }



    public void drawLine(PoseStack poseStack, Vec3 playerPos, Vec3 targetPos, float r, float g, float b){
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();

        builder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix4f = poseStack.last().pose();
        builder.vertex(matrix4f, (float) playerPos.x, (float) playerPos.y, (float) playerPos.z).color(r, g, b, 1f).endVertex();
        builder.vertex(matrix4f, (float) targetPos.x, (float) targetPos.y, (float) targetPos.z).color(r, g, b, 1f).endVertex();
        RenderSystem.lineWidth(1);

        tessellator.end();
    }

    private PoseStack setupPoseStack(final RenderLevelStageEvent event) {
        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.disablePolygonOffset();
        RenderSystem.defaultBlendFunc();

        PoseStack stack = event.getPoseStack();
        stack.pushPose();
        Vec3 view = this.mc.cameraEntity.getEyePosition(event.getPartialTick());
        stack.translate(-view.x, -view.y, -view.z);
        RenderSystem.applyModelViewMatrix();
        return stack;
    }

    private static void teardownPoseStack(PoseStack stack) {
        stack.popPose();
        RenderSystem.applyModelViewMatrix();

        RenderSystem.disableBlend();
        RenderSystem.enablePolygonOffset();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();

    }

    @Mod.EventBusSubscriber(modid = CosmereCraft.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents{
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event){
            event.register(KeyBindings.FERUCHEMY_MENU_KEY);
            event.register(KeyBindings.ALLOMANCY_MENU_KEY);
        }
    }

}
