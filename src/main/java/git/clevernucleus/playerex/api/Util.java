package git.clevernucleus.playerex.api;

import java.util.Random;
import java.util.function.Function;

/**
 * Helper class for useful statics.
 */
public class Util {
	/** Randomly returns the input as either negative or positive. */
	public static final Function<Double, Double> randomVe = var -> ((new Random()).nextBoolean() ? (-1D) : 1D) * var.doubleValue();
	
	/**
	 * Adds par0 and par1 together with diminishing returns as they approach par2.
	 * @param par0 double Input 1.
	 * @param par1 double Input 2.
	 * @param par2 double Limit.
	 * @return double Diminishing returns output.
	 */
	public static double add(final double par0, final double par1, final double par2) {
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
}
