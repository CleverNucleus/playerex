package com.github.clevernucleus.playerex.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	/*
	private static final UUID ATTACK_DAMAGE_MODIFIER_ID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
	private static final UUID ATTACK_SPEED_MODIFIER_ID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
	
	@ModifyVariable(method = "getTooltip", at = @At(value = "STORE", ordinal = 1), name = "d", ordinal = 0)
	private double attackDamageTooltip(double d, @Nullable PlayerEntity player, TooltipContext context) {
		ItemStack stack = (ItemStack)(Object)this;
		double cache = d;
		double modifier = 0;
		
		if(player != null) {
			AttributeData data = ExAPI.DATA.get(player);
			ItemStack held = player.getMainHandStack();
			cache -= player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
			modifier = cache;
			cache += data.get(PlayerAttributes.MELEE_ATTACK_DAMAGE.get());
			
			if(stack.equals(held)) {
				cache -= modifier;
			} else {
				Multimap<EntityAttribute, EntityAttributeModifier> multimap = held.getAttributeModifiers(EquipmentSlot.MAINHAND);
				
				if(!multimap.isEmpty()) {
					Iterator<Entry<EntityAttribute, EntityAttributeModifier>> iterator = multimap.entries().iterator();
					
					while(iterator.hasNext()) {
						Entry<EntityAttribute, EntityAttributeModifier> entry = iterator.next();
						EntityAttributeModifier entityAttributeModifier = (EntityAttributeModifier)entry.getValue();
						double value = entityAttributeModifier.getValue();
						
						if(entityAttributeModifier.getId().equals(ATTACK_DAMAGE_MODIFIER_ID)) {
							value += (double)EnchantmentHelper.getAttackDamage(held, EntityGroup.DEFAULT);
							cache -= value;
						}
					}
				}
			}
		}
		
		return cache;
	}
	
	@ModifyVariable(method = "getTooltip", at = @At(value = "STORE", ordinal = 3), name = "d", ordinal = 0)
	private double attackSpeedTooltip(double d, @Nullable PlayerEntity player, TooltipContext context) {
		ItemStack stack = (ItemStack)(Object)this;
		double cache = d;
		double modifier = 0;
		
		if(player != null) {
			AttributeData data = ExAPI.DATA.get(player);
			ItemStack held = player.getMainHandStack();
			cache -= player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
			modifier = cache;
			cache += data.get(PlayerAttributes.ATTACK_SPEED.get());
			
			if(stack.equals(held)) {
				cache -= modifier;
			} else {
				Multimap<EntityAttribute, EntityAttributeModifier> multimap = held.getAttributeModifiers(EquipmentSlot.MAINHAND);
				
				if(!multimap.isEmpty()) {
					Iterator<Entry<EntityAttribute, EntityAttributeModifier>> iterator = multimap.entries().iterator();
					
					while(iterator.hasNext()) {
						Entry<EntityAttribute, EntityAttributeModifier> entry = iterator.next();
						EntityAttributeModifier entityAttributeModifier = (EntityAttributeModifier)entry.getValue();
						double value = entityAttributeModifier.getValue();
						
						if(entityAttributeModifier.getId().equals(ATTACK_SPEED_MODIFIER_ID)) {
							cache -= value;
						}
					}
				}
			}
		}
		
		return cache;
	}
	*/
}
