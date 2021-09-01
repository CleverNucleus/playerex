package com.github.clevernucleus.playerex.api;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.attribute.EntityAttribute;

/**
 * Interface providing access to the PlayerEx cached modifier values, as well as refund points and the reset option.
 * 
 * @author CleverNucleus
 *
 */
public interface ModifierData extends Component {
	
	/**
	 * @return Returns the number of refund points the player has.
	 */
	int refundPoints();
	
	/**
	 * Adds or subtracts refund points (should only be called on the server; automatically syncs to client).
	 * @param pointsIn
	 * @return The change in refund points.
	 */
	int addRefundPoints(final int pointsIn);
	
	/**
	 * @param attributeIn
	 * @return The input attribute's cached PlayerEx modifier value (or 0 if the attribute is not present or has no modifier).
	 */
	double get(final EntityAttribute attributeIn);
	
	/**
	 * Sets the input attribute's modifier value (removes and the reapplies the modifier).
	 * Should only be called on the server; automatically syncs to the client.
	 * @param attributeIn
	 * @param valueIn
	 */
	void set(final EntityAttribute attributeIn, final double valueIn);
	
	/**
	 * Adds or subtracts to the input attribute's modifier value (removes and the reapplies the modifier).
	 * Should only be called on the server; automatically syncs to the client.
	 * @param attributeIn
	 * @param valueIn
	 */
	void add(final EntityAttribute attributeIn, final double valueIn);
	
	/**
	 * Resets all modifiers to their defaults (should only be called on the server; syncs automatically to the client).
	 */
	void reset();
}
