package com.github.clevernucleus.playerex.client.gui.widget;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.client.gui.ExScreenData;
import com.github.clevernucleus.playerex.client.gui.Page;
import com.github.clevernucleus.playerex.config.ConfigImpl;
import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TabButtonWidget extends ButtonWidget {
	private static final Identifier TABS = new Identifier(ExAPI.MODID, "textures/gui/tab.png");
	private static final Identifier TABS_DARK = new Identifier(ExAPI.MODID, "textures/gui/tab_dark.png");
	private HandledScreen<?> parent;
	private Page page;
	private int index, dx, dy;
	private final float scale = 1.0F / 16.0F;
	
	public TabButtonWidget(HandledScreen<?> parent, Page page, int index, int x, int y, boolean startingState, PressAction onPress) {
		super(x, y, 28, 32, LiteralText.EMPTY, onPress);
		
		this.parent = parent;
		this.page = page;
		this.index = index;
		this.dx = x;
		this.dy = y;
		this.active = startingState;
	}
	
	private boolean isTopRow() {
		return this.index < 6;
	}
	
	public int index() {
		return this.index;
	}
	
	@Override
	public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
		if(this.isHovered()) {
			this.parent.renderTooltip(matrices, this.page.title(), mouseX, mouseY);
		}
	}
	
	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		ExScreenData handledScreen = (ExScreenData)this.parent;
		this.x = handledScreen.getX() + this.dx;
		this.y = handledScreen.getY() + this.dy;
		
		RenderSystem.setShaderTexture(0, ((ConfigImpl)ExAPI.getConfig()).darkMode ? TABS_DARK : TABS);
		RenderSystem.disableDepthTest();
		
		int u = (this.index % 6) * this.width;
		int v = this.isTopRow() ? 0 : (2 * this.height);
		int w = this.isTopRow() ? 9 : 7;
		
		if(!this.active) {
			v += this.height;
		}
		
		this.drawTexture(matrices, this.x, this.y, u, v, this.width, this.height);
		
		RenderSystem.setShaderTexture(0, this.page.icon());
		matrices.push();
		matrices.scale(this.scale, this.scale, 0.75F);
		
		this.drawTexture(matrices, (int)((this.x + 6) / this.scale), (int)((this.y + w) / this.scale), 0, 0, 256, 256);
		
		matrices.pop();
		RenderSystem.enableDepthTest();
	}
}
