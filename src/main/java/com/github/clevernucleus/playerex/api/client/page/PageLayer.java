package com.github.clevernucleus.playerex.api.client.page;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

/**
 * 
 * An abstract class that allows screen-type rendering to be done on the attributes screen.
 * 
 * @author CleverNucleus
 *
 */
@Environment(EnvType.CLIENT)
public abstract class PageLayer extends HandledScreen<ScreenHandler> {
	protected final HandledScreen<?> parent;
	
	/**
	 * 
	 * @param parent
	 * @param handler
	 * @param inventory
	 * @param title
	 */
	public PageLayer(HandledScreen<?> parent, ScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		
		this.parent = parent;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {}
	
	@Override
	public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {}
	
	@FunctionalInterface
	public interface Builder {
		
		/**
		 * 
		 * @param parent
		 * @param handler
		 * @param inv
		 * @param text
		 * @return
		 */
		PageLayer build(HandledScreen<?> parent, ScreenHandler handler, PlayerInventory inv, Text text);
	}
}
