package clevernucleus.playerex.common.init.element;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.util.TriConsumer;

import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.init.capability.IPlayerElements;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

/**
 * Writable to the capability object implementation.
 */
public class WritableElement implements IElement<WritableElement> {
	private String key;
	private float defaultValue, minValue, maxValue;;
	private TriConsumer<PlayerEntity, IPlayerElements, Double> write;
	
	/**
	 * Constructor.
	 * @param par0 Key.
	 * @param par1 Default Value (only for initially writing to capability).
	 * @param par2 Minimum Value (only for random getter limit).
	 * @param par3 Maximum Value (only for random getter limit).
	 * @param par4 Adding function; additional stuff to do when using {@link IElement#add(PlayerEntity, IPlayerElements, double)}.
	 */
	public WritableElement(final @Nonnull String par0, final @Nonnull float par1, final @Nonnull float par2, final @Nonnull float par3, final @Nonnull TriConsumer<PlayerEntity, IPlayerElements, Double> par4) {
		this.key = par0;
		this.defaultValue = par1;
		this.minValue = par2;
		this.maxValue = par3;
		this.write = par4;
		
		Registry.WRITABLE_ELEMENTS.add(this);
	}
	
	/**
	 * Writes this element's default value to the input tag.
	 * @param par0 Input tag.
	 */
	public void writeDefaultValue(final CompoundNBT par0) {
		par0.putFloat(this.key, this.defaultValue);
	}
	
	@Override
	public WritableElement type() {
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
