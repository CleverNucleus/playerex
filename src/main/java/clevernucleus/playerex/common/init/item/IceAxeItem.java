package clevernucleus.playerex.common.init.item;

import java.util.List;

import clevernucleus.playerex.api.Util;
import clevernucleus.playerex.common.init.Registry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Ice axe item object.
 */
public class IceAxeItem extends AxeItem implements ILoot {
	private final float weight;
	
	public IceAxeItem(final float par0) {
		super(ItemTier.IRON, 4, -2.8F, new Properties().group(Group.INSTANCE));
		this.weight = par0;
	}
	
	/** Creates the ice spike. */
	private void createIcicle(World par0, final double par1, final double par2, final double par3) {
		BlockPos var0 = new BlockPos(par1, par2, par3);
		BlockPos[] var1 = new BlockPos[] {
			var0.add(0, 5, 0),
			var0.add(-1, 4, 0),
			var0.add(1, 4, 0),
			var0.add(0, 4, -1),
			var0.add(0, 4, 1),
			var0.add(-1, 3, -1),
			var0.add(-1, 3, 1),
			var0.add(1, 3, -1),
			var0.add(1, 3, 1),
			var0.add(-2, 2, 0),
			var0.add(2, 2, 0),
			var0.add(0, 2, -2),
			var0.add(0, 2, 2),
			var0.add(-2, 1, -1),
			var0.add(-2, 1, 1),
			var0.add(2, 1, -1),
			var0.add(2, 1, 1),
			var0.add(-1, 1, -2),
			var0.add(1, 1, -2),
			var0.add(-1, 1, 2),
			var0.add(1, 1, 2)
		};
		
		for(BlockPos var : var1) {
			for(int var2 = 0; var2 < 6; var2++) {
				BlockPos var3 = var.add(0, -var2, 0);
				
				if(par0.getBlockState(var3).getBlock() == Blocks.AIR) {
					par0.setBlockState(var3, Registry.MAGIC_ICE.getDefaultState());
				}
			}
		}
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
				par1.getCooldownTracker().setCooldown(this, 100);
				
				double var3 = var1.getHitVec().x - (var2 == 4 ? 0.5 : 0) + (var2 == 5 ? 0.5 : 0);
				double var4 = var1.getHitVec().y - (var2 == 0 ? 2.0 : 0) + (var2 == 1 ? 0.5 : 0);
				double var5 = var1.getHitVec().z - (var2 == 2 ? 0.5 : 0) + (var2 == 3 ? 0.5 : 0);
				
				if(par0 instanceof ServerWorld) {
					ServerWorld var6 = (ServerWorld)par0;
					List<LivingEntity> var7 = var6.getEntitiesWithinAABB(LivingEntity.class, Util.effectBounds(var3, var4, var5, 10D));
					
					for(LivingEntity var : var7) {
						this.createIcicle(var6, var.getPosX(), var.getPosY(), var.getPosZ());
					}
					
					par0.playSound((PlayerEntity)null, par1.getPosX(), par1.getPosY(), par1.getPosZ(), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
					var0.damageItem(1, par1, var -> {
						var.sendBreakAnimation(par2);
					});
				}
			}
		}
		
		return ActionResult.resultSuccess(var0);
	}
}
