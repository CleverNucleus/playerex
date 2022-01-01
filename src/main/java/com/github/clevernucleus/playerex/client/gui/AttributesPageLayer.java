package com.github.clevernucleus.playerex.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.ClientUtil;
import com.github.clevernucleus.playerex.api.client.PageLayer;
import com.github.clevernucleus.playerex.api.client.RenderComponent;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class AttributesPageLayer extends PageLayer {
	private static Supplier<Float> scaleX = () -> ExAPI.getConfig().textScaleX();
	private static Supplier<Float> scaleY = () -> ExAPI.getConfig().textScaleY();
	private static float scaleZ = 0.75F;
	
	private static final List<RenderComponent> COMPONENTS = new ArrayList<RenderComponent>();
	
	public AttributesPageLayer(HandledScreen<?> parent, ScreenHandler handler, PlayerInventory inventory, Text title) {
		super(parent, handler, inventory, title);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		matrices.push();
		matrices.scale(scaleX.get(), scaleY.get(), scaleZ);
		
		COMPONENTS.forEach(component -> component.renderText(this.client.player, matrices, this.textRenderer, this.x, this.y, scaleX.get(), scaleY.get()));
		
		matrices.pop();
		
		COMPONENTS.forEach(component -> component.renderTooltip(this.client.player, this::renderTooltip, matrices, this.textRenderer, this.x, this.y, mouseX, mouseY, scaleX.get(), scaleY.get()));
	}
	
	static {
		COMPONENTS.add(new RenderComponent(ExAPI.LEVEL, value -> {
			return new TranslatableText("playerex.gui.page.attributes.text.level", Math.round(value));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.level")).formatted(Formatting.GRAY));
			tooltip.add(LiteralText.EMPTY);
			
			ClientUtil.appendChildrenToTooltip(tooltip, ExAPI.LEVEL);
			return tooltip;
		}, 21, 26));
	}
}
