package com.github.clevernucleus.playerex.api.util;

import java.util.Collection;
import java.util.Map;

import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;

import net.minecraft.util.Identifier;


public interface ExRegistry {
	
	
	IPlayerAttribute getAttribute(final Identifier keyIn);
	
	
	Map<Identifier, IPlayerAttribute> attributes();
	
	
	Collection<IAttributeFunction> functions(final Identifier keyIn);
	
	
	void registerFunction(final Identifier keyIn, final IAttributeFunction functionIn);
	
	
	interface Provider {
		
		
		ExRegistry get();
	}
}
