package clevernucleus.playerex.common.init.element;

import javax.annotation.Nonnull;

/**
 * Data holding object with a key-value system without using multiple type parameters.
 * @param <T> Data type.
 */
public class Element<T> {
	private String name;
	private T data;
	
	public Element(final @Nonnull String par0, T par1) {
		this.name = par0;
		this.data = par1;
	}
	
	@Override
	public String toString() {
		return this.name + ":" + this.data;
	}
}
