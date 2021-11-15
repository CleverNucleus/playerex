package com.github.clevernucleus.playerex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.playerex.api.event.ItemStackEvents;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
abstract class ItemStackMixin {
	
	@Inject(method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V", at = @At("TAIL"))
	private void constructorCall(ItemConvertible item, int count, CallbackInfo info) {
		Item itemIn = item == null ? null : item.asItem();
		ItemStack stack = (ItemStack)(Object)this;
		
		ItemStackEvents.ITEMSTACK_CONSTRUCTED.invoker().onItemStackConstructor(itemIn, stack, count);
	}
	
	@ModifyVariable(method = "getAttributeModifiers", at = @At(value = "STORE", ordinal = 1), ordinal = 0)
	private Multimap<EntityAttribute, EntityAttributeModifier> modifyItemStackModifiers(Multimap<EntityAttribute, EntityAttributeModifier> original, EquipmentSlot slot) {
		ItemStack stack = (ItemStack)(Object)this;
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create(original);
		ItemStackEvents.ITEMSTACK_MODIFIERS.invoker().getAttributeModifiers(stack, modifiers, slot);
		
		return original;
	}
}
