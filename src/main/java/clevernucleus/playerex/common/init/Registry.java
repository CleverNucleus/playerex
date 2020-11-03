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
import net.minecraft.block.Block;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
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
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import top.theillusivec4.curios.api.SlotTypeMessage;

/**
 * Mod registry. Holds all registry objects added by PlayerEx.
 */
@Mod.EventBusSubscriber(modid = PlayerEx.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {
	
	/** Network instance. */
	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(PlayerEx.MODID, "path"), () -> "1", "1"::equals, "1"::equals);
	
	/** List storing instances of every registered item. */
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	/** List storing instances of every registered block. */
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block MAGIC_ICE = register("magic_ice", new MagicIceBlock());
	
	public static final Item RELIC_AMULET = register("relic_amulet", new RelicItem(0.7F));
	public static final Item RELIC_BODY = register("relic_body", new RelicItem(0.7F));
	public static final Item RELIC_HEAD = register("relic_head", new RelicItem(0.7F));
	public static final Item RELIC_RING = register("relic_ring", new RelicItem(0.7F));
	public static final Item SMALL_HEALTH_POTION = register("small_health_potion", new HealthPotionItem(1, 0.8F));
	public static final Item MEDIUM_HEALTH_POTION = register("medium_health_potion", new HealthPotionItem(2, 0.7F));
	public static final Item LARGE_HEALTH_POTION = register("large_health_potion", new HealthPotionItem(3, 0.6F));
	public static final Item SUBTLE_KNIFE = register("subtle_knife", new SubtleKnifeItem(0.15F));
	public static final Item BOOM_STAFF = register("boom_staff", new BoomStaffItem(0.1F));
	public static final Item MJOLNIR = register("mjolnir", new MjolnirItem(0.2F));
	public static final Item ICE_AXE = register("ice_axe", new IceAxeItem(0.3F));
	public static final Item EXCALIBUR = register("excalibur", new ExcaliburItem(0.05F));
	public static final Item HEAL_BRANCH = register("heal_branch", new HealBranchItem(0.3F));
	public static final Item MULAGIR = register("mulagir", new MulagirItem(0.4F));
	
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
	 * Used to pass an block object and its registry name through to a list and returned again.
	 * @param par0 The registry name.
	 * @param par1 The block object.
	 * @return The block object, with its registry name set.
	 */
	private static Block register(final @Nonnull String par0, @Nonnull Block par1) {
		par1.setRegistryName(new ResourceLocation(PlayerEx.MODID, par0));
		
		BLOCKS.add(par1);
		
		register(par0, new BlockItem(par1, new Item.Properties().group(Group.INSTANCE)));
		
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
	 * Event handling the registration of items to the game.
	 * @param par0
	 */
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> par0) {
		for(Block var : BLOCKS) {
			par0.getRegistry().register(var);
		}
	}
	
	/**
	 * Event registering curios slots
	 * @param par0
	 */
	@SubscribeEvent
	public static void registerCurios(final InterModEnqueueEvent par0) {
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("necklace").build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("body").build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("head").build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("ring").build());
	}
}
