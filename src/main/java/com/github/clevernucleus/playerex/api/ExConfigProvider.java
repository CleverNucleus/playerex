package com.github.clevernucleus.playerex.api;

import com.github.clevernucleus.playerex.api.util.IConfig;

/**
 * Data holder to abstract over our configuration file.
 * 
 * @author CleverNucleus
 *
 */
public final class ExConfigProvider implements IConfig.Provider {
	private IConfig config;
	private boolean built;
	
	protected ExConfigProvider() { this.built = false; }
	
	/**
	 * For internal use only; avoid using.
	 * @param config
	 */
	@Deprecated
	public void build(IConfig config) {
		if(this.built) return;
		this.config = config;
		this.built = true;
	}
	
	@Override
	public IConfig get() {
		return this.config;
	}
}
