package com.github.clevernucleus.playerex.handler;

import java.util.Random;
import java.util.function.Function;

import com.github.clevernucleus.playerex.api.ExAPI;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public final class EventHandler {
	private static <T> T ifPresent(final LivingEntity livingEntity, EntityAttribute attribute, final T fallback, Function<Float, T> function) {
		AttributeContainer container = livingEntity.getAttributes();
		if(attribute != null && container.hasAttribute(attribute)) {
			return function.apply((float)container.getValue(attribute));
		}
		
		return fallback;
	}
	
	public static void copyFrom(final ServerPlayerEntity oldPlayer, final ServerPlayerEntity newPlayer, final boolean isAlive) {
		//PlayerData playerData = ExAPI.INSTANCE.get(newPlayer);
		//playerData.reset(); // TODO if we want everything to reset after dying
	}
	
	public static float heal(final LivingEntity livingEntity, final float amount) {
		return ifPresent(livingEntity, ExAPI.HEAL_AMPLIFICATION.get(), amount, value -> {
			return amount * (1.0F + value);
		});
	}
	
	public static void tick(final LivingEntity livingEntity) {
		if(livingEntity.world.isClient) return;
		
		ifPresent(livingEntity, ExAPI.HEALTH_REGENERATION.get(), (Object)null, value -> {
			if(value > 0.0F && livingEntity.getHealth() < livingEntity.getMaxHealth()) {
				livingEntity.heal(value);
			}
			
			return (Object)null;
		});
	}
	
	public static float damageModified(final LivingEntity livingEntity, final DamageSource source, final float original) {
		return original; // TODO resistances and amps
		
		/*
		 * Fire damage <- can do now
		 * 	- isFire()
		 * Freeze damage <- can do now also do we maybe want to dub this 'frost' instead of 'freeze'?
		 *  - equals(FREEZE)
		 * Wither Damage <- can do now
		 *  - equals(WITHER)
		 * Radiant Damage
		 * Poison Damage <- can do now
		 *  - equals(POISON)
		 * Lightning <- can do now maybe
		 *  - equals(LIGHTNING_BOLT) we would dub it as 'lightning damage'
		 * Bludgeoning (explosion damage?)
		 * Piercing
		 * Slashing
		 * Force? would this be like explosion damage?
		 * ... maybe some more ? we want d&d a theme ideally
		 * 
		 * What we had before:
		 * Magic Damage Resistance (sucked and didn't work across mods very well, not to mention what do we do about potions).
		 * Also, what if we fire magic fireballs? are they magic damage or fire damage? I would say fire which is why this is a bad damage type.
		 * 
		 * Fire Damage Resistance
		 * Freeze Damage Resistance
		 * Drowning Damage Resistance (what kind of damage type would this be thematically?) I think we just remove this one.
		 * Falling Damage Resistance (this is basically bludgeoning, but if we called it that would people know what that meant?)
		 * Wither Resistance
		 * 
		 * 
		 * 
		 * So listing it on the resistances section (can really only justify 6, not including the empty space on the combat page): 
		 * 
		 * Also, do we say 'x Damage Resistance' or just 'x Resistance'?
		 * 
		 * Fire Resistance
		 * Freeze/Frost/Cold Resistance
		 * Lightning Resistance
		 * Poison Resistance
		 * Wither Resistance
		 * Radiant Resistance
		 * 
		 * 
		 * We don't have anything we can do with Radiant damage yet, or ever in PlayerEx, so we need to think abut if we add it before
		 * we have an addon mod that uses it. Kind of the same deal with Lightning damage.
		 * 
		 * Additionally, I think we'd rather have 5 attributes listed instead of 6 because it's just more aesthetically pleasing on the gui.
		 * Also, including Lifesteal and Attack Range (which is implemented in another mod) we have an ideal 3-4 slots to fill on combat page:
		 * Slashing Resistance
		 * Bludgeoning Resistance
		 * Piercing Resistance
		 * 
		 * these would be ideal to put there. Unlike in dnd though we have a percentage reduction in damage instead of just 50%, else we could
		 * use simple booleans/DataTracker.
		 * 
		 * Not quite sure yet on how I would mixin to these damage types to ensure that Magic damage is always differentiated into categories.
		 * Maybe we use Wrappers with static a static enum/get map? We input damage type and attacker/source and it returns our category.
		 * 
		 */
	}
	
	public static boolean onDamage(final LivingEntity livingEntity, final DamageSource source, final float original) {
		Entity origin = source.getSource();
		Entity attacker = source.getAttacker();
		
		if(attacker instanceof LivingEntity && (origin instanceof LivingEntity || origin instanceof PersistentProjectileEntity)) {
			LivingEntity user = (LivingEntity)attacker;
			
			ifPresent(user, ExAPI.LIFESTEAL.get(), (Object)null, value -> {
				user.heal(original * value);
				
				return (Object)null;
			});
		}
		
		return ifPresent(livingEntity, ExAPI.EVASION.get(), true, value -> {
			float chance = (new Random()).nextFloat();
			return !(chance < value && origin instanceof PersistentProjectileEntity);
		});
	}
	
	public static boolean attackCritChance(final PlayerEntity player, final Entity target, final boolean vanilla) {
		if(!(target instanceof LivingEntity)) return vanilla;
		
		return ifPresent(player, ExAPI.MELEE_CRIT_CHANCE.get(), vanilla, value -> {
			float chance = (new Random()).nextFloat();
			return (chance < value) && !player.isClimbing() && !player.isTouchingWater() && !player.hasStatusEffect(StatusEffects.BLINDNESS) && !player.hasVehicle();
		});
	}
	
	public static float attackCritDamage(final PlayerEntity player, final Entity target, final float amount) {
		if(!(target instanceof LivingEntity)) return amount;
		
		return ifPresent(player, ExAPI.MELEE_CRIT_DAMAGE.get(), amount, value -> {
			return amount * (1.0F + value) / 1.5F;
		});
	}
}
