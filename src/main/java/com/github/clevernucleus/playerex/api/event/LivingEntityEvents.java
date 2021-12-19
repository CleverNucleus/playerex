package com.github.clevernucleus.playerex.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

/**
 * Some useful hooks exposed mainly for compatibility purposes.
 * 
 * @author CleverNucleus
 *
 */
public final class LivingEntityEvents {
	
	/**
	 * Fired before {@link LivingEntity#heal(float)}; allows the amount healed to be modified before healing happens. 
	 * Setting the output to 0 is an unreliable way to negate incoming damage depending on other mods installed. Instead, use {@link LivingEntityEvents#HEAL}.
	 */
	public static final Event<IncomingHeal> INCOMING_HEAL = EventFactory.createArrayBacked(IncomingHeal.class, listeners -> (livingEntity, original) -> {
		float previous = original;
		
		for(IncomingHeal listener : listeners) {
			previous = listener.onHeal(livingEntity, previous);
		}
		
		return previous;
	});
	
	/**
	 * Fired at the start of {@link LivingEntity#heal(float)}, but before healing is applied. Can return false to cancel all healing, or true to allow it.
	 */
	public static final Event<Heal> HEAL = EventFactory.createArrayBacked(Heal.class, listeners -> (livingEntity, original) -> {
		for(Heal listener : listeners) {
			if(!listener.onHeal(livingEntity, original)) {
				return false;
			}
		}
		
		return true;
	});
	
	/**
	 * Fired at the start of {@link LivingEntity#tick()}.
	 */
	public static final Event<Tick> TICK = EventFactory.createArrayBacked(Tick.class, listeners -> livingEntity -> {
		for(Tick listener : listeners) {
			listener.onTick(livingEntity);
		}
	});
	
	/**
	 * Fired before {@link LivingEntity#damage(DamageSource, float)}; allows the amount of damage to be modified before it is used in any way.
	 * Can be used to perform logic prior to the damage method, and can return the original damage to avoid modifying the value.
	 * The original value is the incoming damage, followed by the result of this event by any previous registries. 
	 * Setting the output to 0 is an unreliable way to negate incoming damage depending on other mods installed. Instead, use {@link LivingEntityEvents#DAMAGE}.
	 */
	public static final Event<IncomingDamage> INCOMING_DAMAGE = EventFactory.createArrayBacked(IncomingDamage.class, listeners -> (livingEntity, source, original) -> {
		float previous = original;
		
		for(IncomingDamage listener : listeners) {
			previous = listener.onDamage(livingEntity, source, previous);
		}
		
		return previous;
	});
	
	/**
	 * Fired after:
	 * <p>
	 * {@link LivingEntity#isInvulnerableTo()}, 
	 * </p>
	 * <p>
	 * {@link net.minecraft.world.World#isClient}, 
	 * </p>
	 * <p>
	 * {@link LivingEntity#isDead()}, 
	 * </p>
	 * <p>
	 * ({@link DamageSource#isFire()} && {@link LivingEntity#hasStatusEffect(net.minecraft.entity.effect.StatusEffect)} for Fire Resistance)
	 * </p>
	 * <p>
	 * and {@link LivingEntity#isSleeping()}
	 * </p>
	 * <p>
	 *  is checked, but before all other logic is performed. Can be used to cancel the method and prevent damage from being taken by returning false.
	 *  Returning true allows the logic to continue.
	 */
	public static final Event<Damage> DAMAGE = EventFactory.createArrayBacked(Damage.class, listeners -> (livingEntity, source, original) -> {
		for(Damage listener : listeners) {
			if(!listener.onDamage(livingEntity, source, original)) {
				return false;
			}
		}
		
		return true;
	});
	
	@FunctionalInterface
	public interface IncomingHeal {
		float onHeal(final LivingEntity livingEntity, final float original);
	}
	
	@FunctionalInterface
	public interface Heal {
		boolean onHeal(final LivingEntity livingEntity, final float original);
	}
	
	@FunctionalInterface
	public interface Tick {
		void onTick(final LivingEntity livingEntity);
	}
	
	@FunctionalInterface
	public interface IncomingDamage {
		float onDamage(final LivingEntity livingEntity, final DamageSource source, final float original);
	}
	
	@FunctionalInterface
	public interface Damage {
		boolean onDamage(final LivingEntity livingEntity, final DamageSource source, final float original);
	}
}
