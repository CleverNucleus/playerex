package com.github.clevernucleus.playerex.api.attribute;

import java.util.Set;
import java.util.UUID;

import net.minecraft.util.Identifier;

/**
 * The Player Attribute interface. This is implemented as a gson superwrapper to EntityAttribute (and synced to the client).
 * 
 * @author CleverNucleus
 *
 */
public interface IPlayerAttribute {
	
	/**
	 * @return The attribute's type
	 */
	AttributeType type();
	
	/**
	 * @return This attributes UUID, assigned from the datapck json (MUST be unique, duh!)
	 */
	UUID uuid();
	
	/**
	 * @return This attributes registry key - this is the same registry key used in {@link net.minecraft.util.registryRegistry#ATTRIBUTES}.
	 */
	Identifier registryKey();
	
	/**
	 * @return A set of functions that this attribute holds.
	 */
	Set<IAttributeFunction> functions();
	
	/**
	 * @return The default value for the type of attribute used for storage (not gameplay). If {@link AttributeType#DATA} returns the default value; else returns 0.
	 */
	double valueFromType();
	
	/**
	 * @return The attribute's default value
	 */
	double defaultValue();
	
	/**
	 * @return The attribute's minimum value.
	 */
	double minValue();
	
	/**
	 * @return The attribute's maximum value.
	 */
	double maxValue();
	
	/**
	 * @return This attribute's translation key (that should link to it's display name).
	 */
	String translationKey();
	
	/**
	 * @param key
	 * @return Checks if the key is present for this attribute; returns true if it is.
	 */
	boolean hasProperty(final String keyIn);
	
	/**
	 * @param key
	 * @return The json defined property for the input key; if the key is not present, returns 0.
	 */
	float getProperty(final String keyIn);
}
