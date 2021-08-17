package com.github.clevernucleus.playerex.api.config;


public final class ExConfigProvider implements IConfig.Provider {
	private IConfig config;
	private boolean built;
	
	
	public ExConfigProvider() { this.built = false; }
	
	
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
