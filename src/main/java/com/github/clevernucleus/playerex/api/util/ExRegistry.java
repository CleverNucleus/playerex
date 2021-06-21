package com.github.clevernucleus.playerex.api.util;

import java.util.Collection;
import java.util.Map;

import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;

import net.minecraft.util.Identifier;

/**
 * The API's main data registry for hooks and external registries.
 * 
 * @author CleverNucleus
 *
 */
public interface ExRegistry {
	
	/**
	 * This should be used carefully and in not static contexts, as the contents of the registry is dynamic and lazily loaded!
	 * @param keyIn The attributes identifier key
	 * @return The PlayerAttribute instance if it exists; if it doesn't, returns null.
	 */
	IPlayerAttribute getAttribute(final Identifier keyIn);
	
	/**
	 * @return An immutable map of the attributes and their registry key's. Use this carefully and in non static contexts.
	 */
	Map<Identifier, IPlayerAttribute> attributes();
	
	/**
	 * This does NOT consider the attribute functions provided by datapack json; only those registered internally (with code) by mods.
	 * @param keyIn A registry key belonging to an attribute (we use registry keys here since the attribute itself may or may not exist yet).
	 * @return An immutable set of attribute functions belonging to the input attribute registry key.
	 */
	Collection<IAttributeFunction> functions(final Identifier keyIn);
	
	/**
	 * Registers an attribute function: adds an attribute function to the collection of attribute functions belonging to the attribute with the input registry key.
	 * @param keyIn
	 * @param functionIn
	 */
	void registerFunction(final Identifier keyIn, final IAttributeFunction functionIn);
	
	/**
	 * Registry provider interface.
	 * 
	 * @author CleverNucleus
	 *
	 */
	interface Provider {
		
		/**
		 * @return The registry object.
		 */
		ExRegistry get();
	}
}
