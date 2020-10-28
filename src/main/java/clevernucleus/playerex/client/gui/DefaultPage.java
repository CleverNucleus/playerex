package clevernucleus.playerex.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import clevernucleus.playerex.api.ElementRegistry;
import clevernucleus.playerex.api.Util;
import clevernucleus.playerex.api.client.gui.Page;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class DefaultPage extends Page {
	private final List<DynamicTextComponent> dynamicTextComponents = new ArrayList<DynamicTextComponent>();
	
	private final DynamicTextComponent level = new DynamicTextComponent(20, 40, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.level", (int)ElementRegistry.LEVEL.get(par0, par1), (int)(100 * Util.expCoeff((float)ElementRegistry.LEVEL.get(par0, par1), (float)ElementRegistry.EXPERIENCE.get(par0, par1))), "%");
		
		return var0.getString();
	}, (par0, par1) -> {
		return Arrays.asList(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.level.alt")));
	});
	private final DynamicTextComponent skillPoints = new DynamicTextComponent(20, 52, (par0, par1) -> {
		TranslationTextComponent var0 = new TranslationTextComponent("playerex.attribute.skillpoints", (int)ElementRegistry.SKILLPOINTS.get(par0, par1));
		
		return var0.getString();
	}, (par0, par1) -> {
		return Arrays.asList(new StringTextComponent(TextFormatting.GRAY + I18n.format("playerex.attribute.skillpoints.alt")));
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
		
		return var0;
	});
	private final DynamicTextComponent strength = new DynamicTextComponent(30, 90, (par0, par1) -> {
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
		
		return var0;
	});
	
	public DefaultPage(ITextComponent par0) {
		super(par0);
		
		this.dynamicTextComponents.add(this.level);
		this.dynamicTextComponents.add(this.skillPoints);
		this.dynamicTextComponents.add(this.constitution);
		this.dynamicTextComponents.add(this.strength);
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
}
