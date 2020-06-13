package io.github.seanboyy.seans_necromancy.client.gui.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.seanboyy.seans_necromancy.Necromancy;
import io.github.seanboyy.seans_necromancy.container.inventory.ChargerContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ChargerScreen extends ContainerScreen<ChargerContainer> {
    private static final ResourceLocation CHARGER_GUI_TEXTURE = new ResourceLocation(Necromancy.MOD_ID, "textures/gui/charger.png");

    public ChargerScreen(ChargerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1, 1, 1, 1);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bindTexture(CHARGER_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
        int chargeTime = this.container.getChargeTime();
        if(chargeTime > 0) {
            int j1 = (int)(22F * (/*1F - */(float)chargeTime / (float)this.container.getChargeTimeMax()));
            this.blit(i + 77, j + 35, 177, 0, j1, 17);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), this.xSize / 2F - this.font.getStringWidth(this.title.getFormattedText()) / 2F, 6F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8F, this.ySize - 96 + 2, 4210752);
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
        this.renderHoveredToolTip(p_render_1_, p_render_2_);
    }
}
