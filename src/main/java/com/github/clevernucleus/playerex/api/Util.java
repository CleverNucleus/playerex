package com.github.clevernucleus.playerex.api;

import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttributes;

import net.minecraft.entity.ai.attributes.AttributeModifier;
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
	 * Applies or removes the input attribute modifier, but uses {@link #dim(double, double, double)} for ADDITION functions. Similar to {@link #add(IPlayerAttributes, PlayerEntity, IPlayerAttribute, double, double)}.
	 * @param par0 {@link IPlayerAttributes#applyModifier(PlayerEntity, IPlayerAttribute, AttributeModifier)} or {@link IPlayerAttributes#removeModifier(PlayerEntity, IPlayerAttribute, AttributeModifier)}.
	 * @param par1 Player instance.
	 * @param par2 IPlayerAttribute.
	 * @param par3 AttributeModifier.
	 * @param par4 AttributeModifier value multiplier.
	 * @param par5 Diminishing value limit (in case of modifier ADDITION function).
	 */
	public static void apply(TriFunction<PlayerEntity, IPlayerAttribute, AttributeModifier, IPlayerAttributes> par0, PlayerEntity par1, IPlayerAttribute par2, AttributeModifier par3, double par4, double par5) {
		double var0 = par3.getAmount() * par4;
		
		if(par3.getOperation() == AttributeModifier.Operation.ADDITION) {
			double var1 = par0.apply(null, null, null).get(par1, par2);
			var0 = dim(var1, par3.getAmount() * par4, par5) - var1;
		}
		
		AttributeModifier var1 = new AttributeModifier(par3.getID(), par3.getName(), var0, par3.getOperation());
		
		par0.apply(par1, par2, var1);
	}
	
	/**
	 * Applies or removes the input attribute modifier, but multiplies the modifier value with the input multiplier.
	 * @param par0 {@link IPlayerAttributes#applyModifier(PlayerEntity, IPlayerAttribute, AttributeModifier)} or {@link IPlayerAttributes#removeModifier(PlayerEntity, IPlayerAttribute, AttributeModifier)}.
	 * @param par1 Player instance.
	 * @param par2 IPlayerAttribute.
	 * @param par3 AttributeModifier.
	 * @param par4 AttributeModifier value multiplier.
	 */
	public static void apply(TriFunction<PlayerEntity, IPlayerAttribute, AttributeModifier, IPlayerAttributes> par0, PlayerEntity par1, IPlayerAttribute par2, AttributeModifier par3, double par4) {
		AttributeModifier var0 = new AttributeModifier(par3.getID(), par3.getName(), par3.getAmount() * par4, par3.getOperation());
		
		par0.apply(par1, par2, var0);
	}
	
	/**
	 * @param par0 float current player level
	 * @param par1 float current xp amount
	 * @return The dynamic xp coefficient to level up (0 - 1).
	 */
	public static double expCoeff(final double par0, final double par1) {
		
		/*
		int var0 = (int)par0 % 3;
		
		double var1 = -((par1 + var0) / (1.0D + par0));
		
		return 1.0D - Math.pow(Math.E, var1);
		*/
		
		return par1 / (3.0F + Math.pow(par0, 2.0D));
	}
}
