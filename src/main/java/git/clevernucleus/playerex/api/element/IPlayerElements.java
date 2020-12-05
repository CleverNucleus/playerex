package git.clevernucleus.playerex.api.element;

import net.minecraft.nbt.CompoundNBT;

public interface IPlayerElements {
	
	/**
	 * @param par0 Input element.
	 * @return Gets the input elements data from the input element's getFunction.
	 */
	float get(Element par0);
	
	/**
	 * Sets the input elements data in the player's tag to the input float.
	 * @param par0 Input element.
	 * @param par1 Input float.
	 */
	//void set(Element par0, float par1);
	
	/**
	 * Adds the input float to the input element's data in the player's tag if it exists.
	 * @param par0 Input element.
	 * @param par1 Input float.
	 */
	//void add(Element par0, float par1);
	
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
