package com.github.clevernucleus.playerex.api;

import java.util.function.BiFunction;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;

/**
 * Cardinal Components PlayerEx component interface. Used to access and change PlayerEx's attribute modifiers.
 * 
 * @author CleverNucleus
 *
 */
public interface PlayerData extends Component {
	
	/**
	 * @param attributeIn
	 * @return the cached attribute modifier value, or 0 if it doesn't exist.
	 */
	double get(final EntityAttribute attributeIn);
	
	/**
	 * Sets the attribute modifier value for this attribute, and creates it if it doesn't exist.
	 * @param attributeIn
	 * @param valueIn
	 */
	void set(final EntityAttribute attributeIn, final double valueIn);
	
	/**
	 * Combination of {@link #get(EntityAttribute)} and {@link #set(EntityAttribute, double)}.
	 * @param attributeIn
	 * @param valueIn
	 */
	void add(final EntityAttribute attributeIn, final double valueIn);
	
	/**
	 * Removes the attribute modifier if it exists.
	 * @param attributeIn
	 */
	void remove(final EntityAttribute attributeIn);
	
	/**
	 * Resets all data (including attribute modifiers) to their defaults - for modifiers this means removing them and deleting the cache.
	 */
	void reset();
	
	/**
	 * Adds skill points to the player.
	 * @param pointsIn
	 */
	void addSkillPoints(final int pointsIn);
	
	/**
	 * Adds refund points to the player.
	 * @param pointsIn
	 * @return
	 */
	int addRefundPoints(final int pointsIn);
	
	/**
	 * @return Current skill points.
	 */
	int skillPoints();
	
	/**
	 * @return Current refund points.
	 */
	int refundPoints();
	
	/**
	 * Used by {@link #addRefundPoints(int)} to help calculate the maximum number of refund points that can be added. 
	 * The output of the function is how many refund points are added to the maximum.
	 * @param condition
	 */
	public static void registerRefundCondition(final BiFunction<PlayerData, LivingEntity, Double> condition) {
		com.github.clevernucleus.playerex.impl.PlayerDataManager.addRefundCondition(condition);
	}
}
