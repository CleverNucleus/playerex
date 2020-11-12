package github.clevernucleus.playerex.client.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;

import github.clevernucleus.playerex.api.ElementRegistry;
import github.clevernucleus.playerex.api.Util;
import github.clevernucleus.playerex.api.client.ClientReg;
import github.clevernucleus.playerex.api.client.Page;
import github.clevernucleus.playerex.api.element.IElement;
import github.clevernucleus.playerex.api.element.IPlayerElements;
import github.clevernucleus.playerex.common.init.Registry;
import github.clevernucleus.playerex.common.network.AddPlayerElement;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class DefaultPage extends Page {
	private final IElement[] idToElement = new IElement[] {ElementRegistry.CONSTITUTION, ElementRegistry.STRENGTH, ElementRegistry.DEXTERITY, ElementRegistry.INTELLIGENCE, ElementRegistry.LUCKINESS};
	private final List<DynamicTextComponent> dynamicTextComponents = new ArrayList<DynamicTextComponent>();
	private final DecimalFormat text = new DecimalFormat("##.##");
	
	private final DynamicTextComponent level = new DynamicTextComponent(20, 50, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.level", (int)ElementRegistry.LEVEL.get(par0, par1), (int)(100 * Util.expCoeff((float)ElementRegistry.LEVEL.get(par0, par1), (float)ElementRegistry.EXPERIENCE.get(par0, par1))), "%");
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.level.alt")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.LEVEL.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent skillPoints = new DynamicTextComponent(20, 62, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.skillpoints", (int)ElementRegistry.SKILLPOINTS.get(par0, par1));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.skillpoints.alt")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.SKILLPOINTS.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent constitution = new DynamicTextComponent(30, 86, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.constitution", (int)ElementRegistry.CONSTITUTION.get(par0, par1));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.constitution.alt0")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.constitution.alt1")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.constitution.alt2")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.constitution.alt3")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.constitution.alt4")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.CONSTITUTION.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent strength = new DynamicTextComponent(30, 110, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.strength", (int)ElementRegistry.STRENGTH.get(par0, par1));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.strength.alt0")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.strength.alt1")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.strength.alt2")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.strength.alt3")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.strength.alt4")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.strength.alt5")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.STRENGTH.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent dexterity = new DynamicTextComponent(30, 134, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.dexterity", (int)ElementRegistry.DEXTERITY.get(par0, par1));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.dexterity.alt0")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.dexterity.alt1")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.dexterity.alt2")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.dexterity.alt3")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.dexterity.alt4")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.dexterity.alt5")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.dexterity.alt6")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.DEXTERITY.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent intelligence = new DynamicTextComponent(30, 158, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.intelligence", (int)ElementRegistry.INTELLIGENCE.get(par0, par1));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.intelligence.alt0")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.intelligence.alt1")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.intelligence.alt2")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.intelligence.alt3")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.intelligence.alt4")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.intelligence.alt5")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.intelligence.alt6")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.INTELLIGENCE.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent luckiness = new DynamicTextComponent(30, 182, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.luckiness", (int)ElementRegistry.LUCKINESS.get(par0, par1));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.luckiness.alt0")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.luckiness.alt1")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.luckiness.alt2")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.luckiness.alt3")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.luckiness.alt4")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.LUCKINESS.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent health = new DynamicTextComponent(130, 50, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.health", text.format((par0.getHealth() + par0.getAbsorptionAmount())), text.format(par0.getMaxHealth()));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.health.alt0")));
		var0.add(new StringTextComponent(" "));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.health.alt1") + text.format(((400D / 9D) * ElementRegistry.HEALTH_REGEN.get(par0, par1))) + "/s"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.health.alt2") + text.format(100D * ElementRegistry.HEALTH_REGEN_AMP.get(par0, par1)) + "%"));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.HEALTH.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent armor = new DynamicTextComponent(130, 62, (par0, par1) -> {
		String var0 = text.format(par0.getAttribute(Attributes.ARMOR).getValue()) + " (+" + text.format(par0.getAttribute(Attributes.ARMOR).getValue() - par0.getAttribute(Attributes.ARMOR).getBaseValue()) + ")";
		TranslationTextComponent var1 = new TranslationTextComponent("playerex.attribute.armor", var0);
		
		return var1.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		String var1 = text.format(par0.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue()) + " (+" + text.format(par0.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue() - par0.getAttribute(Attributes.ARMOR_TOUGHNESS).getBaseValue()) + ")";
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.armor.alt0")));
		var0.add(new StringTextComponent(" "));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.armor.alt1") + var1));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.ARMOR.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent resistance = new DynamicTextComponent(130, 74, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.resistance", text.format(100D * ElementRegistry.KNOCKBACK_RESISTANCE.get(par0, par1)) + "%");
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.resistance.alt0") + text.format(100D * ElementRegistry.DAMAGE_RESISTANCE.get(par0, par1)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.resistance.alt1") + text.format(100D * ElementRegistry.FIRE_RESISTANCE.get(par0, par1)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.resistance.alt2") + text.format(100D * ElementRegistry.LAVA_RESISTANCE.get(par0, par1)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.resistance.alt3") + text.format(100D * ElementRegistry.EXPLOSION_RESISTANCE.get(par0, par1)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.resistance.alt4") + text.format(100D * ElementRegistry.FALLING_RESISTANCE.get(par0, par1)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.resistance.alt5") + text.format(100D * ElementRegistry.POISON_RESISTANCE.get(par0, par1)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.resistance.alt6") + text.format(100D * ElementRegistry.WITHER_RESISTANCE.get(par0, par1)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.resistance.alt7") + text.format(100D * ElementRegistry.DROWNING_RESISTANCE.get(par0, par1)) + "%"));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.KNOCKBACK_RESISTANCE.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent melee = new DynamicTextComponent(130, 98, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.melee", text.format(ElementRegistry.MELEE_DAMAGE.get(par0, par1)));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.melee.alt0") + text.format(100D + (100D * ElementRegistry.MELEE_CRIT_DAMAGE.get(par0, par1))) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.melee.alt1") + text.format(100D * ElementRegistry.MELEE_CRIT_CHANCE.get(par0, par1)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.melee.alt2") + text.format(ElementRegistry.ATTACK_SPEED.get(par0, par1))));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.MELEE_DAMAGE.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent ranged = new DynamicTextComponent(130, 110, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.ranged", text.format(ElementRegistry.RANGED_DAMAGE.get(par0, par1)));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.ranged.alt0") + text.format(100D + (100D * ElementRegistry.RANGED_CRIT_DAMAGE.get(par0, par1))) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.ranged.alt1") + text.format(100D * ElementRegistry.RANGED_CRIT_CHANCE.get(par0, par1)) + "%"));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.RANGED_DAMAGE.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent evasion = new DynamicTextComponent(130, 122, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.evasion", text.format(100D * ElementRegistry.EVASION_CHANCE.get(par0, par1)), "%");
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.evasion.alt0")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.EVASION_CHANCE.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent lifesteal = new DynamicTextComponent(130, 146, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.lifesteal", text.format(100D * ElementRegistry.LIFESTEAL.get(par0, par1)), "%");
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.lifesteal.alt0")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.LIFESTEAL.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent luck = new DynamicTextComponent(130, 158, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.luck", text.format(ElementRegistry.LUCK.get(par0, par1)));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.luck.alt0")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.LUCK.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent movement = new DynamicTextComponent(130, 182, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.movement", text.format(100D * ElementRegistry.MOVEMENT_SPEED_AMP.get(par0, par1)) + "%");
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.movement.alt0")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(ElementRegistry.MOVEMENT_SPEED_AMP.getRegistryName())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	public DefaultPage(ITextComponent par0) {
		super(par0);
		
		this.dynamicTextComponents.add(this.level);
		this.dynamicTextComponents.add(this.skillPoints);
		this.dynamicTextComponents.add(this.constitution);
		this.dynamicTextComponents.add(this.strength);
		this.dynamicTextComponents.add(this.dexterity);
		this.dynamicTextComponents.add(this.intelligence);
		this.dynamicTextComponents.add(this.luckiness);
		this.dynamicTextComponents.add(this.health);
		this.dynamicTextComponents.add(this.armor);
		this.dynamicTextComponents.add(this.resistance);
		this.dynamicTextComponents.add(this.melee);
		this.dynamicTextComponents.add(this.ranged);
		this.dynamicTextComponents.add(this.evasion);
		this.dynamicTextComponents.add(this.lifesteal);
		this.dynamicTextComponents.add(this.luck);
		this.dynamicTextComponents.add(this.movement);
	}
	
	@Override
	public ItemStack displayStack() {
		return new ItemStack(Items.PLAYER_HEAD);
	}
	
	@Override
	public void render(MatrixStack par0, int par1, int par2, float par3) {
		super.render(par0, par1, par2, par3);
		
		this.dynamicTextComponents.forEach(var -> var.drawAlt(par0, this.font, this.minecraft.player, this.width, this.height, par1, par2));
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(MatrixStack par0, int par1, int par2) {
		this.font.drawString(par0, this.title.getString(), 9F, 9F, 4210752);
		this.dynamicTextComponents.forEach(var -> var.draw(par0, this.font, this.minecraft.player));
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(MatrixStack par0, float par1, int par2, int par3) {
		PlayerEntity var0 = this.minecraft.player;
		
		ElementRegistry.GET_PLAYER_ELEMENTS.apply(var0).ifPresent(var -> {
			for(Widget var1 : this.buttons) {
				if(var1 instanceof TexturedButton) {
					TexturedButton var2 = (TexturedButton)var1;
					int var3 = var2.getAdditionalData();
					
					if(var3 >= 0 && var3 < this.idToElement.length) {
						double var4 = ElementRegistry.SKILLPOINTS.get(var0, var);
						
						var1.active = var4 > 0;
					}
				}
			}
		});
	}
	
	@Override
	protected void init(ContainerScreen<?> par1) {
		super.init(par1);
		
		for(int var = 0; var < this.idToElement.length; var++) {
			this.addButton(new TexturedButton(par1, 8, 58 + (17 * var), 11, 10, 204, 0, var, (var0, var1) -> {
				Registry.NETWORK.sendToServer(new AddPlayerElement(Pair.of(this.idToElement[var1], 1D), Pair.of(ElementRegistry.SKILLPOINTS, -1D)));
			}, null));
		}
	}
}
