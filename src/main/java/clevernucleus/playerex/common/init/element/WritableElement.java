package clevernucleus.playerex.common.init.element;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.util.TriConsumer;

import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.init.capability.IPlayerElements;
import clevernucleus.playerex.common.util.BiValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

/**
 * Basic implementation of IElement.
 */
public class WritableElement implements IDataElement {
	private String key;
	private float defaultValue;
	private TriConsumer<PlayerEntity, BiValue<IPlayerElements, IElement>, Double> adder, setter;
	
	/**
	 * Constructor.
	 * @param par0 Key.
	 * @param par1 Default Value.
	 * @param par2 Adder.
	 * @param par3 Setter.
	 */
	public WritableElement(final @Nonnull String par0, final @Nonnull float par1, final @Nonnull TriConsumer<PlayerEntity, BiValue<IPlayerElements, IElement>, Double> par2, final @Nonnull TriConsumer<PlayerEntity, BiValue<IPlayerElements, IElement>, Double> par3) {
		this.key = par0;
		this.defaultValue = par1;
		this.adder = par2;
		this.setter = par3;
		
		this.init(Registry.DATA_ELEMENTS, this);
	}
	
	@Override
	public float defaultValue() {
		return this.defaultValue;
	}
	
	@Override
	public double get(final PlayerEntity par0, final IPlayerElements par1) {
		return this.defaultValue;
	}
	
	@Override
	public void set(final PlayerEntity par0, final IPlayerElements par1, final double par2) {
		this.setter.accept(par0, BiValue.make(par1, this), par2);
	}
	
	@Override
	public void add(final PlayerEntity par0, final IPlayerElements par1, final double par2) {
		this.adder.accept(par0, BiValue.make(par1, this), par2);
	}
	
	@Override
	public ITextComponent getTooltip(final float par0) {
		return new StringTextComponent(this.toString() + ":" + par0);
	}
	
	@Override
	public void writeDefault(final CompoundNBT par0) {
		par0.putDouble(this.key, this.defaultValue);
	}
	
	@Override
	public String toString() {
		return this.key;
	}
}
