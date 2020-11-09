package clevernucleus.playerex.common.init.item;

import clevernucleus.playerex.api.Util;
import clevernucleus.playerex.common.init.Registry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Subtle knife item object.
 */
public class SubtleKnifeItem extends SwordItem implements ILoot {
	private final float weight;
	
	public SubtleKnifeItem(final float par0) {
		super(ItemTier.IRON, 1, -2F, new Properties().group(Group.INSTANCE));
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
				
				for(int var = 0; var < 20; var++) {
					par0.addParticle(ParticleTypes.PORTAL, par1.getPosXRandom(0.5D), par1.getPosYRandom() - 0.25D, par1.getPosZRandom(0.5D), (random.nextDouble() - 0.5D) * 2.0D, -random.nextDouble(), (random.nextDouble() - 0.5D) * 2.0D);
					par0.addParticle(ParticleTypes.PORTAL, var3 + Util.randomVe.apply(0.5D), var4 + Util.randomVe.apply(0.25D), var5 + Util.randomVe.apply(0.5D), (random.nextDouble() - 0.5D) * 2.0D, -random.nextDouble(), (random.nextDouble() - 0.5D) * 2.0D);
				}
				
				if(!par0.isRemote) {
					par0.playSound((PlayerEntity)null, par1.getPosX(), par1.getPosY(), par1.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
					par1.stopRiding();
					par1.teleportKeepLoaded(var3, var4, var5);
					par0.playSound((PlayerEntity)null, par1.getPosX(), par1.getPosY(), par1.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
					var0.damageItem(1, par1, var -> {
						var.sendBreakAnimation(par2);
					});
				}
			}
		}
		
		return ActionResult.resultSuccess(var0);
	}
}
