package clevernucleus.playerex.common.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.init.element.IElement;
import clevernucleus.playerex.common.rarity.Prefixes;
import clevernucleus.playerex.common.rarity.Rareness;
import clevernucleus.playerex.common.rarity.Suffixes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Util class for functions and utility methods.
 */
public class Util {
	
	/** Formats floats to one decimal place. */
	public static final Function<String, DecimalFormat> FORMAT = par0 -> new DecimalFormat(par0);
	
	/**
	 * @param par0 List Consumer.
	 * @return An ArrayList of type <"E">.
	 */
	public static <E> List<E> list(final Consumer<List<E>> par0) {
		List<E> var0 = new ArrayList<E>();
		
		par0.accept(var0);
		
		return var0;
	}
	
	/**
	 * Adds par0 and par1 together with diminishing returns as they approach par2.
	 * @param par0 double Input 1.
	 * @param par1 double Input 2.
	 * @param par2 double Limit.
	 * @return double Diminishing returns output.
	 */
	public static double dim(final double par0, final double par1, final double par2) {
		if(par2 <= 0D) return 0D;
		if(par0 >= 0D && par1 >= 0D) return par2 * (1.0D - ((1.0D - (par0 / par2)) * (1.0D - (par1 / par2))));
		if(par0 >= 0D && par1 <= 0D) return par2 * (1.0D - ((1.0D - (par0 / par2)) / (1.0D - ((-par1) / par2))));
		if(par0 <= 0D && par1 >= 0D) return par2 * (1.0D - ((1.0D - (par1 / par2)) / (1.0D - ((-par0) / par2))));
		
		return 0D;
	}
	
	/**
	 * @param par0 float current player level
	 * @param par1 float current xp amount
	 * @return The dynamic xp coefficient to level up (0 - 1).
	 */
	public static float expCoeff(final float par0, final float par1) {
		int var0 = (int)par0 % 3;
		
		double var1 = -(((double)par1 + (double)var0) / (1.0D + (double)par0));
		
		return (float)(1.0D - Math.pow(Math.E, var1));
	}
	
	/**
	 * @return A random element from {@link Registry#GAME_ELEMENTS}; "<'Element Value, Chance'>".
	 */
	public static BiValue<IElement, BiValue<Float, Float>> randomElement() {
		List<IElement> var0 = Registry.GAME_ELEMENTS.stream().collect(Collectors.toList());
		RandDistribution<BiValue<Float, Float>> var1 = new RandDistribution<BiValue<Float, Float>>(BiValue.make(0F, 0F));
		IElement var2 = var0.get((new Random()).nextInt(var0.size()));
		
		for(float var = var2.minValue(); var < var2.maxValue(); var += var2.minValue()) {
			float var3 = 1F - (var / var2.maxValue());
			BiValue<Float, Float> var4 = BiValue.make(var, var3);
			
			var1.add(var4, var3);
		}
		
		BiValue<Float, Float> var5 = var1.getDistributedRandom();
		
		return BiValue.make(var2, var5);
	}
	
	/**
	 * @return Creates a tag containing random elements and rareness with weighting.
	 */
	public static CompoundNBT createRelicTag() {
		Map<IElement, Float> var0 = new HashMap<IElement, Float>();
		BiValue<IElement, BiValue<Float, Float>> var1 = randomElement();
		
		var0.put(var1.one(), var1.two().one());
		
		float var2 = var1.two().two();
		
		while((float)(new Random()).nextInt(100) / 100F < var2 && var0.size() < 6) {
			BiValue<IElement, BiValue<Float, Float>> var3 = randomElement();
			
			if(!var0.containsKey(var3.one())) {
				var0.put(var3.one(), var3.two().one());
				
				var2 *= var3.two().two();
			}
		}
		
		Rareness var3 = Rareness.range(var2);
		CompoundNBT var4 = new CompoundNBT();
		ListNBT var5 = new ListNBT();
		
		for(Map.Entry<IElement, Float> var : var0.entrySet()) {
			CompoundNBT var6 = new CompoundNBT();
			var6.putString("Name", var.getKey().toString());
			var6.putFloat("Value", var.getValue());
			var5.add(var6);
		}
		
		var4.put("Elements", var5);
		var3.write(var4);
		
		return var4;
	}
	
	/**
	 * Essentially the same as {@link Util#createRelicTag()}, except writes the name too.
	 * @param The input stack.
	 * @return The input ItemStack except with the relic tag.
	 */
	public static ItemStack createRandomRelic(ItemStack par0) {
		CompoundNBT var0 = Util.createRelicTag();
		Prefixes var1 = Prefixes.getRandomPrefix();
		Suffixes var2 = Suffixes.getRandomSuffix();
		
		par0.setTag(var0);
		par0.setDisplayName(new TranslationTextComponent("tooltip.relic_alt", var1.getDisplayText(), var2.getDisplayText()));
		
		return par0;
	}
	
	/**
	 * @param par0 Takes in the equivalent tag from {@link Util#createRelicTag()}.
	 * @return A map of the element instances and their values.
	 */
	public static Map<IElement, Float> attributeMap(final CompoundNBT par0) {
		Map<IElement, Float> var0 = new HashMap<IElement, Float>();
		
		if(!par0.contains("Elements")) return var0;
		
		ListNBT var1 = par0.getList("Elements", 10);
		
		for(int var = 0; var < var1.size(); var++) {
			CompoundNBT var2 = var1.getCompound(var);
			Registry.ELEMENT_FROM_ID.apply(var2.getString("Name")).ifPresent(var3 -> {
				var0.put(var3, var2.getFloat("Value"));
			});
		}
		
		return var0;
	}
}
