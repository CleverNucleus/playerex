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
	
	public static void tick(final LivingEntity livingEntity) {
		if(livingEntity.world.isClient) return;
		
		DataAttributesAPI.ifPresent(livingEntity, ExAPI.HEALTH_REGENERATION, (Object)null, value -> {
			if(value > 0.0F && livingEntity.getHealth() < livingEntity.getMaxHealth()) {
				livingEntity.heal(value);
			}
			
			return (Object)null;
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
		
		/*
		 * We need a predicate for each *resistance/immunity*
		 * We have a registry of predicates and loop through those here
		
		if(origin instanceof PersistentProjectileEntity) {
			// Piercing damage
		} else if(origin instanceof LivingEntity) {
			// We know it's a LivingEntity attacking us
			// Now test *how* it's attacking us
			
			LivingEntity attacker = (LivingEntity)origin;
			ItemStack weapon = attacker.getMainHandStack();
			
			if(weapon != ItemStack.EMPTY) {
				// Now we know what type of weapon it is
				// Hence we can determine if it is:
				//  - Bludgeoning
				//  - Slashing
				//  - Piercing
				
				// Maybe we provide a percentage/weighted distribution of damage types instead of just returning one type
				// Do we base this off of raw nbt from this itemstack or from a local item method that takes itemstack as a parameter
				
			}
		}
		
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
