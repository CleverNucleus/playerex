package clevernucleus.playerex.client.gui;

import java.util.function.BiConsumer;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.AbstractButton;

/**
 * Similar to ImageButton, but with some modifications specific to the needs of this object.
 */
public class TexturedButton extends AbstractButton {
	private ContainerScreen<?> parentScreen;
	private BiConsumer<ContainerScreen<?>, Integer> pressFunction;
	private int textureLat, textureLon, additionalData;
	
	/**
	 * Constructor.
	 * @param par0 ContainerScreen instance.
	 * @param par1 Button's x position.
	 * @param par2 Button's y position.
	 * @param par3 Button's width.
	 * @param par4 Button's height.
	 * @param par5 Button's texture start position (latitude).
	 * @param par6 Button's texture start position (longitude).
	 * @param par7 Called when the button is pressed.
	 */
	public TexturedButton(final ContainerScreen<?> par0, final int par1, final int par2, final int par3, final int par4, final int par5, final int par6, final int par7, final BiConsumer<ContainerScreen<?>, Integer> par8) {
		super(par0.getGuiLeft() + par1, par0.getGuiTop() + par2, par3, par4, "");
		
		this.parentScreen = par0;
		this.textureLat = par5;
		this.textureLon = par6;
		this.additionalData = par7;
		this.pressFunction = par8;
		
	}
	
	@Override
	public void onPress() {
		this.pressFunction.accept(this.parentScreen, this.additionalData);
	}
	
	@Override
	public void renderButton(int par0, int par1, float par2) {
		Minecraft var0 = Minecraft.getInstance();
		
		var0.getTextureManager().bindTexture(PlayerElementsScreen.GUI);
		
		RenderSystem.disableDepthTest();
		
		int var1 = this.textureLon;
		
		if(this.active) {
			if(this.isHovered()) {
				var1 += this.height;
			}
		} else {
			var1 += (2 * this.height);
		}
		
		this.blit(this.x, this.y, this.textureLat, var1, this.width, this.height);
		
		RenderSystem.enableDepthTest();
	}
}
