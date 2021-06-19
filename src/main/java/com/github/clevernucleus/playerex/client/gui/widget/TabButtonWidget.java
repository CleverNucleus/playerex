package com.github.clevernucleus.playerex.client.gui.widget;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.PageScreen;
import com.github.clevernucleus.playerex.client.gui.IHandledScreen;
import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TabButtonWidget extends ButtonWidget {
	private static final Identifier TABS = new Identifier(ExAPI.MODID, "textures/gui/tab.png");
	private HandledScreen<?> parent;
	private PageScreen page;
	private int dx, dy, index;
	
	public TabButtonWidget(HandledScreen<?> parent, PageScreen page, int index, int x, int y, PressAction onPress, TooltipSupplier tooltipSupplier) {
		super(x, y, 28, 32, LiteralText.EMPTY, onPress, tooltipSupplier);
		
		this.parent = parent;
		this.page = page;
		this.index = index;
		this.dx = x;
		this.dy = y;
	}
	
	private boolean isTopRow() {
		return this.index < 6;
	}
	
	public PageScreen page() {
		return this.page;
	}
	
	public int index() {
		return this.index;
	}
	
	@Override
	public void renderToolTip(MatrixStack matrices, int mouseX, int mouseY) {
		if(this.isHovered()) {
			this.tooltipSupplier.onTooltip(this, matrices, mouseX, mouseY);
		}
	}
	
	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		MinecraftClient client = MinecraftClient.getInstance();
		IHandledScreen handledScreen = (IHandledScreen)this.parent;
		this.x = handledScreen.getX() + this.dx;
		this.y = handledScreen.getY() + this.dy;
		
		client.getTextureManager().bindTexture(TABS);
		
		RenderSystem.disableDepthTest();
		
		int u = (this.index % 6) * this.width;
		int v = this.isTopRow() ? 0 : (2 * this.height);
		int w = this.isTopRow() ? 9 : 7;
		
		if(!this.active) {
			v += this.height;
		}
		
		this.drawTexture(matrices, this.x, this.y, u, v, this.width, this.height);
		
		RenderSystem.enableDepthTest();
		
		ItemRenderer renderer = client.getItemRenderer();
		renderer.renderInGui(this.page.tabIcon(), this.x + 6, this.y + w);
	}
}
