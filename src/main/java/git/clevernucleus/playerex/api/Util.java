package git.clevernucleus.playerex.api;

import git.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import git.clevernucleus.playerex.api.attribute.IPlayerAttributes;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Helper class for useful statics.
 */
public class Util {
	
	/**
	 * Adds par0 and par1 together with diminishing returns as they approach par2.
	 * @param par0 double Current Value (Input 1).
	 * @param par1 double Adding/Subtracting Value (Input 2).
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
	 * Adds the input IPlayerAttribute's current value with the input value, using {@link #dim(double, double, double)} and the input limit.
	 * @param par0 Capability instance.
	 * @param par1 Player instance.
	 * @param par2 IPlayerAttribute to add to.
	 * @param par3 Amount to add (can be negative to subtract).
	 * @param par4 Limit.
	 */
	public static void add(IPlayerAttributes par0, PlayerEntity par1, IPlayerAttribute par2, double par3, double par4) {
		double var0 = par0.get(par1, par2);
		double var1 = dim(var0, par3, par4) - var0;
		
		par0.add(par1, par2, var1);
	}
	
	/**
	 * @param par0 float current player level
	 * @param par1 float current xp amount
	 * @return The dynamic xp coefficient to level up (0 - 1).
	 */
	public static double expCoeff(final double par0, final double par1) {
		int var0 = (int)par0 % 3;
		
		double var1 = -((par1 + var0) / (1.0D + par0));
		
		return 1.0D - Math.pow(Math.E, var1);
	}
}
