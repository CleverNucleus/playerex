package clevernucleus.playerex.common.init.item;

import com.lazy.baubles.api.BaubleType;
import com.lazy.baubles.api.IBauble;

import clevernucleus.playerex.common.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Tiered Relic bauble.
 */
public class RelicItem extends Item implements IBauble {
	private BaubleType baubleType;
	
	public RelicItem(final BaubleType par0) {
		super(new Properties().group(Group.INSTANCE));
		
		this.baubleType = par0;
	}
	
	@Override
	public void inventoryTick(ItemStack par0, World par1, Entity par2, int par3, boolean par4) {
		if(par0.hasTag() && par0.getTag().contains("Elements") && par0.getTag().contains("Rareness")) return;
		
		Util.createRandomRelic(par0);
	}
	
	@Override
	public void onEquipped(final LivingEntity par0) {
		if(par0.world.isRemote) return;
		if(!(par0 instanceof PlayerEntity)) return;
		
		PlayerEntity var0 = (PlayerEntity)par0;
		
		
	}
	
	@Override
	public void onUnequipped(final LivingEntity par0) {}
	
	@Override
	public BaubleType getBaubleType() {
		return this.baubleType;
	}
}
