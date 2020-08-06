package clevernucleus.playerex.common.init.element;

import clevernucleus.playerex.common.init.capability.IPlayerElements;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Accessing interface.
 * @param <T> Type.
 */
public interface IElement<T> {
	
	/**
	 * @return The specific type of element.
	 */
	T type();
	
	/**
	 * The internal add function.
	 * @param par0 Player instance.
	 * @param par1 Capability instance.
	 * @param par2 value to add.
	 */
	void add(PlayerEntity par0, IPlayerElements par1, double par2);
	
	/**
	 * @return A float array containing {Minimum Value, Maximum Value}.
	 */
	float[] values();
}
