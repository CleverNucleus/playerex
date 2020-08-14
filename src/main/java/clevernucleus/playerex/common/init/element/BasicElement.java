package clevernucleus.playerex.common.init.element;

import java.util.function.BiFunction;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.util.TriConsumer;

import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.init.capability.IPlayerElements;
import clevernucleus.playerex.common.util.BiValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

/**
 * Basic implementation of IElement.
 */
public class BasicElement implements IElement {
	private String key;
	private float minValue, maxValue;
	private TriConsumer<PlayerEntity, BiValue<IPlayerElements, IElement>, Double> adder;
	private BiFunction<PlayerEntity, IPlayerElements, Double> getter;
	private BiFunction<IElement, Float, ITextComponent> tooltip;
	
	/**
	 * Constructor.
	 * @param par0 Key.
	 * @param par1 Minimum Value.
	 * @param par2 Maximum Value.
	 * @param par3 Adder.
	 * @param par4 Getter.
	 */
	public BasicElement(final @Nonnull String par0, final @Nonnull float par1, final @Nonnull float par2, final @Nonnull TriConsumer<PlayerEntity, BiValue<IPlayerElements, IElement>, Double> par3, final @Nonnull BiFunction<PlayerEntity, IPlayerElements, Double> par4, final BiFunction<IElement, Float, ITextComponent> par5) {
		this.key = par0;
		this.minValue = par1;
		this.maxValue = par2;
		this.adder = par3;
		this.getter = par4;
		this.tooltip = par5;
		
		this.init(Registry.GAME_ELEMENTS, this);
	}
	
	@Override
	public float minValue() {
		return this.minValue;
	}
	
	@Override
	public float maxValue() {
		return this.maxValue;
	}
	
	@Override
	public double get(final PlayerEntity par0, final IPlayerElements par1) {
		return this.getter.apply(par0, par1);
	}
	
	@Override
	public void add(final PlayerEntity par0, final IPlayerElements par1, final double par2) {
		this.adder.accept(par0, BiValue.make(par1, this), par2);
	}
	
	@Override
	public ITextComponent getTooltip(final float par0) {
		return this.tooltip.apply(this, par0);
	}
	
	@Override
	public String toString() {
		return this.key;
	}
}
