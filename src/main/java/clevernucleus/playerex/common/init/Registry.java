package clevernucleus.playerex.common.init;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import clevernucleus.playerex.api.element.IPlayerElements;
import clevernucleus.playerex.api.element.PlayerElements;
import clevernucleus.playerex.common.PlayerEx;
import clevernucleus.playerex.common.init.container.PlayerElementsContainer;
import clevernucleus.playerex.common.init.item.*;
import clevernucleus.playerex.common.network.AddPlayerElement;
import clevernucleus.playerex.common.network.SwitchScreens;
import clevernucleus.playerex.common.network.SyncPlayerElements;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
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
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * Mod registry. Holds all registry objects added by PlayerEx.
 */
@Mod.EventBusSubscriber(modid = PlayerEx.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {
	
	/** Network instance. */
	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(PlayerEx.MODID, "path"), () -> "1", "1"::equals, "1"::equals);
	
	/** List storing instances of every registered item. */
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item RELIC_AMULET = register("relic_amulet", new RelicItem(0.7F));
	public static final Item RELIC_BODY = register("relic_body", new RelicItem(0.7F));
	public static final Item RELIC_HEAD = register("relic_head", new RelicItem(0.7F));
	public static final Item RELIC_RING = register("relic_ring", new RelicItem(0.7F));
	
	/** Static identifier for the player elements container type. */
	public static final ContainerType<PlayerElementsContainer> ELEMENTS_CONTAINER = register("elements", IForgeContainerType.create((var0, var1, var2) -> new PlayerElementsContainer(var0, var1)));
	
	/**
	 * Used to pass an item object and its registry name through to a list and returned again.
	 * @param par0 The registry name.
	 * @param par1 The item object.
	 * @return The item object, with its registry name set.
	 */
	private static Item register(final @Nonnull String par0, @Nonnull Item par1) {
		par1.setRegistryName(new ResourceLocation(PlayerEx.MODID, par0));
		
		ITEMS.add(par1);
		
		return par1;
	}
	
	/**
	 * Used to pass a container type and its registry name through to a list and returned again.
	 * @param par0 The registry name.
	 * @param par1 The container type object.
	 * @return The container type object, with its registry name set.
	 */
	private static <T extends Container> ContainerType<T> register(final @Nonnull String par0, ContainerType<T> par1) {
		par1.setRegistryName(new ResourceLocation(PlayerEx.MODID, par0));
		
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
	
	/**
	 * Event handling the registration of items to the game.
	 * @param par0
	 */
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> par0) {
		for(Item var : ITEMS) {
			par0.getRegistry().register(var);
		}
	}
	
	/**
	 * Event registering curios slots
	 * @param par0
	 */
	@SubscribeEvent
	public static void registerCurios(final InterModEnqueueEvent par0) {
		/*InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("necklace"));
		InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("body"));
		InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("head"));
		InterModComms.sendTo(CuriosAPI.MODID, CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("ring"));*/
	}
}
