package clevernucleus.playerex.api.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class Page extends Screen {
	public Page(ITextComponent par0) {
		super(par0);
	}
	
	public ItemStack displayStack() {
		return ItemStack.EMPTY;
	}
	
	public void drawGuiContainerForegroundLayer(MatrixStack par0, int par1, int par2) {}
	
	public void drawGuiContainerBackgroundLayer(MatrixStack par0, float par1, int par2, int par3) {}
}
