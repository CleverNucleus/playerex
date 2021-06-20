package com.github.clevernucleus.playerex.impl.attribute;

import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction;
import com.google.gson.annotations.Expose;

import net.minecraft.util.Identifier;

public final class AttributeFunction implements IAttributeFunction {
	@Expose private String attribute;
	@Expose private Type type;
	@Expose private double multiplier;
	
	protected AttributeFunction(String attribute, Type type, double multiplier) {
		this.attribute = attribute;
		this.type = type;
		this.multiplier = multiplier;
	}
	
	@Override
	public Identifier attributeKey() {
		return new Identifier(this.attribute);
	}
	
	@Override
	public Type type() {
		return this.type;
	}
	
	@Override
	public double multiplier() {
		return this.multiplier;
	}
	
	@Override
	public boolean equals(Object object) {
		if(this == object) return true;
		if(!(object instanceof AttributeFunction)) return false;
		
		AttributeFunction function = (AttributeFunction)object;
		return this.attribute.equals(function.attribute) && this.type.equals(function.type) && this.multiplier == function.multiplier;
	}
	
	@Override
	public String toString() {
		return "[attribute={" + this.attribute + "};type={" + this.type + "};multiplier={" + this.multiplier + "};]";
	}
}
