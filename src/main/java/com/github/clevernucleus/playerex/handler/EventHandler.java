package com.github.clevernucleus.playerex.handler;

import java.util.Random;

import org.apache.commons.lang3.mutable.MutableDouble;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.dataattributes.api.attribute.IEntityAttribute;
import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerData;
import com.github.clevernucleus.playerex.config.ConfigImpl;
import com.github.clevernucleus.playerex.impl.DamageModificationImpl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public final class EventHandler {
	public static void serverStarting(MinecraftServer server) {
		PlayerEx.MANAGER.load();
		((ConfigImpl)ExAPI.getConfig()).init();
	}
	
	public static void copyFrom(final ServerPlayerEntity oldPlayer, final ServerPlayerEntity newPlayer, final boolean isAlive) {
		if(ExAPI.getConfig().resetOnDeath()) {
			PlayerData playerData = ExAPI.INSTANCE.get(newPlayer);
			playerData.reset();
		}
	}
	
	public static void clamp(final EntityAttribute attributeIn, MutableDouble valueIn) {
		IEntityAttribute attribute = (IEntityAttribute)attributeIn;
		
		if(attribute.hasProperty(ExAPI.INTEGER_PROPERTY)) {
			final float cache = valueIn.floatValue();
			final int rounded = Math.round(cache);
			
			valueIn.setValue(rounded);
		}
	}
	
	public static float heal(final LivingEntity livingEntity, final float amount) {
		return DataAttributesAPI.ifPresent(livingEntity, ExAPI.HEAL_AMPLIFICATION, amount, value -> {
			return amount * (1.0F + (value * 10.0F));
		});
	}
	
	public static float damageModified(final LivingEntity livingEntity, final DamageSource source, final float original) {
		float amount = original;
		
		for(var condition : DamageModificationImpl.get()) {
			float damage = amount;
			
			amount = condition.apply((predicate, function) -> {
				if(predicate.test(livingEntity, source, damage)) {
					return function.apply(livingEntity, source, damage);
				} else {
					return damage;
				}
			});
		}
		
		return amount;
	}
	
	public static boolean onDamage(final LivingEntity livingEntity, final DamageSource source, final float original) {
		if(original == 0.0F) return false;
		
		Entity origin = source.getSource();
		Entity attacker = source.getAttacker();
		
		if(attacker instanceof LivingEntity && (origin instanceof LivingEntity || origin instanceof PersistentProjectileEntity)) {
			LivingEntity user = (LivingEntity)attacker;
			
			DataAttributesAPI.ifPresent(user, ExAPI.LIFESTEAL, (Object)null, value -> {
				user.heal(original * value * 10.0F);
				
				return (Object)null;
			});
		}
		
		return DataAttributesAPI.ifPresent(livingEntity, ExAPI.EVASION, true, value -> {
			float chance = (new Random()).nextFloat();
			return !(chance < value && origin instanceof PersistentProjectileEntity);
		});
	}
	
	public static boolean attackCritChance(final PlayerEntity player, final Entity target, final boolean vanilla) {
		if(!(target instanceof LivingEntity)) return vanilla;
		
		return DataAttributesAPI.ifPresent(player, ExAPI.MELEE_CRIT_CHANCE, vanilla, value -> {
			float chance = (new Random()).nextFloat();
			return (chance < value) && !player.isClimbing() && !player.isTouchingWater() && !player.hasStatusEffect(StatusEffects.BLINDNESS) && !player.hasVehicle();
		});
	}
	
	public static float attackCritDamage(final PlayerEntity player, final Entity target, final float amount) {
		if(!(target instanceof LivingEntity)) return amount;
		
		return DataAttributesAPI.ifPresent(player, ExAPI.MELEE_CRIT_DAMAGE, amount, value -> {
			return amount * (1.0F + (value * 10.0F)) / 1.5F;
		});
	}
}
