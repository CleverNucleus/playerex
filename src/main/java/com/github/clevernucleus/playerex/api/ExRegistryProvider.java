package com.github.clevernucleus.playerex.api;

import com.github.clevernucleus.playerex.api.util.ExRegistry;

/**
 * A provider implementation to hold the registry object safely.
 * 
 * @author CleverNucleus
 *
 */
public final class ExRegistryProvider implements ExRegistry.Provider {
	private ExRegistry registry;
	private boolean initialised;
	
	protected ExRegistryProvider() { this.initialised = false; }
	
	/**
	 * For internal use only; avoid using.
	 * @param registry
	 */
	@Deprecated
	public void init(final ExRegistry registry) {
		if(this.initialised) return;
		
		this.registry = registry;
		this.initialised = true;
	}
	
	@Override
	public ExRegistry get() {
		return this.registry;
	}
}
