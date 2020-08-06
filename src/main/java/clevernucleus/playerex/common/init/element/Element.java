package clevernucleus.playerex.common.init.element;

import java.util.function.BiFunction;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.util.TriConsumer;

import clevernucleus.playerex.common.init.capability.IPlayerElements;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Non-writable to the capability object implementation.
 */
public class Element implements IElement<Element> {
	private String key;
	private float minValue, maxValue;
	private TriConsumer<PlayerEntity, IPlayerElements, Double> write;
	private BiFunction<PlayerEntity, IPlayerElements, Double> read;
	
	/**
	 * Constructor.
	 * @param par0 Key.
	 * @param par1 Minimum Value (only for random getter limit).
	 * @param par2 Maximum Value (only for random getter limit).
	 * @param par3 Adding function; additional stuff to do when using {@link IElement#add(PlayerEntity, IPlayerElements, double)}.
	 * @param par4 Reading function; returns from whatever is in here instead of the capability.
	 */
	public Element(final @Nonnull String par0, final @Nonnull float par1, final @Nonnull float par2, final @Nonnull TriConsumer<PlayerEntity, IPlayerElements, Double> par3, final @Nonnull BiFunction<PlayerEntity, IPlayerElements, Double> par4) {
		this.key = par0;
		this.minValue = par1;
		this.maxValue = par2;
		this.write = par3;
		this.read = par4;
	}
	
	/**
	 * @param par0 Player instance.
	 * @param par1 Capability instance.
	 * @return This element's internal getter function.
	 */
	public double get(final PlayerEntity par0, final IPlayerElements par1) {
		return this.read.apply(par0, par1);
	}
	
	@Override
	public Element type() {
		return this;
	}
	
	//@Override
	public void add(final PlayerEntity par0, final IPlayerElements par1, final double par2) {
		this.write.accept(par0, par1, par2);
	}
	
	@Override
	public float[] values() {
		return new float[] {this.minValue, this.maxValue};
	}
	
	@Override
	public String toString() {
		return this.key;
	}
}
