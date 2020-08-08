package clevernucleus.playerex.common.init.capability;

import clevernucleus.playerex.common.init.element.IElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

/**
 * PlayerElements capability access interface.
 */
public interface IPlayerElements {
	
	/**
	 * @param par0 Player instance.
	 * @param par1 IElement instance.
	 * @return the element value.
	 */
	double get(PlayerEntity par0, IElement par1);
	
	/**
	 * Sets the input value for this element to its storage.
	 * @param par0 Player instance.
	 * @param par1 IElement instance.
	 * @param par2 Value to set.
	 */
	void set(PlayerEntity par0, IElement par1, double par2);
	
	/**
	 * Adds the input value for this element to its storage.
	 * @param par0 Player instance.
	 * @param par1 IElement instance.
	 * @param par2 Value to add.
	 */
	void add(PlayerEntity par0, IElement par1, double par2);
	
	/**
	 * Puts the input element to storage with value from input.
	 * @param par0 Element instance.
	 * @param par1 Value.
	 */
	void put(IElement par0, double par1);
	
	/**
	 * Writes capability data to a tag.
	 * @return A tag with the capability data.
	 */
	CompoundNBT write();
	
	/**
	 * Reads the data from the input tag to the capability.
	 * @param par0 Input tag.
	 */
	void read(CompoundNBT par0);
	
	/**
	 * Test to see if the player has been initialised; if not, sets the players initial conditions; otherwise sets the initialised status 
	 * to true.
	 * @param par0 Player instance.
	 */
	void init(PlayerEntity par0);
	
	/**
	 * Syncs the input player's capability with the client.
	 * @param par0 PlayerEntity input.
	 */
	void sync(PlayerEntity par0);
}
