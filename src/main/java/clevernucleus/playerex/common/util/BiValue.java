package clevernucleus.playerex.common.util;

/**
 * Object holder for two object types.
 * @param <O> One.
 * @param <T> Two.
 */
public class BiValue<O, T> {
	private O first;
	private T second;
	
	private BiValue(final O par0, final T par1) {
		this.first = par0;
		this.second = par1;
	}
	
	/**
	 * @param par0 First input.
	 * @param par1 Second input.
	 * @return A new BiValue of types input one and input two.
	 */
	public static <F, S> BiValue<F, S> make(final F par0, final S par1) {
		return new BiValue<F, S>(par0, par1);
	}
	
	/**
	 * @return Object one.
	 */
	public O one() {
		return this.first;
	}
	
	/**
	 * @return Object two.
	 */
	public T two() {
		return this.second;
	}
	
	@Override
	public boolean equals(Object par0) {
		if(!(par0 instanceof BiValue)) return false;
		
		BiValue<?, ?> var0 = (BiValue<?, ?>)par0;
		
		return this.first.equals(var0.one()) && this.second.equals(var0.two());
	}
	
	@Override
	public String toString() {
		return "[" + this.first.toString() + "," + this.second.toString() + "]";
	}
}
