package clevernucleus.playerex.common.init.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * Boom staff item object.
 */
public class MulagirItem extends Item implements ILoot {
	private final float weight;
	
	public MulagirItem(final float par0) {
		super(new Properties().group(Group.INSTANCE).maxStackSize(1).maxDamage(50));
		this.weight = par0;
	}
	
	@Override
	public float getWight() {
		return this.weight;
	}
	
	@Override
	public Rarity getRarity(ItemStack par0) {
		return Rarity.create("immortal", TextFormatting.GOLD);
	}
	
	@Override
	public void addInformation(ItemStack par0, World par1, List<ITextComponent> par2, ITooltipFlag par3) {
		par2.add(new TranslationTextComponent("tooltip.mulagir", TextFormatting.GRAY));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(final World par0, final PlayerEntity par1, final Hand par2) {
		ItemStack var0 = par1.getHeldItem(par2);
		
		par1.getCooldownTracker().setCooldown(this, 200);
		
		if(!par0.isRemote) {
			for(int var1 = -5; var1 < 5; var1++) {
				ArrowEntity var2 = new ArrowEntity(par0, par1);
				
				var2.shoot(par1, par1.rotationPitch, par1.rotationYaw + var1, 0.0F, 4.0F, 1F);
				var2.setIsCritical(true);
				var2.setDamage(var2.getDamage() + 10D);
				var2.setKnockbackStrength(2);
				var2.setShotFromCrossbow(true);
				var2.setFire(100);
				var2.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
				par0.addEntity(var2);
			}
			
			par0.playSound((PlayerEntity)null, par1.getPosX(), par1.getPosY(), par1.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
			var0.damageItem(1, par1, var -> {
				var.sendBreakAnimation(par2);
			});
		}
		
		return ActionResult.resultSuccess(var0);
	}
}
