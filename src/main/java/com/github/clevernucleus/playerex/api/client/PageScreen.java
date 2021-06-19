package com.github.clevernucleus.playerex.api.client;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

/**
 * The abstract page class that should be extended when building a new attribute's page.
 * 
 * @author CleverNucleus
 *
 */
@Environment(EnvType.CLIENT)
public abstract class PageScreen extends HandledScreen<ScreenHandler> {
	private ItemStack icon;
	protected HandledScreen<?> parent;
	
	/**
	 * The constructor; this is lazily instantiated during the initialisation of the main attributes screen.
	 * @param parent The main attributes screen.
	 * @param handler The main attributes screen handler.
	 * @param inventory Player inventory
	 * @param title The text to disable at the head of the page.
	 * @param icon The page tab's icon.
	 */
	public PageScreen(HandledScreen<?> parent, ScreenHandler handler, PlayerInventory inventory, Text title, ItemStack icon) {
		super(handler, inventory, title);
		
		this.icon = icon;
		this.parent = parent;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {}
	
	
	@Override
	public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {}
	
	/**
	 * @return The normally protected button list for this page.
	 */
	public List<AbstractButtonWidget> buttons() {
		return this.buttons;
	}
	
	/**
	 * @return This page's tab icon.
	 */
	public ItemStack tabIcon() {
		return this.icon;
	}
}
