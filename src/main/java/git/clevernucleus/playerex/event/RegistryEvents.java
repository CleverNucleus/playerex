package git.clevernucleus.playerex.event;

import javax.annotation.Nonnull;

import git.clevernucleus.playerex.api.ExAPI;
import git.clevernucleus.playerex.api.element.IPlayerElements;
import git.clevernucleus.playerex.api.element.PlayerElements;
import git.clevernucleus.playerex.container.PlayerElementsContainer;
import git.clevernucleus.playerex.network.AddPlayerElement;
import git.clevernucleus.playerex.network.SwitchScreens;
import git.clevernucleus.playerex.network.SyncPlayerElements;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = ExAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {
	
	/** Network instance. */
	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(ExAPI.MODID, "path"), () -> "1", "1"::equals, "1"::equals);
	
	/** Static identifier for the player elements container type. */
	public static final ContainerType<PlayerElementsContainer> ELEMENTS_CONTAINER = register("elements", IForgeContainerType.create((var0, var1, var2) -> new PlayerElementsContainer(var0, var1)));
	
	/**
	 * Used to pass a container type and its registry name through to a list and returned again.
	 * @param par0 The registry name.
	 * @param par1 The container type object.
	 * @return The container type object, with its registry name set.
	 */
	private static <T extends Container> ContainerType<T> register(final @Nonnull String par0, ContainerType<T> par1) {
		par1.setRegistryName(new ResourceLocation(ExAPI.MODID, par0));
		
		return par1;
	}
	
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
		NETWORK.registerMessage(1, SwitchScreens.class, SwitchScreens::encode, SwitchScreens::decode, SwitchScreens::handle);
		NETWORK.registerMessage(2, AddPlayerElement.class, AddPlayerElement::encode, AddPlayerElement::decode, AddPlayerElement::handle);
	}
	
	/**
	 * Event handling the registration of container types.
	 * @param par0
	 */
	@SubscribeEvent
	public static void registerContainerTypes(final RegistryEvent.Register<ContainerType<?>> par0) {
		par0.getRegistry().register(ELEMENTS_CONTAINER);
	}
}
