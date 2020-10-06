package clevernucleus.playerex.api.element;

import clevernucleus.playerex.api.ElementRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;

/**
 * Main capability implementation.
 */
public class PlayerElements implements IPlayerElements {
	private CompoundNBT tag;
	
	public PlayerElements() {
		ListNBT var0 = new ListNBT();
		
		this.tag = new CompoundNBT();
		this.tag.put("Elements", var0);
		this.tag.putBoolean("Initialised", false);
		
		for(ResourceLocation var : ElementRegistry.getRegistry()) {
			IElement var1 = ElementRegistry.getElement(var);
			
			if(var1.type() == IElement.Type.ALL || var1.type() == IElement.Type.DATA) {
				putTag(var1, var1.getDefaultValue());
			}
		}
	}
	
	private boolean isEmpty() {
		return this.tag.getList("Elements", 10).isEmpty();
	}
	
	private void createTag(IElement par0, double par1) {
		CompoundNBT var1 = new CompoundNBT();
		
		var1.putString("Name", par0.toString());
		var1.putDouble("Value", par1);
		
		this.tag.getList("Elements", 10).add(var1);
	}
	
	private void putTag(IElement par0, double par1) {
		if(isEmpty()) {
			createTag(par0, par1);
			
			return;
		}
		
		for(INBT var : this.tag.getList("Elements", 10)) {
			CompoundNBT var0 = (CompoundNBT)var;
			String var1 = var0.getString("Name");
			
			if(var1.equals(par0.toString())) {
				var0.putDouble("Value", par1);
				
				return;
			}
		}
		
		createTag(par0, par1);
	}
	
	protected double get(IElement par0) {
		if(isEmpty()) return par0.getDefaultValue();
		
		for(INBT var : this.tag.getList("Elements", 10)) {
			CompoundNBT var0 = (CompoundNBT)var;
			String var1 = var0.getString("Name");
			
			if(var1.equals(par0.toString())) return var0.getDouble("Value");
		}
		
		return par0.getDefaultValue();
	}
	
	protected void set(IElement par0, double par1) {
		putTag(par0, par1);
	}
	
	protected void add(IElement par0, double par1) {
		putTag(par0, get(par0) + par1);
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
