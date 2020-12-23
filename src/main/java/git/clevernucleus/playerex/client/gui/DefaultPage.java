package git.clevernucleus.playerex.client.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;

import git.clevernucleus.playerex.api.ExAPI;
import git.clevernucleus.playerex.api.Util;
import git.clevernucleus.playerex.api.client.ClientReg;
import git.clevernucleus.playerex.api.client.Page;
import git.clevernucleus.playerex.api.element.Element;
import git.clevernucleus.playerex.api.element.Elements;
import git.clevernucleus.playerex.api.element.IPlayerElements;
import git.clevernucleus.playerex.event.RegistryEvents;
import git.clevernucleus.playerex.network.AddPlayerElement;
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
	private final Element[] idToElement = new Element[] {Elements.CONSTITUTION, Elements.STRENGTH, Elements.DEXTERITY, Elements.INTELLIGENCE, Elements.LUCKINESS};
	private final List<DynamicTextComponent> dynamicTextComponents = new ArrayList<DynamicTextComponent>();
	private final DecimalFormat text = new DecimalFormat("##.##");
	
	private final DynamicTextComponent level = new DynamicTextComponent(20, 50, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.level", (int)par1.get(par0, Elements.LEVEL), (int)(100 * Util.expCoeff(par1.get(par0, Elements.LEVEL), par1.get(par0, Elements.EXPERIENCE))), "%");
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.level.alt")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.LEVEL.registry())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent skillPoints = new DynamicTextComponent(20, 62, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.skillpoints", (int)par1.get(par0, Elements.SKILLPOINTS));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.skillpoints.alt")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.SKILLPOINTS.registry())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent constitution = new DynamicTextComponent(30, 86, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.constitution", (int)par1.get(par0, Elements.CONSTITUTION));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.constitution.alt0")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.constitution.alt1")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.constitution.alt2")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.constitution.alt3")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.constitution.alt4")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.CONSTITUTION.registry())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent strength = new DynamicTextComponent(30, 110, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.strength", (int)par1.get(par0, Elements.STRENGTH));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.strength.alt0")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.strength.alt1")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.strength.alt2")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.strength.alt3")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.strength.alt4")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.strength.alt5")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.STRENGTH.registry())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent dexterity = new DynamicTextComponent(30, 134, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.dexterity", (int)par1.get(par0, Elements.DEXTERITY));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.dexterity.alt0")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.dexterity.alt1")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.dexterity.alt2")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.dexterity.alt3")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.dexterity.alt4")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.dexterity.alt5")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.dexterity.alt6")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.DEXTERITY.registry())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent intelligence = new DynamicTextComponent(30, 158, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.intelligence", (int)par1.get(par0, Elements.INTELLIGENCE));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.intelligence.alt0")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.intelligence.alt1")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.intelligence.alt2")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.intelligence.alt3")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.intelligence.alt4")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.intelligence.alt5")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.intelligence.alt6")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.INTELLIGENCE.registry())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent luckiness = new DynamicTextComponent(30, 182, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.luckiness", (int)par1.get(par0, Elements.LUCKINESS));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.luckiness.alt0")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.luckiness.alt1")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.luckiness.alt2")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.luckiness.alt3")));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.luckiness.alt4")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.LUCKINESS.registry())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent health = new DynamicTextComponent(130, 50, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.health", text.format((par0.getHealth() + par0.getAbsorptionAmount())), text.format(par0.getMaxHealth()));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.health.alt0")));
		var0.add(new StringTextComponent(" "));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.health.alt1") + text.format(((400D / 9D) * par1.get(par0, Elements.HEALTH_REGEN))) + "/s"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.health.alt2") + text.format(100D * par1.get(par0, Elements.HEALTH_REGEN_AMP)) + "%"));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.HEALTH.registry())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent armor = new DynamicTextComponent(130, 62, (par0, par1) -> {
		String var0 = text.format(par0.getAttribute(Attributes.ARMOR).getValue()) + " (+" + text.format(par0.getAttribute(Attributes.ARMOR).getValue() - par0.getAttribute(Attributes.ARMOR).getBaseValue()) + ")";
		TranslationTextComponent var1 = new TranslationTextComponent(ExAPI.MODID + ".attribute.armor", var0);
		
		return var1.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		String var1 = text.format(par0.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue()) + " (+" + text.format(par0.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue() - par0.getAttribute(Attributes.ARMOR_TOUGHNESS).getBaseValue()) + ")";
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.armor.alt0")));
		var0.add(new StringTextComponent(" "));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.armor.alt1") + var1));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.ARMOR.registry())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent resistance = new DynamicTextComponent(130, 74, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.resistance", text.format(100D * par1.get(par0, Elements.KNOCKBACK_RESISTANCE)) + "%");
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.resistance.alt0") + text.format(100D * par1.get(par0, Elements.DAMAGE_RESISTANCE)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.resistance.alt1") + text.format(100D * par1.get(par0, Elements.FIRE_RESISTANCE)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.resistance.alt2") + text.format(100D * par1.get(par0, Elements.LAVA_RESISTANCE)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.resistance.alt3") + text.format(100D * par1.get(par0, Elements.EXPLOSION_RESISTANCE)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.resistance.alt4") + text.format(100D * par1.get(par0, Elements.FALLING_RESISTANCE)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.resistance.alt5") + text.format(100D * par1.get(par0, Elements.POISON_RESISTANCE)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.resistance.alt6") + text.format(100D * par1.get(par0, Elements.WITHER_RESISTANCE)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.resistance.alt7") + text.format(100D * par1.get(par0, Elements.DROWNING_RESISTANCE)) + "%"));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.KNOCKBACK_RESISTANCE.registry())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent melee = new DynamicTextComponent(130, 98, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.melee", text.format(par1.get(par0, Elements.MELEE_DAMAGE)));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.melee.alt0") + text.format(100D + (100D * par1.get(par0, Elements.MELEE_CRIT_DAMAGE))) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.melee.alt1") + text.format(100D * par1.get(par0, Elements.MELEE_CRIT_CHANCE)) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.melee.alt2") + text.format(par1.get(par0, Elements.ATTACK_SPEED))));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.MELEE_DAMAGE.registry())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent ranged = new DynamicTextComponent(130, 110, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.ranged", text.format(par1.get(par0, Elements.RANGED_DAMAGE)));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.ranged.alt0") + text.format(100D + (100D * par1.get(par0, Elements.RANGED_CRIT_DAMAGE))) + "%"));
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.ranged.alt1") + text.format(100D * par1.get(par0, Elements.RANGED_CRIT_CHANCE)) + "%"));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.RANGED_DAMAGE.registry())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent evasion = new DynamicTextComponent(130, 122, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.evasion", text.format(100D * par1.get(par0, Elements.EVASION_CHANCE)), "%");
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.evasion.alt0")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.EVASION_CHANCE.registry())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent lifesteal = new DynamicTextComponent(130, 146, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.lifesteal", text.format(100D * par1.get(par0, Elements.LIFESTEAL)), "%");
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.lifesteal.alt0")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.LIFESTEAL.registry())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent luck = new DynamicTextComponent(130, 158, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.luck", text.format(par1.get(par0, Elements.LUCK)));
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.luck.alt0")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.LUCK.registry())) {
			var0.add(new StringTextComponent(var.apply(par0, par1)));
		}
		
		return var0;
	});
	private final DynamicTextComponent movement = new DynamicTextComponent(130, 182, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent(ExAPI.MODID + ".attribute.movement", text.format(100D * par1.get(par0, Elements.MOVEMENT_SPEED_AMP)) + "%");
		
		return var0.getString();
	}, (par0, par1) -> {
		List<ITextComponent> var0 = new ArrayList<ITextComponent>();
		
		var0.add(new StringTextComponent(TextFormatting.GRAY + I18n.format(ExAPI.MODID + ".attribute.movement.alt0")));
		
		for(BiFunction<PlayerEntity, IPlayerElements, String> var : ClientReg.getTooltips(Elements.MOVEMENT_SPEED_AMP.registry())) {
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
		
		ExAPI.playerElements(var0).ifPresent(var -> {
			for(Widget var1 : this.buttons) {
				if(var1 instanceof TexturedButton) {
					TexturedButton var2 = (TexturedButton)var1;
					int var3 = var2.getAdditionalData();
					
					if(var3 >= 0 && var3 < this.idToElement.length) {
						float var4 = var.get(var0, Elements.SKILLPOINTS);
						
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
				RegistryEvents.NETWORK.sendToServer(new AddPlayerElement(Pair.of(this.idToElement[var1], 1F), Pair.of(Elements.SKILLPOINTS, -1F)));
			}, null));
		}
	}
}
