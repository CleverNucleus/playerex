package clevernucleus.playerex.client.gui;

import java.util.function.BiConsumer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import clevernucleus.playerex.api.client.ClientReg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.text.StringTextComponent;

/**
 * Similar to ImageButton, but with some modifications specific to the needs of this object.
 */
public class PageButton extends AbstractButton {
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
	 * @param par7 Called when the button is pressed; additional data.
	 */
	public PageButton(final ContainerScreen<?> par0, final int par1, final int par2, int par3, int par4, final int par5, final int par6, final int par7, final BiConsumer<ContainerScreen<?>, Integer> par8) {
		super(par0.getGuiLeft() + par1, par0.getGuiTop() + par2, par3, par4, new StringTextComponent(""));
		
		this.parentScreen = par0;
		this.textureLat = par5;
		this.textureLon = par6;
		this.additionalData = par7;
		this.pressFunction = par8;
		this.active = (par7 != 0);
	}
	
	public boolean isHovered(int par0, int par1) {
		return (par0 >= this.x && par1 >= this.y && par0 < this.x + this.width && par1 < this.y + this.height);
	}
	
	public int getAdditionalData() {
		return this.additionalData;
	}
	
	@Override
	public void onPress() {
		this.pressFunction.accept(this.parentScreen, this.additionalData);
		this.active = false;
	}
	
	@Override
	public void renderButton(MatrixStack par0, int par1, int par2, float par3) {
		Minecraft var0 = Minecraft.getInstance();
		
		var0.getTextureManager().bindTexture(PlayerElementsScreen.TAB);
		
		RenderSystem.disableDepthTest();
		
		this.blit(par0, this.x, this.y, this.textureLat, this.active ? this.textureLon : (this.textureLon + 32), this.width, this.height);
		
		RenderSystem.enableDepthTest();
		
		ItemRenderer var1 = var0.getItemRenderer();
		
		var1.renderItemAndEffectIntoGUI(ClientReg.getPage(this.additionalData).displayStack(), this.x + 6, this.y + (this.additionalData < 6 ? 8 : 6));
	}
}
