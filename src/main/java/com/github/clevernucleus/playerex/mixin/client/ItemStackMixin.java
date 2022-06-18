package com.github.clevernucleus.playerex.mixin.client;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.github.clevernucleus.dataattributes.api.attribute.IEntityAttribute;
import com.github.clevernucleus.dataattributes.api.item.ItemFields;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.ClientUtil;
import com.github.clevernucleus.playerex.config.ConfigImpl;
import com.google.common.collect.Multimap;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

@Mixin(ItemStack.class)
abstract class ItemStackMixin {
	
	@Unique
	private ConfigImpl.Tooltip playerex_tooltip() {
		return ((ConfigImpl)ExAPI.getConfig()).tooltip();
	}
	
	@Unique
	private double playerex_modifyValue(double d, PlayerEntity player, EntityAttribute attribute, UUID uuid) {
		if(player == null || playerex_tooltip() == ConfigImpl.Tooltip.DEFAULT) return d;
		double e = d - player.getAttributeBaseValue(attribute);
		EntityAttributeInstance instance = player.getAttributeInstance(attribute);
		EntityAttributeModifier modifier = instance.getModifier(uuid);
		double value = player.getAttributeValue(attribute) + e;
		
		if(modifier != null) {
			value -= modifier.getValue();
		}
		
		return playerex_tooltip() == ConfigImpl.Tooltip.PLAYEREX ? e : value;
	}
	
	@Unique
	private String playerex_value(double e, Map.Entry<EntityAttribute, EntityAttributeModifier> entry, EntityAttributeModifier modifier) {
		if(modifier.getOperation() != EntityAttributeModifier.Operation.ADDITION) return ItemStack.MODIFIER_FORMAT.format(e);
		EntityAttribute attribute = entry.getKey();
		String value = ItemStack.MODIFIER_FORMAT.format(ClientUtil.displayValue(attribute, e));
		
		if(((IEntityAttribute)attribute).hasProperty(ExAPI.PERCENTAGE_PROPERTY)) {
			value += "%";
		}
		
		return value;
	}
	
	@ModifyVariable(method = "getTooltip", at = @At(value = "STORE", ordinal = 1), ordinal = 0)
	private double playerex_modifyAttackDamage(double d, PlayerEntity player) {
		return playerex_modifyValue(d, player, EntityAttributes.GENERIC_ATTACK_DAMAGE, ItemFields.attackDamageModifierID());
	}
	
	@ModifyVariable(method = "getTooltip", at = @At(value = "STORE", ordinal = 3), ordinal = 0)
	private double playerex_modifyAttackSpeed(double d, PlayerEntity player) {
		return playerex_modifyValue(d, player, EntityAttributes.GENERIC_ATTACK_SPEED, ItemFields.attackSpeedModifierID());
	}
	
	@ModifyVariable(method = "getTooltip", at = @At(value = "STORE", ordinal = 1), ordinal = 0)
	private boolean playerex_flagAttackDamage(boolean bl) {
		return playerex_tooltip() == ConfigImpl.Tooltip.PLAYEREX ? false : bl;
	}
	
	@ModifyVariable(method = "getTooltip", at = @At(value = "STORE", ordinal = 2), ordinal = 0)
	private boolean playerex_flagAttackSpeed(boolean bl) {
		return playerex_tooltip() == ConfigImpl.Tooltip.PLAYEREX ? false : bl;
	}
	
	@ModifyVariable(method = "getTooltip", at = @At(value = "STORE", ordinal = 1), ordinal = 1)
	private double playerex_modifyAdditionAttributeKnockback(double e, PlayerEntity player) {
		return e / 10.0D;
	}
	
	@Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 7, shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void playerex_insertModifierEqualsTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info, List<Text> list, MutableText arg3, int arg4, EquipmentSlot[] arg5, int arg6, int arg7, EquipmentSlot arg8, Multimap<?, ?> arg9, Iterator<?> arg10, Map.Entry<EntityAttribute, EntityAttributeModifier> entry, EntityAttributeModifier entityAttributeModifier, double arg13, double e) {
		list.set(list.size() - 1, new LiteralText(" ").append(new TranslatableText("attribute.modifier.equals." + entityAttributeModifier.getOperation().getId(), playerex_value(e, entry, entityAttributeModifier), new TranslatableText(entry.getKey().getTranslationKey()))).formatted(Formatting.DARK_GREEN));
	}
	
	@Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 8, shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void playerex_insertModifierPlusTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info, List<Text> list, MutableText arg3, int arg4, EquipmentSlot[] arg5, int arg6, int arg7, EquipmentSlot arg8, Multimap<?, ?> arg9, Iterator<?> arg10, Map.Entry<EntityAttribute, EntityAttributeModifier> entry, EntityAttributeModifier entityAttributeModifier, double arg13, double e) {
		list.set(list.size() - 1, new TranslatableText("attribute.modifier.plus." + entityAttributeModifier.getOperation().getId(), playerex_value(e, entry, entityAttributeModifier), new TranslatableText(entry.getKey().getTranslationKey())).formatted(Formatting.BLUE));
	}
	
	@Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 9, shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void playerex_insertModifierTakeTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info, List<Text> list, MutableText arg3, int arg4, EquipmentSlot[] arg5, int arg6, int arg7, EquipmentSlot arg8, Multimap<?, ?> arg9, Iterator<?> arg10, Map.Entry<EntityAttribute, EntityAttributeModifier> entry, EntityAttributeModifier entityAttributeModifier, double arg13, double e) {
		list.set(list.size() - 1, new TranslatableText("attribute.modifier.take." + entityAttributeModifier.getOperation().getId(), playerex_value(e, entry, entityAttributeModifier), new TranslatableText(entry.getKey().getTranslationKey())).formatted(Formatting.RED));
	}
}
