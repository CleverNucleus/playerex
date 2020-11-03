package clevernucleus.playerex.common.init.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * The creative tab object for this mod.
 */
public class Group extends ItemGroup {
	
	/** Retrieves an instance of this creative tab. */
	public static final Group INSTANCE = new Group();
	
	private Group() {
		super(ItemGroup.GROUPS.length, "playerex_tab");
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(net.minecraft.item.Items.SUGAR);
	}
}
