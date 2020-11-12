package github.clevernucleus.playerex.api;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * A custom rarity enumeration.
 */
public enum Rareness {
	COMMON("common", 70, 0.0F, TextFormatting.WHITE),
	UNCOMMON("uncommon", 50, 0.1F, TextFormatting.DARK_GRAY),
	RARE("rare", 30, 0.2F, TextFormatting.DARK_GREEN),
	EPIC("epic", 20, 0.3F, TextFormatting.DARK_AQUA),
	MYTHICAL("mythical", 10, 0.4F, TextFormatting.DARK_RED),
	LEGENDARY("legendary", 5, 0.5F, TextFormatting.DARK_PURPLE),
	IMMORTAL("immortal", 0, 0.6F, TextFormatting.GOLD);
	
	private TextFormatting colour;
	private String name;
	private int weight;
	private float property;
	
	Rareness(final String par0, final int par1, final float par2, final TextFormatting par3) {
		this.name = par0;
		this.weight = par1;
		this.property = par2;
		this.colour = par3;
	}
	
	/**
	 * @param par0 Input name.
	 * @return The Rareness object that matches the input; otherwise {@link Rareness#COMMON}.
	 */
	public static Rareness fromName(final String par0) {
		for(Rareness var : Rareness.values()) {
			if(par0.equals(var.toString())) return var;
		}
		
		return COMMON;
	}
	
	/**
	 * @param par0 Input tag.
	 * @return Reads the Rareness from the input tag.
	 */
	public static Rareness read(final CompoundNBT par0) {
		if(!par0.contains("Rareness")) return COMMON;
		
		return fromName(par0.getString("Rareness"));
	}
	
	/**
	 * @param par0 input percentage (0 - 1).
	 * @return Uses boundaries to get the correct rareness.
	 */
	public static Rareness range(final float par0) {
		Rareness[] var0 = Rareness.values();
		
		for(int var = 0; var < var0.length; var++) {
			int var1 = (var - 1) % var0.length;
			float var2 = (float)var0[var].getWeight() / 100F;
			float var3 = var1 < 0 ? 1.0F : (float)var0[var1].getWeight() / 100F;
			
			if(par0 > var2 && par0 <= var3) return var0[var];
		}
		
		return COMMON;
	}
	
	/**
	 * Writes this Rareness to the input tag.
	 * @param par0 Input tag.
	 */
	public void write(CompoundNBT par0) {
		par0.putString("Rareness", this.name);
	}
	
	/**
	 * @return The weighting.
	 */
	public int getWeight() {
		return this.weight;
	}
	
	/**
	 * @return The property needed for textures.
	 */
	public float getProperty() {
		return this.property;
	}
	
	/**
	 * @return The text colour.
	 */
	public TextFormatting getColour() {
		return this.colour;
	}
	
	/**
	 * @return The display text for this rareness.
	 */
	public ITextComponent getDisplayText() {
		return new StringTextComponent(this.colour + (new TranslationTextComponent(ElementRegistry.MODID + ".rarity." + this.name)).getString());
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
