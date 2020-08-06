package clevernucleus.playerex.common.init;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import clevernucleus.playerex.common.PlayerEx;
import clevernucleus.playerex.common.init.capability.IPlayerElements;
import clevernucleus.playerex.common.init.capability.PlayerElements;
import clevernucleus.playerex.common.init.capability.SyncPlayerElements;
import clevernucleus.playerex.common.init.element.*;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * Mod registry. Holds all registry objects added by PlayerEx.
 */
@Mod.EventBusSubscriber(modid = PlayerEx.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {
	
	/** Holder for iterating over all elements. */
	public static final Set<IElement<WritableElement>> WRITABLE_ELEMENTS = new HashSet<IElement<WritableElement>>();
	
	/** Network instance. */
	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(PlayerEx.MODID, "path"), () -> "1", "1"::equals, "1"::equals);
	
	/** Capability access. */
	@CapabilityInject(IPlayerElements.class)
	public static final Capability<IPlayerElements> CAPABILITY = null;
	
	/** Capability pass-through function. */
	public static final Function<PlayerEntity, LazyOptional<IPlayerElements>> ELEMENTS = var -> var.getCapability(CAPABILITY, null);
	
	public static final IElement<WritableElement> CONSTITUTION = new WritableElement("Constitution", 6F, 1F, 10F, (par0, par1, par2) -> {
		par1.add(par0, Registry.HEALTH, par2);
	});
	
	public static final IElement<Element> HEALTH = new Element("Health", 1F, 10F, (par0, par1, par2) -> {
		par0.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(par2.doubleValue() + par0.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getValue());
		
		if(par2 < 0D) {
			par0.setHealth((par0.getHealth() + par2) < par0.getMaxHealth() ? par0.getMaxHealth() : ((par0.getHealth() + par2) < 1F) ? 1F : par0.getHealth());
		}
	}, (par0, par1) -> (double)par0.getMaxHealth());
	
	/**
	 * Mod initialisation event.
	 * @param par0
	 */
	@SubscribeEvent
	public static void commonSetup(final FMLCommonSetupEvent par0) {
		CapabilityManager.INSTANCE.register(IPlayerElements.class, new Capability.IStorage<IPlayerElements>() {
			
			@Override
			public INBT writeNBT(Capability<IPlayerElements> par0, IPlayerElements par1, Direction par2) {
				return par1.write();
			}
			
			@Override
			public void readNBT(Capability<IPlayerElements> par0, IPlayerElements par1, Direction par2, INBT par3) {
				par1.read((CompoundNBT)par3);
			}
		}, PlayerElements::new);
		
		NETWORK.registerMessage(0, SyncPlayerElements.class, SyncPlayerElements::encode, SyncPlayerElements::decode, SyncPlayerElements::handle);
	}
}
