package com.github.clevernucleus.playerex.client.network;

import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public final class ClientAttributeFunction implements IAttributeFunction {
	private String attribute;
	private Type type;
	private double multiplier;
	
	protected ClientAttributeFunction(String attribute, Type type, double multiplier) {
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
		if(!(object instanceof ClientAttributeFunction)) return false;
		
		ClientAttributeFunction function = (ClientAttributeFunction)object;
		return this.attribute.equals(function.attribute) && this.type.equals(function.type) && this.multiplier == function.multiplier;
	}
	
	@Override
	public String toString() {
		return "[attribute={" + this.attribute + "};type={" + this.type + "};multiplier={" + this.multiplier + "};]";
	}
}
