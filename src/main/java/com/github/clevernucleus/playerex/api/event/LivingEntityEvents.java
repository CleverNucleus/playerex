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
	 * Setting the output to 0 is an unreliable way to negate incoming damage depending on other mods installed. Instead, use {@link LivingEntityEvents#SHOUL_HEAL}.
	 */
	public static final Event<Healed> ON_HEAL = EventFactory.createArrayBacked(Healed.class, callbacks -> (livingEntity, original) -> {
		float previous = original;
		
		for(Healed callback : callbacks) {
			previous = callback.onHeal(livingEntity, previous);
		}
		
		return previous;
	});
	
	/**
	 * Fired at the start of {@link LivingEntity#heal(float)}, but before healing is applied. Can return false to cancel all healing, or true to allow it.
	 */
	public static final Event<Heal> SHOULD_HEAL = EventFactory.createArrayBacked(Heal.class, callbacks -> (livingEntity, original) -> {
		for(Heal callback : callbacks) {
			if(!callback.shouldHeal(livingEntity, original)) return false;
		}
		
		return true;
	});
	
	/**
	 * Fired once at the end of {@link LivingEntity#tick()}, every 20 ticks (1 second).
	 */
	public static final Event<Tick> EVERY_SECOND = EventFactory.createArrayBacked(Tick.class, callbacks -> livingEntity -> {
		for(Tick callback : callbacks) {
			callback.everySecond(livingEntity);
		}
	});
	
	/**
	 * Fired before {@link LivingEntity#damage(DamageSource, float)}; allows the amount of damage to be modified before it is used in any way.
	 * Can be used to perform logic prior to the damage method, and can return the original damage to avoid modifying the value.
	 * The original value is the incoming damage, followed by the result of this event by any previous registries. 
	 * Setting the output to 0 is an unreliable way to negate incoming damage depending on other mods installed. Instead, use {@link LivingEntityEvents#SHOUL_DAMAGE}.
	 */
	public static final Event<Damaged> ON_DAMAGE = EventFactory.createArrayBacked(Damaged.class, callbacks -> (livingEntity, source, original) -> {
		float previous = original;
		
		for(Damaged callback : callbacks) {
			previous = callback.onDamage(livingEntity, source, previous);
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
	public static final Event<Damage> SHOULD_DAMAGE = EventFactory.createArrayBacked(Damage.class, callbacks -> (livingEntity, source, original) -> {
		for(Damage callback : callbacks) {
			if(!callback.shouldDamage(livingEntity, source, original)) return false;
		}
		
		return true;
	});
	
	@FunctionalInterface
	public interface Healed {
		float onHeal(final LivingEntity livingEntity, final float original);
	}
	
	@FunctionalInterface
	public interface Heal {
		boolean shouldHeal(final LivingEntity livingEntity, final float original);
	}
	
	@FunctionalInterface
	public interface Tick {
		void everySecond(final LivingEntity livingEntity);
	}
	
	@FunctionalInterface
	public interface Damaged {
		float onDamage(final LivingEntity livingEntity, final DamageSource source, final float original);
	}
	
	@FunctionalInterface
	public interface Damage {
		boolean shouldDamage(final LivingEntity livingEntity, final DamageSource source, final float original);
	}
}
