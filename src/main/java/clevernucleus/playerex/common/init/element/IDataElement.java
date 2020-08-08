package clevernucleus.playerex.common.init.element;

import net.minecraft.nbt.CompoundNBT;

/**
 * Data interface for elements.
 */
public interface IDataElement extends IElement {
	
	/**
	 * @return The default value for this element.
	 */
	float defaultValue();
	
	/**
	 * Writes this element's default value to the input tag.
	 * @param par0 Input tag.
	 */
	void writeDefault(CompoundNBT par0);
}
