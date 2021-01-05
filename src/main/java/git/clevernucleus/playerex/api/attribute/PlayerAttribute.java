package git.clevernucleus.playerex.api.attribute;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.util.ResourceLocation;

public class PlayerAttribute implements IPlayerAttribute {
	private IPlayerAttribute.Type type;
	private Supplier<Attribute> attribute;
	private ResourceLocation registryName;
	private UUID uuid;
	
	public PlayerAttribute(final ResourceLocation par0, final UUID par1, final IPlayerAttribute.Type par2, final Supplier<Attribute> par3) {
		this.registryName = par0;
		this.uuid = par1;
		this.type = par2;
		this.attribute = par3;
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
