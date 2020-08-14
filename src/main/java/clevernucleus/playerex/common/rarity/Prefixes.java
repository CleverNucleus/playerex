package clevernucleus.playerex.common.rarity;

import clevernucleus.playerex.common.util.RandDistribution;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * List of prefixes for relic names.
 */
public enum Prefixes {
	BASIC("basic", 80),
	DULL("dull", 80),
	BORING("boring", 80),
	TRIVIAL("trivial", 80),
	SAD("sad", 80),
	SMALL("small", 80),
	LARGE("large", 70),
	MUNDANE("mundane", 70),
	LIFELESS("lifeless", 70),
	STUPID("stupid", 70),
	UNINTERESTING("uninteresting", 70),
	USELESS("useless", 70),
	STRONG("strong", 50),
	STURDY("sturdy", 50),
	POWERFUL("powerful", 50),
	FIRM("firm", 50),
	TOUGH("tough", 50),
	STABLE("stable", 50),
	INTENSE("intense", 50),
	ADEPT("adept", 50),
	EXCEPTIONAL("exceptional", 50),
	APT("apt", 30),
	SAAVY("saavy", 30),
	NIMBLE("nimble", 30),
	WISE("wise", 30),
	SHINY("shiny", 30),
	UNBELIEVABLE("unbelievable", 30),
	JAWDROPPING("jawdropping", 10),
	AMAZING("amazing", 10),
	GODLIKE("godlike", 10);
	
	private String name;
	private int weight;
	
	private Prefixes(final String par0, final int par1) {
		this.name = par0;
		this.weight = par1;
	}
	
	/**
	 * @return The random chance associated with this prefix.
	 */
	public int getWeight() {
		return this.weight;
	}
	
	/**
	 * @param par0 A name.
	 * @return The prefix with the input name; otherwise {@link Prefixes#BASIC}.
	 */
	public static Prefixes fromName(final String par0) {
		for(Prefixes var : Prefixes.values()) {
			if(par0.equals(var.toString())) return var;
		}
		
		return BASIC;
	}
	
	/**
	 * @return A random prefix with its weighting taken into account.
	 */
	public static Prefixes getRandomPrefix() {
		RandDistribution<Prefixes> var0 = new RandDistribution<Prefixes>(BASIC);
		
		for(Prefixes var : Prefixes.values()) {
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
