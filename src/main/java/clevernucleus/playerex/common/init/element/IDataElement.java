package clevernucleus.playerex.common.init.element;

import clevernucleus.playerex.common.init.Registry;
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
	
	/**
	 * Adds this element to the registry set.
	 * @param par0 This element.
	 */
	default void init(IDataElement par0) {
		Registry.DATA_ELEMENTS.add(par0);
	}
}
