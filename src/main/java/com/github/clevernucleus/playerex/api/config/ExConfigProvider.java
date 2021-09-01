package com.github.clevernucleus.playerex.api.config;

/**
 * PlayerEx config provider class. Essentially a Supplier.
 * 
 * @author CleverNucleus
 *
 */
public final class ExConfigProvider implements IConfig.Provider {
	private IConfig config;
	private boolean built;
	
	public ExConfigProvider() { this.built = false; }
	
	/**
	 * For internal use only; redundant for modders.
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
