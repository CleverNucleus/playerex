package clevernucleus.playerex.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import clevernucleus.playerex.common.init.element.IElement;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

/**
 * Util class for functions and utility methods.
 */
public class Util {
	
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
	 * @param par0 Input data.
	 * @return A tag with the input data.
	 */
	@SafeVarargs
	public static CompoundNBT fromElements(final BiValue<IElement, Double> ... par0) {
		CompoundNBT var0 = new CompoundNBT();
		ListNBT var1 = new ListNBT();
		
		for(BiValue<IElement, Double> var : par0) {
			CompoundNBT var2 = new CompoundNBT();
			String var3 = var.one().toString();
			double var4 = var.two().doubleValue();
			
			var2.putString("Name", var3);
			var2.putDouble("Value", var4);
			var1.add(var2);
		}
		
		var0.put("elements", var1);
		
		return var0;
	}
}
