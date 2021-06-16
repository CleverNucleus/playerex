package com.github.clevernucleus.playerex.api;

import com.github.clevernucleus.playerex.api.attribute.AttributeData;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.minecraft.util.Identifier;

public final class ExAPI {
	
	
	public static final String MODID = "playerex";
	
	
	public static final ExRegistry REGISTRY = new ExRegistry();
	
	
	public static final ComponentKey<AttributeData> DATA = ComponentRegistry.getOrCreate(new Identifier(MODID, "attributes"), AttributeData.class);
}
