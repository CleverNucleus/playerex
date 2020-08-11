package clevernucleus.playerex.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.util.text.ITextComponent;

public class TextDisplayPanel {
	private List<ITextComponent> list = new ArrayList<ITextComponent>();
	private ITextComponent displayString;
	private float posX = 0F, posY = 0F;
	
	public TextDisplayPanel(final ITextComponent par0, final float par1, final float par2, final Consumer<List<ITextComponent>> par3) {
		this.displayString = par0;
		this.posX = par1;
		this.posY = par2;
		
		par3.accept(this.list);
	}
	
	public float posX() {
		return this.posX;
	}
	
	public float posY() {
		return this.posY;
	}
	
	public List<ITextComponent> getHoverText() {
		return this.list;
	}
	
	public String getDisplayText() {
		return this.displayString == null ? "" : this.displayString.getFormattedText();
	}
	
	@Override
	public String toString() {
		return "[X:" + this.posX + ",Y:" + this.posY + "]";
	}
}
