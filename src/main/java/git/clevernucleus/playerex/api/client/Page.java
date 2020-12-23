package git.clevernucleus.playerex.api.client;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class Page extends Screen {
	public Page(ITextComponent par0) {
		super(par0);
	}
	
	public List<Widget> getButtonList() {
		return this.buttons;
	}
	
	public ItemStack displayStack() {
		return ItemStack.EMPTY;
	}
	
	public void drawGuiContainerForegroundLayer(MatrixStack par0, int par1, int par2) {}
	
	public void drawGuiContainerBackgroundLayer(MatrixStack par0, float par1, int par2, int par3) {}
	
	public void init(Minecraft par0, ContainerScreen<?> par1, int par2, int par3) {
		this.init(par0, par2, par3);
		this.init(par1);
	}
	
	protected void init(ContainerScreen<?> par1) {}
}
