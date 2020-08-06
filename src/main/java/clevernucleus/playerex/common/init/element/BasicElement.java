package clevernucleus.playerex.common.init.element;

import java.util.function.BiFunction;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.util.TriConsumer;

import clevernucleus.playerex.common.init.capability.IPlayerElements;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Basic implementation of IElement.
 */
public class BasicElement implements IElement {
	private String key;
	private TriConsumer<PlayerEntity, IPlayerElements, Double> adder;
	private BiFunction<PlayerEntity, IPlayerElements, Double> getter;
	
	/**
	 * Constructor.
	 * @param par0 Key.
	 * @param par1 Adder.
	 * @param par2 Getter.
	 */
	public BasicElement(final @Nonnull String par0, final @Nonnull TriConsumer<PlayerEntity, IPlayerElements, Double> par1, final @Nonnull BiFunction<PlayerEntity, IPlayerElements, Double> par2) {
		this.key = par0;
		this.adder = par1;
		this.getter = par2;
	}
	
	@Override
	public double get(final PlayerEntity par0, final IPlayerElements par1) {
		return this.getter.apply(par0, par1);
	}
	
	@Override
	public void add(final PlayerEntity par0, final IPlayerElements par1, final double par2) {
		this.adder.accept(par0, par1, par2);
	}
	
	@Override
	public String toString() {
		return this.key;
	}
}
