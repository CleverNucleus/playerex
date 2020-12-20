package git.clevernucleus.playerex.api.element;

import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.Nonnull;

import git.clevernucleus.playerex.api.ElementFunction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class Element {
	enum Type {
		GAME, DATA, ALL;
	}
	
	private BiFunction<PlayerEntity, Float, Float> get;
	private Function<ElementFunction, Float> add;
	private float defaultValue, minValue, maxValue;
	private Type type;
	
	public Element(float par0, float par1, float par2, Type par3, @Nonnull BiFunction<PlayerEntity, Float, Float> par4, @Nonnull Function<ElementFunction, Float> par5) {
		this.defaultValue = par0;
		this.minValue = par1;
		this.maxValue = par2;
		this.type = par3;
		this.get = par4;
		this.add = par5;
	}
	
	protected BiFunction<PlayerEntity, Float, Float> getFunction() {
		return this.get;
	}
	
	protected Function<ElementFunction, Float> addFunction() {
		return this.add;
	}
	
	public Type type() {
		return this.type;
	}
	
	public float defaultValue() {
		return this.defaultValue;
	}
	
	public float minValue() {
		return this.minValue;
	}
	
	public float maxValue() {
		return this.maxValue;
	}
	
	public ResourceLocation registry() {
		if(Elements.ELEMENT_REGISTRY.containsValue(this)) return Elements.ELEMENT_REGISTRY.inverse().get(this);
		return new ResourceLocation("null");
	}
	
	@Override
	public boolean equals(Object par0) {
		if(!(par0 instanceof Element)) return false;
		
		Element var0 = (Element)par0;
		
		return toString().equals(var0.toString());
	}
	
	@Override
	public String toString() {
		return registry().toString();
	}
}
