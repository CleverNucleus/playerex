package clevernucleus.playerex.api;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;

import clevernucleus.playerex.api.element.IElement;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Helper class for useful statics.
 */
public class Util {
	
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
	 * @return A random element from {@link ElementRegistry#getRegistry}; Output Format: [Element[Value, Chance]].
	 */
	public static Pair<IElement, Pair<Double, Double>> randomElement() {
		List<IElement> var0 = ElementRegistry.getRegistry().stream().map(var -> ElementRegistry.getElement(var)).collect(Collectors.toList());
		RandDistribution<Pair<Double, Double>> var1 = new RandDistribution<Pair<Double, Double>>(Pair.of(0D, 0D));
		IElement var2 = var0.get((new Random()).nextInt(var0.size()));
		
		for(double var = var2.getMinValue(); var < var2.getMaxValue(); var += var2.getMinValue()) {
			float var3 = 1F - (float)(var / var2.getMaxValue());
			Pair<Double, Double> var4 = Pair.of(var, (double)var3);
			
			var1.add(var4, var3);
		}
		
		Pair<Double, Double> var3 = var1.getDistributedRandom();
		
		return Pair.of(var2, var3);
	}
	
	/**
	 * @return Creates a tag containing random elements and rareness with weighting.
	 */
	public static CompoundNBT createRelicTag() {
		Map<IElement, Double> var0 = Maps.newHashMap();
		Pair<IElement, Pair<Double, Double>> var1 = randomElement();
		
		var0.put(var1.getFirst(), var1.getSecond().getFirst());
		
		double var2 = var1.getSecond().getSecond();
		
		while((float)(new Random()).nextInt(100) / 100F < var2 && var0.size() < 6) {
			Pair<IElement, Pair<Double, Double>> var3 = randomElement();
			
			if(!var0.containsKey(var3.getFirst())) {
				var0.put(var3.getFirst(), var3.getSecond().getFirst());
				var2 *= var3.getSecond().getSecond();
			}
		}
		
		Rareness var3 = Rareness.range((float)var2);
		CompoundNBT var4 = new CompoundNBT();
		ListNBT var5 = new ListNBT();
		
		for(Map.Entry<IElement, Double> var : var0.entrySet()) {
			CompoundNBT var6 = new CompoundNBT();
			var6.putString("Name", var.getKey().toString());
			var6.putDouble("Value", var.getValue());
			var5.add(var6);
		}
		
		var4.put("Elements", var5);
		var3.write(var4);
		
		return var4;
	}
	
	/**
	 * @param par0 Takes in the equivalent tag from {@link Util#createRelicTag()}.
	 * @return A map of the element instances and their values.
	 */
	public static Map<IElement, Double> attributeMap(final CompoundNBT par0) {
		Map<IElement, Double> var0 = Maps.newHashMap();
		
		if(!par0.contains("Elements")) return var0;
		
		ListNBT var1 = par0.getList("Elements", 10);
		
		for(int var = 0; var < var1.size(); var++) {
			CompoundNBT var2 = var1.getCompound(var);
			IElement var3 = ElementRegistry.getElement(new ResourceLocation(var2.getString("Name")));
			
			var0.put(var3, var2.getDouble("Value"));
		}
		
		return var0;
	}
	
	/**
	 * Essentially the same as {@link Util#createRelicTag()}, except writes the name too.
	 * @param The input stack.
	 * @return The input ItemStack except with the relic tag.
	 */
	public static ItemStack createRandomRelic(ItemStack par0) {
		CompoundNBT var0 = Util.createRelicTag();
		
		par0.setTag(var0);
		par0.setDisplayName(new TranslationTextComponent(ElementRegistry.MODID + ".relic.name"));
		
		return par0;
	}
}
