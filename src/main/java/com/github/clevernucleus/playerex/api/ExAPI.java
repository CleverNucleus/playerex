package com.github.clevernucleus.playerex.api;

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
	/** The Cardinal Components Key for PlayerEx modifier data. */
	public static final ComponentKey<PlayerData> INSTANCE = ComponentRegistry.getOrCreate(new Identifier(MODID, "data"), PlayerData.class);
}
