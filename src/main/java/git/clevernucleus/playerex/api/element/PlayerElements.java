package git.clevernucleus.playerex.api.element;

import git.clevernucleus.playerex.api.ElementFunction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

public class PlayerElements implements IPlayerElements {
	private CompoundNBT tag;
	private PlayerEntity player;
	
	public PlayerElements() {
		ListNBT var0 = new ListNBT();
		
		this.tag = new CompoundNBT();
		this.tag.put("Elements", var0);
		this.tag.putBoolean("Initialised", false);
		
		for(Element var : Elements.ELEMENT_REGISTRY.values()) {
			if(var.type() == Element.Type.ALL || var.type() == Element.Type.DATA) {
				putTag(var, var.defaultValue());
			}
		}
	}
	
	private boolean isEmpty() {
		return this.tag.getList("Elements", 10).isEmpty();
	}
	
	private void createTag(Element par0, float par1) {
		CompoundNBT var1 = new CompoundNBT();
		
		var1.putString("Name", par0.toString());
		var1.putFloat("Value", par1);
		
		this.tag.getList("Elements", 10).add(var1);
	}
	
	private void putTag(Element par0, float par1) {
		if(isEmpty()) {
			createTag(par0, par1);
			
			return;
		}
		
		for(INBT var : this.tag.getList("Elements", 10)) {
			CompoundNBT var0 = (CompoundNBT)var;
			String var1 = var0.getString("Name");
			
			if(var1.equals(par0.toString())) {
				var0.putFloat("Value", par1);
				
				return;
			}
		}
		
		createTag(par0, par1);
	}
	
	private float getTag(Element par0) {
		if(isEmpty()) return par0.defaultValue();
		
		for(INBT var : this.tag.getList("Elements", 10)) {
			CompoundNBT var0 = (CompoundNBT)var;
			String var1 = var0.getString("Name");
			
			if(var1.equals(par0.toString())) return var0.getFloat("Value");
		}
		
		return par0.defaultValue();
	}
	
	@Override
	public float get(Element par0) {
		return par0.getFunction().apply(this.player, getTag(par0)).floatValue();
	}
	
	@Override
	public void add(Element par0, float par1) {
		if(par0.type() == Element.Type.ALL || par0.type() == Element.Type.DATA) {
			putTag(par0, par0.addFunction().apply(ElementFunction.hold(this.player, this, get(par0), par1)));
		} else {
			par0.addFunction().apply(ElementFunction.hold(this.player, this, get(par0), par1));
		}
	}
	
	@Override
	public void set(Element par0, float par1) {
		if(par0.type() != Element.Type.ALL && par0.type() != Element.Type.DATA) return;
		
		putTag(par0, par1);
	}
	
	@Override
	public CompoundNBT write() {
		return this.tag;
	}
	
	@Override
	public void read(CompoundNBT par0) {
		this.tag = par0;
	}
}
