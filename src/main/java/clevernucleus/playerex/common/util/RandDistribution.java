package clevernucleus.playerex.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Specialised object that takes in a type and works out weighted distributions.
 * @param <T> Input type.
 */
public class RandDistribution<T> {
	private Map<T, Float> distribution;
	private T defualtValue;
	private float distSum;
	
	/**
	 * Constructor.
	 * @param par0 Default value that is returned in case something fails.
	 */
	public RandDistribution(final T par0) {
		this.distribution = new HashMap<>();
		this.defualtValue = par0;
	}
	
	/**
	 * Adds to the internal inventory of the distributor.
	 * @param par0 Input object type.
	 * @param par1 Input object type's weight.
	 */
	public void add(T par0, float par1) {
		if(this.distribution.get(par0) != null) {
			this.distSum -= this.distribution.get(par0);
		}
		
		this.distribution.put(par0, par1);
		this.distSum += par1;
	}
	
	/**
	 * @return A randomly distributed and weighted result.
	 */
	public T getDistributedRandom() {
		float var0 = (float)Math.random();
		float var1 = 1.0F / this.distSum;
		float var2 = 0.0F;
		
		for(T var : this.distribution.keySet()) {
			var2 += this.distribution.get(var);
			
			if(var0 / var1 <= var2) return var;
		}
		
		return this.defualtValue;
	}
}
