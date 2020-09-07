package clevernucleus.playerex.common.init.capability;

import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.init.element.IDataElement;
import clevernucleus.playerex.common.init.element.IElement;
import clevernucleus.playerex.common.network.SyncPlayerElements;
import clevernucleus.playerex.common.util.ConfigSetting;
import net.minecraft.entity.SharedMonsterAttributes;
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
		
		for(IDataElement var : Registry.DATA_ELEMENTS) {
			var.writeDefault(this.tag);
		}
	}
	
	/**
	 * Performs {@link IPlayerElements#add(PlayerEntity, IElement, double)} only if the value to add is greater than 0.
	 * @param par0 Player instance.
	 * @param par1 Element to add.
	 * @param par2 Value to add.
	 */
	private void addOnce(final PlayerEntity par0, final IElement par1, final int par2) {
		if(par2 <= 0) return;
		
		this.add(par0, par1, par2);
	}
	
	@Override
	public double get(final PlayerEntity par0, final IElement par1) {
		if(par1 instanceof IDataElement) return (double)this.tag.getFloat(par1.toString());
		
		return par1.get(par0, this);
	}
	
	@Override
	public void set(final PlayerEntity par0, final IElement par1, final double par2) {
		par1.set(par0, this, par2);
	}
	
	@Override
	public void add(final PlayerEntity par0, final IElement par1, final double par2) {
		par1.add(par0, this, par2);
	}
	
	@Override
	public void put(final IElement par0, final double par1) {
		this.tag.putDouble(par0.toString(), par1);
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
		
		this.addOnce(par0, Registry.CONSTITUTION, ConfigSetting.COMMON.constitution.get());
		this.addOnce(par0, Registry.STRENGTH, ConfigSetting.COMMON.strength.get());
		this.addOnce(par0, Registry.DEXTERITY, ConfigSetting.COMMON.dexterity.get());
		this.addOnce(par0, Registry.INTELLIGENCE, ConfigSetting.COMMON.intelligence.get());
		this.addOnce(par0, Registry.LUCK, ConfigSetting.COMMON.luck.get());
		this.add(par0, Registry.HEALTH, -20D);
		this.tag.putBoolean("Initialised", true);
	}
	
	@Override
	public void sync(final PlayerEntity par0) {
		if(par0.world.isRemote) return;
		
		CompoundNBT var0 = new CompoundNBT();
		
		var0.put("elements", this.tag);
		var0.putDouble("generic.knockbackResistance", par0.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getBaseValue());
		var0.putDouble("generic.attackDamage", par0.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue());
		
		Registry.NETWORK.sendTo(new SyncPlayerElements(var0), ((ServerPlayerEntity)par0).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
	}
}
