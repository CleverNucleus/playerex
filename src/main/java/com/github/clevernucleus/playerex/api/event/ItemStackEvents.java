package com.github.clevernucleus.playerex.api.event;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Multimap;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Provides a hook into the ItemStack constructor and ItemStack#getAttributeModifiers.
 * 
 * @author CleverNucleus
 *
 */
public final class ItemStackEvents {
	
	/**
	 * Fired at the end of the itemstack constructor. The item may be null.
	 */
	public static final Event<ItemStackEvents.Constructor> ITEMSTACK_CONSTRUCTED = EventFactory.createArrayBacked(ItemStackEvents.Constructor.class, listeners -> (item, itemStack, count) -> {
		for(Constructor listener : listeners) {
			listener.onItemStackConstructor(item, itemStack, count);
		}
	});
	
	/**
	 * Fired on {@link ItemStack#getAttributeModifiers(EquipmentSlot)}. Allows for editing the modifiers multimap with ItemStack (i.e. NBT) arguments.
	 */
	public static final Event<ItemStackEvents.GetAttributeModifiers> ITEMSTACK_MODIFIERS = EventFactory.createArrayBacked(ItemStackEvents.GetAttributeModifiers.class, listeners -> (itemStack, modifiers, slot) -> {
		for(GetAttributeModifiers listener : listeners) {
			listener.getAttributeModifiers(itemStack, modifiers, slot);
		}
	});
	
	@FunctionalInterface
	public interface Constructor {
		
		/**
		 * 
		 * @param item
		 * @param itemStack
		 * @param count
		 */
		void onItemStackConstructor(@Nullable final Item item, final ItemStack itemStack, final int count);
	}
	
	@FunctionalInterface
	public interface GetAttributeModifiers {
		
		/**
		 * 
		 * @param itemStack
		 * @param modifiers
		 * @param slot
		 */
		void getAttributeModifiers(final ItemStack itemStack, final Multimap<EntityAttribute, EntityAttributeModifier> modifiers, final EquipmentSlot slot);
	}
}
