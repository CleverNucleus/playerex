package com.github.clevernucleus.playerex.api.attribute;

import java.util.UUID;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.util.ResourceLocation;

/**
 * This is the attribute interface that should be used to build new attributes.
 */
public interface IPlayerAttribute {
	
	/**
	 * These are used to define the behaviour of the attribute. See docs for detailed information.
	 */
	enum Type {
		GAME, DATA, ALL;
	}
	
	/**
	 * @return The attributes data behaviour; can also be used to differentiate between loot attributes and data attributes.
	 */
	Type type();
	
	/**
	 * @return The attributes UUID, used to define it.
	 */
	UUID uuid();
	
	/**
	 * @return Should be in the format of (YourMod.MODID, "name_of_attribute"). Example: {@link PlayerAttributes#CONSTITUTION} is (ExAPI.MODID, "constitution").
	 */
	ResourceLocation registryName();
	
	/**
	 * @return The attribute object based on {@link #type()}. This should be a get() method from a Supplier.
	 */
	Attribute get();
}
