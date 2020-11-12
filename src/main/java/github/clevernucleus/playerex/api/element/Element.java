package github.clevernucleus.playerex.api.element;

import github.clevernucleus.playerex.api.ElementRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

/**
 * Object implementation element.
 */
public class Element implements IElement {
	private ResourceLocation registryName;
	private Type type;
	
	private double defaultValue, minValue, maxValue;
	
	/**
	 * @param par0 Default value when first created.
	 * @param par1 Minimum value (doesn't affect value setting/adding).
	 * @param par2 Maximum value (doesn't affect value setting/adding).
	 * @param par3 Type (read the docs!).
	 */
	public Element(double par0, double par1, double par2, Type par3) {
		this.defaultValue = par0;
		this.minValue = par1;
		this.maxValue = par2;
		this.type = par3;
	}
	
	@Override
	public Type type() {
		return this.type;
	}
	
	@Override
	public double get(PlayerEntity par0, IPlayerElements par1) {
		return ((PlayerElements)par1).get(this);
	}
	
	@Override
	public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
		((PlayerElements)par1).set(this, par2);
	}
	
	@Override
	public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
		((PlayerElements)par1).add(this, par2);
	}
	
	@Override
	public IElement setRegistryName(ResourceLocation par0) {
		if(ElementRegistry.getRegistry().contains(par0)) return this;
		
		this.registryName = par0;
		
		return this;
	}
	
	@Override
	public ResourceLocation getRegistryName() {
		return this.registryName;
	}
	
	@Override
	public double getDefaultValue() {
		return this.defaultValue;
	}
	
	@Override
	public double getMinValue() {
		return this.minValue;
	}
	
	@Override
	public double getMaxValue() {
		return this.maxValue;
	}
	
	@Override
	public String toString() {
		return this.registryName.toString();
	}
}
