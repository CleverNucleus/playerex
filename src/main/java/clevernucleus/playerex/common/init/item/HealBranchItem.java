package clevernucleus.playerex.common.init.item;

import java.util.List;

import clevernucleus.playerex.api.Util;
import clevernucleus.playerex.common.init.Registry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Healbranch item object.
 */
public class HealBranchItem extends Item implements ILoot {
	private final float weight;
	
	public HealBranchItem(final float par0) {
		super(new Properties().group(Group.INSTANCE).maxStackSize(1).maxDamage(25));
		this.weight = par0;
	}
	
	@Override
	public float getWeight() {
		return this.weight;
	}
	
	@Override
	public Rarity getRarity(ItemStack par0) {
		return Registry.IMMORTAL;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(final World par0, final PlayerEntity par1, final Hand par2) {
		ItemStack var0 = par1.getHeldItem(par2);
		RayTraceResult var1 = Util.lookPos(par0, par1, 100D);
		
		if(var1 != null && var1.getType() == RayTraceResult.Type.BLOCK || par1.rotationPitch >= -5) {
			int var2 = var1.getType().ordinal();
			
			if(var2 != -1) {
				par1.getCooldownTracker().setCooldown(this, 60);
				
				double var3 = var1.getHitVec().x - (var2 == 4 ? 0.5 : 0) + (var2 == 5 ? 0.5 : 0);
				double var4 = var1.getHitVec().y - (var2 == 0 ? 2.0 : 0) + (var2 == 1 ? 0.5 : 0);
				double var5 = var1.getHitVec().z - (var2 == 2 ? 0.5 : 0) + (var2 == 3 ? 0.5 : 0);
				
				if(par0 instanceof ServerWorld) {
					ServerWorld var6 = (ServerWorld)par0;
					List<LivingEntity> var7 = var6.getEntitiesWithinAABB(LivingEntity.class, Util.effectBounds(var3, var4, var5, 5D));
					
					for(LivingEntity var : var7) {
						var.heal(10F);
						
						for(int var12 = 0; var12 < 10; var12++) {
							var6.spawnParticle(ParticleTypes.HEART, var.getPosX() + Util.randomVe.apply(0.5D), var.getPosY() + var.getEyeHeight() + Util.randomVe.apply(0.5D) - 0.25D, var.getPosZ() + Util.randomVe.apply(0.5D), 1, (random.nextDouble() - 0.5D) * 2.0D, -random.nextDouble(), (random.nextDouble() - 0.5D) * 2.0D, 0.5D);
						}
					}
					
					par0.playSound((PlayerEntity)null, par1.getPosX(), par1.getPosY(), par1.getPosZ(), SoundEvents.BLOCK_BELL_USE, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
					var0.damageItem(1, par1, var -> {
						var.sendBreakAnimation(par2);
					});
				}
			}
		}
		
		return ActionResult.resultSuccess(var0);
	}
}
