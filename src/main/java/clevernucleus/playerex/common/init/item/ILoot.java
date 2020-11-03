package clevernucleus.playerex.common.init.item;

/**
 * Defines the implementations weighting on randomly dropping this.
 */
public interface ILoot {
	
	/**
	 * @return The weighting for random getter from 0F - 1F.
	 */
	float getWeight();
}
