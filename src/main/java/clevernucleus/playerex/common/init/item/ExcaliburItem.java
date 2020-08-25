package clevernucleus.playerex.common.init.item;

import java.util.List;
import java.util.function.Function;

import clevernucleus.playerex.common.util.Util;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ExcaliburItem extends SwordItem implements ILoot {
	private final Function<Double, Double> positive = var -> var < 0D ? ((-1D) * var) : var;
	private final float weight;
	
	public ExcaliburItem(final float par0) {
		super(ItemTier.DIAMOND, 4, 4, new Properties().group(Group.INSTANCE));
		this.weight = par0;
	}
	
	/**
	 * @param par0 Origin x.
	 * @param par1 Origin y.
	 * @param par2 Origin z.
	 * @param par3 Target x.
	 * @param par4 Target y.
	 * @param par5 Target z.
	 * @return An array of interpolated BlockPos's between the origin and the target.
	 */
	private BlockPos[] positions(double par0, double par1, double par2, double par3, double par4, double par5) {
		double var0 = positive.apply(par3 - par0);
		double var1 = positive.apply(par5 - par2);
		double var2 = (par5 - par2) / (par3 - par0);
		double var3 = (par4 - par1) / (par3 - par0);
		double var4 = (par4 - par1) / (par5 - par2);
		int var5 = (int)Math.max(var0, var1);
		
		BlockPos[] var6 = new BlockPos[var5];
		
		if(var0 > var1) {
			Util.loop(var -> {
				double var7 = (par3 - par0) < 0 ? (par0 - var - 1) : (par0 + var + 1);
				double var8 = (var2 * (var7 - par3)) + par5;
				double var9 = (var3 * (var7 - par3)) + par4;
				
				var6[var] = new BlockPos(var7, var9, var8);
			}, var5);
		} else {
			Util.loop(var -> {
				double var7 = (par5 - par2) < 0 ? (par2 - var - 1) : (par2 + var + 1);
				double var8 = ((var7 - par5) / var2) + par3;
				double var9 = (var4 * (var7 - par5)) + par4;
				
				var6[var] = new BlockPos(var8, var9, var7);
			}, var5);
		}
		
		return var6;
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
		par2.add(new TranslationTextComponent("tooltip.excalibur", TextFormatting.GRAY));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World par0, PlayerEntity par1, Hand par2) {
		ItemStack var0 = par1.getHeldItem(par2);
		RayTraceResult var1 = Util.lookPos(par0, par1, 100D);
		
		if(var1 != null && var1.getType() == RayTraceResult.Type.BLOCK || par1.rotationPitch >= -5) {
			int var2 = var1.getType().ordinal();
			
			if(var2 != -1) {
				par1.getCooldownTracker().setCooldown(this, 1200);
				
				double var3 = var1.getHitVec().x - (var2 == 4 ? 0.5 : 0) + (var2 == 5 ? 0.5 : 0);
				double var4 = var1.getHitVec().y - (var2 == 0 ? 2.0 : 0) + (var2 == 1 ? 0.5 : 0);
				double var5 = var1.getHitVec().z - (var2 == 2 ? 0.5 : 0) + (var2 == 3 ? 0.5 : 0);
				double var6 = par1.getPosX();
				double var7 = par1.getPosY();
				double var8 = par1.getPosZ();
				
				if(par0 instanceof ServerWorld) {
					ServerWorld var9 = (ServerWorld)par0;
					
					for(BlockPos var : positions(var6, var7, var8, var3, var4, var5)) {
						AxisAlignedBB var10 = Util.effectBounds(var.getX(), var.getY(), var.getZ(), 5);
						List<LivingEntity> var11 = var9.getEntitiesWithinAABB(LivingEntity.class, var10);
						
						for(LivingEntity var12 : var11) {
							if(var12 != par1) {
								var12.setHealth(var12.getHealth() - 10F);
							}
						}
						
						for(int var12 = 0; var12 < 20; var12++) {
							var9.spawnParticle(ParticleTypes.LAVA, var.getX() + Util.RANDOM.apply(0.5D), var.getY() + Util.RANDOM.apply(0.5D) - 0.25D, var.getZ() + Util.RANDOM.apply(0.5D), 1, (random.nextDouble() - 0.5D) * 2.0D, -random.nextDouble(), (random.nextDouble() - 0.5D) * 2.0D, 0.5D);
						}
						
						var9.createExplosion(par1, DamageSource.causeExplosionDamage(par1), var.getX(), var.getY(), var.getZ(), 1.0F, false, Explosion.Mode.DESTROY);
					}
				}
				
				var0.damageItem(1, par1, var -> {
					var.sendBreakAnimation(par2);
				});
			}
		}
		
		return ActionResult.resultSuccess(var0);
	}
}
