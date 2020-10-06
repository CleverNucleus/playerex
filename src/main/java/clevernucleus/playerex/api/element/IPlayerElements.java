package clevernucleus.playerex.api.element;

import net.minecraft.nbt.CompoundNBT;

/**
 * The main capability for player elements.
 */
public interface IPlayerElements {
	
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
}
