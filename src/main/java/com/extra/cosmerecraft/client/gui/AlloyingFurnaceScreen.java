package com.extra.cosmerecraft.client.gui;

import com.extra.cosmerecraft.CosmereCraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AlloyingFurnaceScreen extends AbstractContainerScreen<AlloyingFurnaceMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(CosmereCraft.MODID,"textures/gui/alloying_furnace_gui.png");

    public AlloyingFurnaceScreen(AlloyingFurnaceMenu menu, Inventory inv, Component component) {
        super(menu, inv, component);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(pPoseStack, x, y);
        renderFuelFire(pPoseStack, x, y);
    }

    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        if(menu.isCrafting()) {
            blit(pPoseStack, x + 81, y + 36, 177, 14, menu.getScaledProgress(), 16);
        }
    }

    private void renderFuelFire(PoseStack pPoseStack, int x, int y){
        if(menu.isLit()) {
            blit(pPoseStack, x + 47, y + 46 + 14 - menu.getScaledFuelFire(), 176, 14 - menu.getScaledFuelFire(), 14, menu.getScaledFuelFire());
        }
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack p_97808_, int p_97809_, int p_97810_) {}
}
