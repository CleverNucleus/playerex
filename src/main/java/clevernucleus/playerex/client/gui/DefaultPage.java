package clevernucleus.playerex.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import clevernucleus.playerex.api.client.gui.Page;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;

public class DefaultPage extends Page {
	
	public DefaultPage(ITextComponent par0) {
		super(par0);
	}
	
	@Override
	public ItemStack displayStack() {
		return new ItemStack(Items.PLAYER_HEAD);
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(MatrixStack par0, int par1, int par2) {
		this.font.drawString(par0, this.title.getString(), 9F, 9F, 4210752);
	}
}
