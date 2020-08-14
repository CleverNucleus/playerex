package clevernucleus.playerex.common.rarity;

import clevernucleus.playerex.common.util.RandDistribution;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * List of suffixes for relic names.
 */
public enum Suffixes {
	CRAFTED("crafted", 70),
	MADE("made", 70),
	KEPT("kept", 70),
	MOLDED("molded", 70),
	WHOLESOME("wholesome", 70),
	VALUABLE("valuable", 70),
	STALE("stale", 70),
	RUSTY("rusty", 70),
	DURABLE("durable", 50),
	MASTER("master", 50),
	VIGOROUS("vigorous", 50),
	HOT("hot", 50),
	LIT("lit", 30),
	SICKENING("sickening", 30),
	ANIME("anime", 10);
	
	private String name;
	private int weight;
	
	private Suffixes(final String par0, final int par1) {
		this.name = par0;
		this.weight = par1;
	}
	
	/**
	 * @return The random chance associated with this suffix.
	 */
	public int getWeight() {
		return this.weight;
	}
	
	/**
	 * @param par0 A name.
	 * @return The suffix with the input name; otherwise {@link Suffixes#CRAFTED}.
	 */
	public static Suffixes fromName(final String par0) {
		for(Suffixes var : Suffixes.values()) {
			if(par0.equals(var.toString())) return var;
		}
		
		return CRAFTED;
	}
	
	/**
	 * @return A random suffix with its weighting taken into account.
	 */
	public static Suffixes getRandomSuffix() {
		RandDistribution<Suffixes> var0 = new RandDistribution<Suffixes>(CRAFTED);
		
		for(Suffixes var : Suffixes.values()) {
			var0.add(var, (float)var.getWeight() / 100F);
		}
		
		return var0.getDistributedRandom();
	}
	
	/**
	 * @return The display text for this prefix.
	 */
	public ITextComponent getDisplayText() {
		return new TranslationTextComponent("tooltip." + this.name);
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
