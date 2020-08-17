package clevernucleus.playerex.common.init.item;

import com.google.common.collect.Multimap;

import clevernucleus.playerex.common.util.Util;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * Subtle knife item object.
 */
public class SubtleKnifeItem extends Item {
	public SubtleKnifeItem() {
		super(new Properties().group(Group.INSTANCE).maxStackSize(1).maxDamage(250));
	}
	
	@Override
	public Rarity getRarity(ItemStack par0) {
		return Rarity.create("immortal", TextFormatting.GOLD);
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
					par0.addParticle(ParticleTypes.PORTAL, var3 + Util.RANDOM.apply(0.5D), var4 + Util.RANDOM.apply(0.25D), var5 + Util.RANDOM.apply(0.5D), (random.nextDouble() - 0.5D) * 2.0D, -random.nextDouble(), (random.nextDouble() - 0.5D) * 2.0D);
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
	
	@Override
	public boolean hitEntity(ItemStack par0, LivingEntity par1, LivingEntity par2) {
		par0.damageItem(1, par2, var -> {
			var.sendBreakAnimation(par2.getActiveHand());
		});
		
		return true;
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType par0, ItemStack par1) {
		Multimap<String, AttributeModifier> var0 = super.getAttributeModifiers(par0, par1);
		
		if(par0 == EquipmentSlotType.MAINHAND) {
			var0.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 1.0D, AttributeModifier.Operation.ADDITION));
			var0.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.0D, AttributeModifier.Operation.ADDITION));
		}
		
		return var0;
	}
}
