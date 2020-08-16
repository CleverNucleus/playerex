package clevernucleus.playerex.common.init.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class HealthPotionItem extends Item {
	private final int level;
	
	public HealthPotionItem(final int par0) {
		super(new Properties().group(Group.INSTANCE).maxStackSize(1));
		
		this.level = par0;
	}
	
	@Override
	public void inventoryTick(ItemStack par0, World par1, Entity par2, int par3, boolean par4) {
		if(par1.isRemote) return;
		if(!(par2 instanceof PlayerEntity)) return;
		
		PlayerEntity var0 = (PlayerEntity)par2;
		
		var0.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 2F, 2F);
		var0.heal(this.level * 4F);
		par0.shrink(1);
	}
}
