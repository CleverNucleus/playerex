package clevernucleus.playerex.common.init.element;

import java.util.Set;

import clevernucleus.playerex.common.init.capability.IPlayerElements;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Accessing interface.
 * @param <T> Type.
 */
public interface IElement {
	
	default ITextComponent getDisplayName() {
		return new TranslationTextComponent("attribute." + this.toString().toLowerCase());
	}
	
	/**
	 * @return This element's minimum value.
	 */
	default float minValue() {
		return 0F;
	}
	
	/**
	 * @return This element's maximum value.
	 */
	default float maxValue() {
		return 0F;
	}
	
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
	
	/**
	 * Adds the input element to the input set.
	 * @param par0 Input set.
	 * @param par1 Input element.
	 */
	default <T extends IElement> void init(Set<T> par0, T par1) {
		par0.add(par1);
	}
}
