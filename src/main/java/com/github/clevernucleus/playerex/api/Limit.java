package com.github.clevernucleus.playerex.api;

/**
 * Holds four values needed for attribute manipulation required by chance-based mods.
 */
public class Limit {
	private double increment, minValue, maxValue, weight;
	
	private Limit(final double par0, final double par1, final double par2, final double par3) {
		this.increment = (par0 < par2 ? par0 : 0D);
		this.minValue = (par1 < par2 ? par1 : 0D);
		this.maxValue = (par2 > par1 ? par2 : par0 + par1);
		this.weight = Math.min(Math.max(par3, 0D), 1D);
	}
	
	/**
	 * @param par0 Increment Value (must be less than the Max Value).
	 * @param par1 Min Value (must be less than the Max Value).
	 * @param par2 Max Value (must be greater than the Min Value).
	 * @param par3 Weight - for random chance; could be interpreted as how rare this attribute is (must be between 0 and 1).
	 * @return A new Limit instance holding the input parameters.
	 */
	public static Limit hold(final double par0, final double par1, final double par2, final double par3) {
		return new Limit(par0, par1, par2, par3);
	}
	
	/**
	 * @return A null-equivalent Limit instance holding [0D, 0D, 0D, 0D].
	 */
	public static Limit none() {
		return new Limit(0D, 0D, 0D, 0D);
	}
	
	public double increment() {
		return this.increment;
	}
	
	public double minValue() {
		return this.minValue;
	}
	
	public double maxValue() {
		return this.maxValue;
	}
	
	public double weight() {
		return this.weight;
	}
	
	@Override
	public boolean equals(Object par0) {
		if(par0 == null) return false;
		if(par0 == this) return true;
		if(!(par0 instanceof Limit)) return false;
		
		Limit var0 = (Limit)par0;
		
		return toString().equals(var0.toString());
	}
	
	@Override
	public String toString() {
		return "[incr=" + this.increment + ",min=" + this.minValue + ",max=" + this.maxValue + ",weight=" + this.weight + "]";
	}
}
