package clevernucleus.playerex.common.init.item;

import java.util.List;

import clevernucleus.playerex.common.util.Util;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/**
 * Boom staff item object.
 */
public class BoomStaffItem extends Item {
	public BoomStaffItem() {
		super(new Properties().group(Group.INSTANCE).maxStackSize(1).maxDamage(25));
	}
	
	@Override
	public Rarity getRarity(ItemStack par0) {
		return Rarity.create("immortal", TextFormatting.GOLD);
	}
	
	@Override
	public void addInformation(ItemStack par0, World par1, List<ITextComponent> par2, ITooltipFlag par3) {
		par2.add(new TranslationTextComponent("tooltip.boom_staff", TextFormatting.GRAY));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(final World par0, final PlayerEntity par1, final Hand par2) {
		ItemStack var0 = par1.getHeldItem(par2);
		RayTraceResult var1 = Util.lookPos(par0, par1, 100D);
		
		if(var1 != null && var1.getType() == RayTraceResult.Type.BLOCK || par1.rotationPitch >= -5) {
			int var2 = var1.getType().ordinal();
			
			if(var2 != -1) {
				par1.getCooldownTracker().setCooldown(this, 1200);
				
				double var3 = var1.getHitVec().x - (var2 == 4 ? 0.5 : 0) + (var2 == 5 ? 0.5 : 0);
				double var4 = var1.getHitVec().y - (var2 == 0 ? 2.0 : 0) + (var2 == 1 ? 0.5 : 0);
				double var5 = var1.getHitVec().z - (var2 == 2 ? 0.5 : 0) + (var2 == 3 ? 0.5 : 0);
				
				if(!par0.isRemote) {
					Explosion.Mode var6 = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(par0, par1) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
					
					par0.createExplosion((PlayerEntity)null, DamageSource.causeExplosionDamage(par1), var3, var4, var5, 10.0F, true, var6);
					var0.damageItem(1, par1, var -> {
						var.sendBreakAnimation(par2);
					});
				}
			}
		}
		
		return ActionResult.resultSuccess(var0);
	}
}
