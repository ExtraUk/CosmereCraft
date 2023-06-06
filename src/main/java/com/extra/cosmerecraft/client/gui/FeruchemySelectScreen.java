package com.extra.cosmerecraft.client.gui;

import com.extra.cosmerecraft.api.data.IFeruchemyData;
import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import com.extra.cosmerecraft.network.ModMessages;
import com.extra.cosmerecraft.network.UpdateTappingPacket;
import com.google.common.graph.Network;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;

public class FeruchemySelectScreen extends Screen {

    private static final String[] METALS = Arrays.stream(Metal.values()).map(Metal::getName).toArray(String[]::new);
    private static final String[] METAL_NAMES = Arrays.stream(Metal.values()).map(Metal::getName).toArray(String[]::new);
    private static final String[] METAL_LOCAL = Arrays.stream(METAL_NAMES).map(s -> "metals." + s).toArray(String[]::new);
    private static final String GUI_METAL = "cosmerecraft:textures/gui/metals/%s_feruchemy.png";
    private static final ResourceLocation[] METAL_ICONS = Arrays.stream(METAL_NAMES).map(s -> new ResourceLocation(String.format(GUI_METAL, s))).toArray(ResourceLocation[]::new);
    final Minecraft mc;
    int slotSelected = -1;

    public FeruchemySelectScreen() {
        super(Component.translatable("feruchemy_gui"));
        this.mc = Minecraft.getInstance();
    }


    @Override
    public void render(PoseStack matrixStack, int mx, int my, float partialTicks){
        super.render(matrixStack, mx, my ,partialTicks);

        int x = this.width / 2;
        int y = this.height / 2;
        int maxRadius = 80;

        double angle = mouseAngle(x, y, mx, my);

        int segments = METALS.length;
        float step = (float) Math.PI / 180;
        float degPer = (float) Math.PI * 2 / segments;

        this.slotSelected = -1;

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.getBuilder();

        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        buf.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);

        for (int seg = 0; seg < segments; seg++) {
            Metal mt = Metal.getMetal(toMetalIndex(seg));
            boolean mouseInSector = /*data.hasPower(mt) && */(degPer * seg < angle && angle < degPer * (seg + 1));
            float radius = 80;//Math.max(0F, Math.min((this.timeIn + partialTicks - seg * 6F / segments) * 40F, maxRadius));
            if (mouseInSector) {
                this.slotSelected = seg;
                radius *= 1.025f;
            }

            int gs = 0x55;
            if (seg % 2 == 0) {
                gs += 0x19;
            }

            //gs = (!data.hasPower(mt) || data.getAmount(mt) == 0) ? 0 : gs;

            int r = gs;//data.isBurning(mt) ? 0xFF : gs;
            int g = gs;
            int b = gs;
            int a = 0x99;

            if (seg == 0) {
                buf.vertex(x, y, 0).color(0x19, 0x19, 0x19, 0x15).endVertex();
            }

            for (float v = 0; v < degPer + step / 2; v += step) {
                float rad = v + seg * degPer;
                float xp = x + Mth.cos(rad) * radius;
                float yp = y + Mth.sin(rad) * radius;

                if (v == 0) {
                    buf.vertex(xp, yp, 0).color(r, g, b, a).endVertex();
                }
                buf.vertex(xp, yp, 0).color(r, g, b, a).endVertex();
            }
        }
        tess.end();

        for (int seg = 0; seg < segments; seg++) {
            Metal mt = Metal.getMetal(toMetalIndex(seg));
            boolean mouseInSector = /*data.hasPower(mt) && */(degPer * seg < angle && angle < degPer * (seg + 1));
            float radius = maxRadius;//Math.max(0F, Math.min((this.timeIn + partialTicks - seg * 6F / segments) * 40F, maxRadius));
            if (mouseInSector) {
                radius *= 1.025f;
            }


            float rad = (seg + 0.5f) * degPer;
            float xp = x + Mth.cos(rad) * radius;
            float yp = y + Mth.sin(rad) * radius;

            float xsp = xp - 4;
            float ysp = yp;
            String name = (mouseInSector ? ChatFormatting.UNDERLINE : ChatFormatting.RESET) + Component.translatable(METAL_LOCAL[toMetalIndex(seg)]).getString();
            int textwidth = this.mc.font.width(name);

            if (xsp < x) {
                xsp -= textwidth - 8;
            }
            if (ysp < y) {
                ysp -= 9;
            }

            this.mc.font.drawShadow(matrixStack, name, xsp, ysp, 0xFFFFFF);

            double mod = 0.8;
            int xdp = (int) ((xp - x) * mod + x);
            int ydp = (int) ((yp - y) * mod + y);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, METAL_ICONS[toMetalIndex(seg)]);
            blit(matrixStack, xdp - 8, ydp - 8, 0, 0, 16, 16, 16, 16);

        }

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        RenderSystem.disableBlend();
    }

    private static double mouseAngle(int x, int y, int mx, int my) {
        return (Mth.atan2(my - y, mx - x) + Math.PI * 2) % (Math.PI * 2);
    }

    private static int toMetalIndex(int segment) {
        return (segment + 5) % Metal.values().length;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        toggleSelected(mouseButton);
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }


    private void toggleSelected(int mouseButton) {
        if (this.slotSelected != -1) {
            Metal mt = Metal.getMetal(toMetalIndex(this.slotSelected));
            System.out.println(mt.getName());
            this.mc.player.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
                changeTapping(mt, data, mouseButton);
                this.mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.1F, 2.0F);
            });
        }
    }


    public static void changeTapping(Metal metal, IFeruchemyData capability, int mouseButton) {
        int modificator = mouseButton == 0 ? 1 : -1;
        if (!capability.hasPower(metal) || (capability.tappingLevel(metal) + modificator > 3 && modificator == 1) || (capability.tappingLevel(metal) + modificator < metal.getMinTap() && modificator == -1)) {
            return;
        }
        ModMessages.sendToServer(new UpdateTappingPacket(metal, capability.tappingLevel(metal), modificator));

       /* if (capability.getAmount(metal) > 0) {
            capability.setBurning(metal, !capability.isBurning(metal));
        }*/
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
