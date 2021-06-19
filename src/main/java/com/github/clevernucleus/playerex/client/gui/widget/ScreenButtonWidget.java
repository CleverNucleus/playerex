package com.github.clevernucleus.playerex.client.gui.widget;

import com.github.clevernucleus.playerex.client.gui.AttributesScreen;
import com.github.clevernucleus.playerex.client.gui.IHandledScreen;
import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ScreenButtonWidget extends ButtonWidget {
	private HandledScreen<?> parent;
	private Identifier attribute;
	private int u, v, dx, dy;
	public boolean alt;
	
	public ScreenButtonWidget(HandledScreen<?> parent, int x, int y, int u, int v, int width, int height, Identifier attribute, PressAction pressAction, TooltipSupplier tooltipSupplier) {
		super(x, y, width, height, LiteralText.EMPTY, pressAction, tooltipSupplier);
		
		this.parent = parent;
		this.attribute = attribute;
		this.u = u;
		this.v = v;
		this.dx = x;
		this.dy = y;
		this.alt = false;
	}
	
	public ScreenButtonWidget(HandledScreen<?> parent, int x, int y, int u, int v, int width, int height, Identifier attribute, PressAction pressAction) {
		this(parent, x, y, u, v, width, height, attribute, pressAction, EMPTY);
	}
	
	public ScreenButtonWidget(HandledScreen<?> parent, int x, int y, int u, int v, int width, int height, PressAction pressAction, TooltipSupplier tooltipSupplier) {
		this(parent, x, y, u, v, width, height, null, pressAction, tooltipSupplier);
	}
	
	public ScreenButtonWidget(HandledScreen<?> parent, int x, int y, int u, int v, int width, int height, PressAction pressAction) {
		this(parent, x, y, u, v, width, height, null, pressAction, EMPTY);
	}
	
	public Identifier attribute() {
		return this.attribute;
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
		
		client.getTextureManager().bindTexture(AttributesScreen.EX_GUI);
		
		RenderSystem.disableDepthTest();
		
		int i = this.u;
		int j = this.v;
		
		if(this.alt) {
			i += this.width;
		}
		
		if(this.active) {
			if(this.isHovered()) {
				j += this.height;
			}
		} else {
			j += (2 * this.height);
		}
		
		this.drawTexture(matrices, this.x, this.y, i, j, this.width, this.height);
		
		RenderSystem.enableDepthTest();
	}
}
