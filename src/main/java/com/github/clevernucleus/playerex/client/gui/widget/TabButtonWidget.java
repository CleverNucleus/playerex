package com.github.clevernucleus.playerex.client.gui.widget;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.Page;
import com.github.clevernucleus.playerex.client.gui.IHandledScreen;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class TabButtonWidget extends ButtonWidget {
	private static final Identifier TABS = new Identifier(ExAPI.MODID, "textures/gui/tab.png");
	private HandledScreen<?> parent;
	private Page page;
	private int index, dx, dy;
	
	public TabButtonWidget(HandledScreen<?> parent, Page page, int index, int x, int y, PressAction onPress, TooltipSupplier tooltipSupplier) {
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
	
	public Page page() {
		return this.page;
	}
	
	public int index() {
		return this.index;
	}
	
	@Override
	public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
		if(this.isHovered()) {
			this.tooltipSupplier.onTooltip(this, matrices, mouseX, mouseY);
		}
	}
	
	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		IHandledScreen handledScreen = (IHandledScreen)this.parent;
		this.x = handledScreen.getX() + this.dx;
		this.y = handledScreen.getY() + this.dy;
		
		RenderSystem.setShaderTexture(0, TABS);
		RenderSystem.disableDepthTest();
		
		int u = (this.index % 6) * this.width;
		int v = this.isTopRow() ? 0 : (2 * this.height);
		int w = this.isTopRow() ? 9 : 7;
		
		if(!this.active) {
			v += this.height;
		}
		
		this.drawTexture(matrices, this.x, this.y, u, v, this.width, this.height);
		
		RenderSystem.enableDepthTest();
		
		ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
		itemRenderer.renderInGui(this.page.tabIcon(), this.x + 6, this.y + w);
	}
}
