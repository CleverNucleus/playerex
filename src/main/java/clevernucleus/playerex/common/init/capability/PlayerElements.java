package clevernucleus.playerex.common.init.capability;

import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.init.element.Element;
import clevernucleus.playerex.common.init.element.IElement;
import clevernucleus.playerex.common.init.element.WritableElement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.NetworkDirection;

/**
 * Capability object; this is where elements should be accessed and changes should be made.
 */
public class PlayerElements implements IPlayerElements {
	private CompoundNBT tag;
	
	public PlayerElements() {
		this.tag = new CompoundNBT();
		this.tag.putBoolean("Initialised", false);
		
		for(IElement<WritableElement> var : Registry.WRITABLE_ELEMENTS) {
			var.type().writeDefaultValue(this.tag);
		}
	}
	
	@Override
	public double get(final PlayerEntity par0, final IElement<?> par1) {
		if(par1.type() instanceof WritableElement) return (double)this.tag.getFloat(par1.toString());
		
		return ((Element)par1.type()).get(par0, this);
	}
	
	@Override
	public void add(final PlayerEntity par0, final IElement<?> par1, final double par2) {
		if(par1.type() instanceof WritableElement) {
			this.tag.putFloat(par1.toString(), (float)(par2 + this.get(par0, par1)));
		}
		
		par1.add(par0, this, par2);
	}
	
	@Override
	public CompoundNBT write() {
		return this.tag;
	}
	
	@Override
	public void read(CompoundNBT par0) {
		this.tag = par0;
	}
	
	@Override
	public void init(final PlayerEntity par0) {
		if(par0.world.isRemote || this.tag.getBoolean("Initialised")) return;
		
		this.add(par0, Registry.HEALTH, -14D);
		this.tag.putBoolean("Initialised", true);
	}
	
	@Override
	public void sync(PlayerEntity par0) {
		if(par0.world.isRemote) return;
		
		Registry.NETWORK.sendTo(new SyncPlayerElements(this.tag), ((ServerPlayerEntity)par0).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
	}
}
