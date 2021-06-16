package com.github.clevernucleus.playerex.api.util;

/**
 * A helper class for maths.
 * 
 * @author CleverNucleus
 *
 */
public final class Maths {
	
	/**
	 * Adds or subtracts the current and adding values, with diminishing returns tending towards the limit.
	 * @param current Current value (add to or subtract from).
	 * @param adding Modifying value (+/-).
	 * @param limit The limiting value (output respects this, unless limit == 0).
	 * @return The diminishing returns.
	 */
	public static double add(final double current, final double adding, final double limit) {
		if(limit == 0.0D) return current;
		if(adding > 0.0D) return limit * ((current + adding) / (limit + adding));
		if(adding < 0.0D) return adding + current - (current * adding / limit);
		
		return current;
	}
}
