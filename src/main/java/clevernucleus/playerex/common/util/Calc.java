package clevernucleus.playerex.common.util;

/**
 * Custom maths helper class.
 */
public class Calc {
	
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
}
