package clevernucleus.playerex.client.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;

import clevernucleus.playerex.api.ElementRegistry;
import clevernucleus.playerex.api.Util;
import clevernucleus.playerex.api.client.ClientReg;
import clevernucleus.playerex.api.client.gui.Page;
import clevernucleus.playerex.api.element.IElement;
import clevernucleus.playerex.api.element.IPlayerElements;
import clevernucleus.playerex.common.init.Registry;
import clevernucleus.playerex.common.network.AddPlayerElement;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
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
	
	private final DynamicTextComponent level = new DynamicTextComponent(20, 40, (par0, par1) -> {
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
	private final DynamicTextComponent skillPoints = new DynamicTextComponent(20, 52, (par0, par1) -> {
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
	private final DynamicTextComponent constitution = new DynamicTextComponent(30, 76, (par0, par1) -> {
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
	private final DynamicTextComponent strength = new DynamicTextComponent(30, 100, (par0, par1) -> {
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
	private final DynamicTextComponent dexterity = new DynamicTextComponent(30, 124, (par0, par1) -> {
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
	private final DynamicTextComponent intelligence = new DynamicTextComponent(30, 148, (par0, par1) -> {
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
	private final DynamicTextComponent luckiness = new DynamicTextComponent(30, 172, (par0, par1) -> {
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
	private final DynamicTextComponent health = new DynamicTextComponent(140, 40, (par0, par1) -> {
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
			this.addButton(new TexturedButton(par1, 8, 50 + (17 * var), 11, 10, 204, 0, var, (var0, var1) -> {
				Registry.NETWORK.sendToServer(new AddPlayerElement(Pair.of(this.idToElement[var1], 1D), Pair.of(ElementRegistry.SKILLPOINTS, -1D)));
			}, null));
		}
	}
}
