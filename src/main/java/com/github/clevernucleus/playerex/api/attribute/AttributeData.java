package com.github.clevernucleus.playerex.api.attribute;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.attribute.EntityAttributeModifier;

/**
 * The PlayerEx API's data interface, provided by Cardinal Components. This should be used to access/modify player attributes.
 * 
 * @author CleverNucleus
 *
 */
public interface AttributeData extends Component {
	
	/**
	 * Semi-internal use, modder's don't need to use this.
	 * @param keyIn
	 * @return
	 */
	double getValue(final IPlayerAttribute keyIn);
	
	/**
	 * Resets all attributes to their defaults. requires a sync.
	 */
	void reset();
	
	/**
	 * Adds refund points (if allowed). Refund points are clamped above zero.
	 * @param valueIn
	 * @return The change in refund points.
	 */
	int addRefundPoints(final int pointsIn);
	
	/**
	 * @return The number of refund points this player has.
	 */
	int refundPoints();
	
	/**
	 * @param keyIn
	 * @return The input attribute's current value (including applied modifiers if of type {@link AttributeType#GAME}.
	 */
	double get(final IPlayerAttribute keyIn);
	
	/**
	 * Sets the input attribute's current value to the input value. Accounts for currently applied modifiers if of type {@link AttributeType#GAME}.
	 * @param keyIn
	 * @param valueIn
	 */
	void set(final IPlayerAttribute keyIn, final double valueIn);
	
	/**
	 * Adds/subtracts to the input attribute's current value. Accounts for currently applied modifiers if of type {@link AttributeType#GAME}.
	 * @param keyIn
	 * @param valueIn
	 */
	void add(final IPlayerAttribute keyIn, final double valueIn);
	
	/**
	 * Applies a persistent (permanent unless removed) modifier to the input attribute.
	 * @param keyIn
	 * @param modifier
	 */
	void applyAttributeModifier(final IPlayerAttribute keyIn, final EntityAttributeModifier modifierIn);
	
	/**
	 * Removes a persistent (permanent unless removed) modifier from the input attribute.
	 * @param keyIn
	 * @param modifier
	 */
	void removeAttributeModifier(final IPlayerAttribute keyIn, final EntityAttributeModifier modifierIn);
}
