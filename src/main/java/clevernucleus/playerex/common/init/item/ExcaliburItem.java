package clevernucleus.playerex.common.init.item;

import net.minecraft.item.ItemTier;
import net.minecraft.item.SwordItem;

public class ExcaliburItem extends SwordItem implements ILoot {
	private final float weight;
	
	public ExcaliburItem(final float par0) {
		super(ItemTier.DIAMOND, 4, 4, new Properties().group(Group.INSTANCE));
		this.weight = par0;
	}
	
	@Override
	public float getWight() {
		return this.weight;
	}
}
