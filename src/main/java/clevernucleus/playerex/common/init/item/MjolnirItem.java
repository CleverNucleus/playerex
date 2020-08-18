package clevernucleus.playerex.common.init.item;

import java.util.List;

import com.google.common.collect.Multimap;

import clevernucleus.playerex.common.util.Util;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Mjolnir item object.
 */
public class MjolnirItem extends Item {
	public MjolnirItem() {
		super(new Properties().group(Group.INSTANCE).maxStackSize(1).maxDamage(300));
	}
	
	@Override
	public Rarity getRarity(ItemStack par0) {
		return Rarity.create("immortal", TextFormatting.GOLD);
	}
	
	@Override
	public void addInformation(ItemStack par0, World par1, List<ITextComponent> par2, ITooltipFlag par3) {
		par2.add(new TranslationTextComponent("tooltip.mjolnir", TextFormatting.GRAY));
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
						LightningBoltEntity var8 = new LightningBoltEntity(par0, var.getPosX(), var.getPosY(), var.getPosZ(), false);
						
						var8.setCaster((ServerPlayerEntity)par1);
						var6.addLightningBolt(var8);
					}
					
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
			var0.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 6.0D, AttributeModifier.Operation.ADDITION));
			var0.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.8D, AttributeModifier.Operation.ADDITION));
		}
		
		return var0;
	}
}
