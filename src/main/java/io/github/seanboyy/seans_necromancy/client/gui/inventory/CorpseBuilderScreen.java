package io.github.seanboyy.seans_necromancy.client.gui.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.container.inventory.CorpseBuilderContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class CorpseBuilderScreen extends ContainerScreen<CorpseBuilderContainer> {
    private static final ResourceLocation CORPSE_BUILDER_GUI_TEXTURES = new ResourceLocation(Necromancy.MOD_ID, "textures/gui/corpse_builder.png");

    public CorpseBuilderScreen(CorpseBuilderContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(final int mouseX, final int mouseY, final float f) {
        this.renderBackground();
        super.render(mouseX, mouseY, f);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), this.xSize / 2F - this.font.getStringWidth(this.title.getFormattedText()) / 2F, 6F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8F, this.ySize - 96 + 2, 4210752);
    }

    //TODO: verify this
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1, 1, 1, 1);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bindTexture(CORPSE_BUILDER_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
        int skeletonTime = this.container.getSkeletonTime();
        int zombieTime = this.container.getZombieTime();
        if(skeletonTime > 0) {
            int j1 = (int)(32F * (1F - skeletonTime / 600F));
            if(j1 > 0) this.blit(i + 80, j + 16, 176, 4, 16, j1);
        }
        if(zombieTime > 0) {
            this.blit(i + 80, j + 16, 176, 4, 16, 32);
            int j1 = (int)(32F * (1F - zombieTime / 600F));
            if(j1 > 0) this.blit(i + 80, j + 16, 176, 36, 16, j1);
        }
    }
}
