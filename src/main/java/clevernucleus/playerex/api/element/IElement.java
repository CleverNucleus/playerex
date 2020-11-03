package clevernucleus.playerex.api.element;

import java.text.DecimalFormat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Element interface.
 */
public interface IElement {
	
	/**
	 * This is used to tell the game which elements are only for storage.
	 */
	enum Type {
		ALL, DATA, GAME;
	}
	
	/**
	 * @return The type of element this is.
	 */
	default Type type() {
		return Type.ALL;
	}
	
	
	/**
	 * Sets this element's registry name.
	 * @param par0 Registry name.
	 * @return The now registered element.
	 */
	IElement setRegistryName(ResourceLocation par0);
	
	/**
	 * @return This element's registry name.
	 */
	ResourceLocation getRegistryName();
	
	/**
	 * @param par0 Player instance.
	 * @param par1 PlayerElements capability.
	 * @return The value associated with this element.
	 */
	double get(PlayerEntity par0, IPlayerElements par1);
	
	/**
	 * Setter. Sets this element's value.
	 * @param par0 Player input.
	 * @param par1 Player Elements capability input.
	 * @param par2 Value input.
	 */
	void set(PlayerEntity par0, IPlayerElements par1, double par2);
	
	/**
	 * Adder. Adds the input to this element's value.
	 * @param par0 Player input.
	 * @param par1 Player Elements capability input.
	 * @param par2 Value input (note: if negative, the result is 'originalValue - inputValue').
	 */
	void add(PlayerEntity par0, IPlayerElements par1, double par2);
	
	/**
	 * @return This element's default written value.
	 */
	default double getDefaultValue() {
		return 0.0D;
	}
	
	/**
	 * @return This element's minimum value.
	 */
	default double getMinValue() {
		return 0.0D;
	}
	
	/**
	 * @return This element's maximum value.
	 */
	default double getMaxValue() {
		return 0.0D;
	}
	
	/**
	 * @return The display text for this element when provided a value.
	 */
	default ITextComponent getDisplay(double par0) {
		return new TranslationTextComponent(getRegistryName() + ".display", (new DecimalFormat("##.##")).format(par0));
	}
}
