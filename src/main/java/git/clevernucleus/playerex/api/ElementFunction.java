package git.clevernucleus.playerex.api;

import git.clevernucleus.playerex.api.element.IPlayerElements;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Holds a Player instance, IPlayerElements capability instance, an element's current value and adding value.
 */
public class ElementFunction {
	private PlayerEntity player;
	private IPlayerElements elements;
	private float currentValue, addingValue;
	
	private ElementFunction(PlayerEntity par0, IPlayerElements par1, float par2, float par3) {
		this.player = par0;
		this.elements = par1;
		this.currentValue = par2;
		this.addingValue = par3;
	}
	
	/**
	 * @param par0 Player instance.
	 * @param par1 IPlayerElements instance.
	 * @param par2 Current value.
	 * @param par3 Adding value.
	 * @return A new holding object.
	 */
	public static ElementFunction hold(PlayerEntity par0, IPlayerElements par1, float par2, float par3) {
		return new ElementFunction(par0, par1, par2, par3);
	}
	
	/**
	 * @return The player instance.
	 */
	public PlayerEntity player() {
		return this.player;
	}
	
	/**
	 * @return The IPlayerElements capability instance.
	 */
	public IPlayerElements elements() {
		return this.elements;
	}
	
	/**
	 * @return Current Element Value.
	 */
	public float currentValue() {
		return this.currentValue;
	}
	
	/**
	 * @return Adding Element Value.
	 */
	public float addingValue() {
		return this.addingValue;
	}
}
