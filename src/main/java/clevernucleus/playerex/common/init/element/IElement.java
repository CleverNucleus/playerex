package clevernucleus.playerex.common.init.element;

import clevernucleus.playerex.common.init.capability.IPlayerElements;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Accessing interface.
 * @param <T> Type.
 */
public interface IElement {
	
	/**
	 * The internal getter function.
	 * @param par0 Player instance.
	 * @param par1 Capability instance.
	 * @return This elements getting function.
	 */
	double get(PlayerEntity par0, IPlayerElements par1);
	
	/**
	 * The internal set function.
	 * @param par0 Player instance.
	 * @param par1 Capability instance.
	 * @param par2 value to set.
	 */
	default void set(PlayerEntity par0, IPlayerElements par1, double par2) {}
	
	/**
	 * The internal add function.
	 * @param par0 Player instance.
	 * @param par1 Capability instance.
	 * @param par2 value to add.
	 */
	void add(PlayerEntity par0, IPlayerElements par1, double par2);
}
