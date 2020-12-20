package git.clevernucleus.playerex.event;

import git.clevernucleus.playerex.api.ExAPI;
import git.clevernucleus.playerex.api.element.IPlayerElements;
import git.clevernucleus.playerex.api.element.PlayerElements;
import git.clevernucleus.playerex.network.SyncPlayerElements;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = ExAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {
	
	/** Network instance. */
	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(ExAPI.MODID, "path"), () -> "1", "1"::equals, "1"::equals);
	
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
