package com.github.clevernucleus.playerex.api.attribute;

import java.util.UUID;
import java.util.function.Supplier;

import com.github.clevernucleus.playerex.api.Limit;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.util.ResourceLocation;

public class PlayerAttribute implements IPlayerAttribute {
	private IPlayerAttribute.Type type;
	private Supplier<Attribute> attribute;
	private ResourceLocation registryName;
	private Limit limit;
	private UUID uuid;
	
	public PlayerAttribute(final ResourceLocation par0, final UUID par1, final Limit par2, final IPlayerAttribute.Type par3, final Supplier<Attribute> par4) {
		this.registryName = par0;
		this.uuid = par1;
		this.limit = par2;
		this.type = par3;
		this.attribute = par4;
	}
	
	@Override
	public Type type() {
		return this.type;
	}
	
	@Override
	public UUID uuid() {
		return this.uuid;
	}
	
	@Override
	public Limit limit() {
		return this.limit;
	}
	
	@Override
	public ResourceLocation registryName() {
		return this.registryName;
	}
	
	@Override
	public Attribute get() {
		return this.attribute.get();
	}
	
	@Override
	public boolean equals(Object par0) {
		if(par0 == null) return false;
		if(par0 == this) return true;
		if(!(par0 instanceof IPlayerAttribute)) return false;
		
		IPlayerAttribute var0 = (IPlayerAttribute)par0;
		
		return toString().equals(var0.toString());
	}
	
	@Override
	public String toString() {
		return this.registryName.toString();
	}
}
