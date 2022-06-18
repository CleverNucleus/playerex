package com.github.clevernucleus.playerex.api;

import java.util.function.Supplier;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;

/**
 * Dedicated Supplier class to pass dynamic attribute references.
 * 
 * @author CleverNucleus
 *
 */
public final class EntityAttributeSupplier implements Supplier<EntityAttribute> {
	private final Identifier identifier;
	
	private EntityAttributeSupplier(final Identifier identifier) { this.identifier = identifier; }
	
	/**
	 * @param registryKey EntityAttribute registry key.
	 * @return
	 */
	public static EntityAttributeSupplier of(final Identifier registryKey) {
		return new EntityAttributeSupplier(registryKey);
	}
	
	/**
	 * @return The registry key.
	 */
	public Identifier getId() {
		return this.identifier;
	}
	
	@Override
	public EntityAttribute get() {
		return DataAttributesAPI.getAttribute(this.identifier).get();
	}
}
