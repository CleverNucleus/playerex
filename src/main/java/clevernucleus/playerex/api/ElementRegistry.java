package clevernucleus.playerex.api;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Maps;

import clevernucleus.playerex.api.element.Element;
import clevernucleus.playerex.api.element.IElement;
import clevernucleus.playerex.api.element.IPlayerElements;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

/**
 * For modders using the api, this will be your main access point for PlayerEx.
 * 
 * Secondary Registry for API.
 */
public class ElementRegistry {
	
	/** Holds all Elements. */
	private static final Map<ResourceLocation, IElement> ELEMENTS = Maps.newHashMap();
	
	/** Capability access. */
	@CapabilityInject(IPlayerElements.class)
	public static final Capability<IPlayerElements> PLAYER_ELEMENTS = null;
	
	/** Capability pass-through function. */
	public static final Function<PlayerEntity, LazyOptional<IPlayerElements>> GET_PLAYER_ELEMENTS = var -> var.getCapability(PLAYER_ELEMENTS, null);
	
	/** The modid used to identify playerex. */
	public static final String MODID = "playerex";
	
	/** Instead of null we always use this! */
	public static final IElement NONE = null;
	
	public static final IElement CONSTITUTION = registerElement(new ResourceLocation(MODID, "constitution"), new Element(0D, 1D, 10D, IElement.Type.ALL) {
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.set(par0, par1, par2);
			
			ElementRegistry.HEALTH.set(par0, par1, par2);
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			super.add(par0, par1, par2);
			
			ElementRegistry.HEALTH.add(par0, par1, par2);
		}
	});
	public static final IElement STRENGTH = registerElement(new ResourceLocation(MODID, "strength"), new Element(0D, 1D, 10D, IElement.Type.ALL));
	public static final IElement DEXTERITY = registerElement(new ResourceLocation(MODID, "dexterity"), new Element(0D, 1D, 10D, IElement.Type.ALL));
	public static final IElement INTELLIGENCE = registerElement(new ResourceLocation(MODID, "intelligence"), new Element(0D, 1D, 10D, IElement.Type.ALL));
	public static final IElement LUCKINESS = registerElement(new ResourceLocation(MODID, "luck"), new Element(0D, 1D, 10D, IElement.Type.ALL));
	public static final IElement HEALTH = registerElement(new ResourceLocation("health"), new Element(0D, 1D, 10D, IElement.Type.GAME) {
		
		@Override
		public double get(PlayerEntity par0, IPlayerElements par1) {
			return par0.getMaxHealth();
		}
		
		@Override
		public void set(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233818_a_).setBaseValue(Math.max(1.0D, par2));
		}
		
		@Override
		public void add(PlayerEntity par0, IPlayerElements par1, double par2) {
			par0.getAttribute(Attributes.field_233818_a_).setBaseValue(par0.getAttribute(Attributes.field_233818_a_).getBaseValue() + par2);
			
			if(par2 < 0D) {
				par0.setHealth((par0.getHealth() + par2) < par0.getMaxHealth() ? par0.getMaxHealth() : ((par0.getHealth() + par2) < 1F) ? 1F : par0.getHealth());
			}
		}
	});
	
	/**
	 * Use this to register an element.
	 * @param par0 This should include your modid.
	 * @param par1 The element object; this should be fully constructed with no nulls.
	 * @return The now registered input element. This is a pass-through, but can be used as a stand-alone too.
	 */
	public static IElement registerElement(final ResourceLocation par0, final IElement par1) {
		par1.setRegistryName(par0);
		
		ELEMENTS.putIfAbsent(par0, par1);
		
		return par1;
	}
	
	/**
	 * @param par0 The element's id (resource location) - should contain a modid.
	 * @return Getter. Returns the mapped element to the input id; if that is null returns {@link ElementRegistry#NONE}
	 */
	public static IElement getElement(final ResourceLocation par0) {
		return ELEMENTS.getOrDefault(par0, NONE);
	}
	
	/**
	 * @return Returns an set containing all the resource location id's for all elements. Should be used only for iteration.
	 */
	public static Set<ResourceLocation> getRegistry() {
		return ELEMENTS.keySet();
	}
}
