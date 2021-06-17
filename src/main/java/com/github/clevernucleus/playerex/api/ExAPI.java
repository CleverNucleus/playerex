package com.github.clevernucleus.playerex.api;

import com.github.clevernucleus.playerex.api.attribute.AttributeData;
import com.github.clevernucleus.playerex.api.util.ExRegistry;
import com.github.clevernucleus.playerex.api.util.IConfig;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.minecraft.util.Identifier;

/**
 * Main API access for the PlayerEx API package.
 * 
 * @author CleverNucleus
 *
 */
public final class ExAPI {
	
	/** The PlayerEx mod Id */
	public static final String MODID = "playerex";
	
	/** The API's config instance. Dev's can use this to access the config values implemented by the core mod. */
	public static final IConfig.Provider CONFIG = new ExConfigProvider();
	
	/** The API's registry instance. Use this to register/access attributes, additionals and functions.  */
	public static final ExRegistry.Provider REGISTRY = new ExRegistryProvider();
	
	/** The API's repository of all default player attributes. */
	public static final PlayerAttributesProvider ATTRIBUTES = new PlayerAttributesProvider();
	
	/** The API's data system key: provided by Cardinal Components. Use this to interact with attributes/modifiers preferentially to EntityAttributes. */
	public static final ComponentKey<AttributeData> DATA = ComponentRegistry.getOrCreate(new Identifier(MODID, "attributes"), AttributeData.class);
}
