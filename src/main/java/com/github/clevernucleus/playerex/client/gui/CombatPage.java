package com.github.clevernucleus.playerex.client.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerAttributes;
import com.github.clevernucleus.playerex.api.attribute.AttributeData;
import com.github.clevernucleus.playerex.api.attribute.AttributeProperties;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.api.client.PageScreen;
import com.github.clevernucleus.playerex.api.util.Maths;
import com.mojang.blaze3d.platform.GlStateManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class CombatPage extends PageScreen {
	private AttributeData data;
	
	public CombatPage(HandledScreen<?> parent, ScreenHandler handler, PlayerInventory inventory) {
		super(parent, handler, inventory, new TranslatableText("gui.playerex.page.combat"), new ItemStack(Items.IRON_SWORD));
	}
	
	private String value(double value) {
		DecimalFormat format = new DecimalFormat("###.##");
		String text = format.format(value);
		
		if(value > 0.0D) return "+" + text;
		return text;
	}
	
	private String display(final IPlayerAttribute attribute) {
		Text text = new TranslatableText(attribute.translationKey());
		boolean percent = attribute.hasProperty(AttributeProperties.PROPERTY_PERCENT);
		float value = (float)Maths.shownValue(attribute, this.data.get(attribute));
		String suffix = percent ? "%" : "";
		
		return text.getString() + ": " + value(value) + suffix;
	}
	
	private String reach() {
		IPlayerAttribute attribute = PlayerAttributes.ATTACK_RANGE.get();
		Text text = new TranslatableText("gui.playerex.text.attack_range");
		boolean percent = attribute.hasProperty(AttributeProperties.PROPERTY_PERCENT);
		float value = (float)Maths.shownValue(attribute, 3.0D + this.data.get(attribute));
		String suffix = percent ? "%" : "";
		
		return text.getString() + ": " + value(value) + suffix;
	}
	
	private String magicAmp() {
		IPlayerAttribute attribute = PlayerAttributes.MAGIC_AMPLIFICATION.get();
		Text text = new TranslatableText("gui.playerex.text.magic_amp");
		boolean percent = attribute.hasProperty(AttributeProperties.PROPERTY_PERCENT);
		float value = (float)Maths.shownValue(attribute, this.data.get(attribute));
		String suffix = percent ? "%" : "";
		
		return text.getString() + ": " + value(value) + suffix;
	}
	
	private String crit(final IPlayerAttribute attribute) {
		String key = attribute.translationKey();
		String type = key.substring(key.length() - 11);
		Text text = new TranslatableText("gui.playerex.text." + type);
		boolean percent = attribute.hasProperty(AttributeProperties.PROPERTY_PERCENT);
		float value = (float)Maths.shownValue(attribute, this.data.get(attribute));
		String suffix = percent ? "%" : "";
		
		return text.getString() + ": " + value(value) + suffix;
	}
	
	private String knockback() {
		IPlayerAttribute attribute = PlayerAttributes.KNOCKBACK_RESISTANCE.get();
		Text text = new TranslatableText("gui.playerex.text.knockback_resistance");
		float value = (float)Maths.shownValue(attribute, this.data.get(attribute));
		
		return text.getString() + ": " + value(value);
	}
	
	private String ranged() {
		IPlayerAttribute attribute = PlayerAttributes.RANGED_DAMAGE.get();
		Text text = new TranslatableText("gui.playerex.text.ranged_damage");
		float value = (float)Maths.shownValue(attribute, this.data.get(attribute));
		
		return text.getString() + ": " + value(value);
	}
	
	private void meleeCritTT(MatrixStack matrices, int mouseX, int mouseY) {
		List<Text> tooltip = new ArrayList<Text>();
		IPlayerAttribute attribute = PlayerAttributes.MELEE_CRIT_DAMAGE.get();
		
		tooltip.add((new TranslatableText("gui.playerex.tooltip.melee_crit_damage")).formatted(Formatting.GRAY));
		
		boolean percent = attribute.hasProperty(AttributeProperties.PROPERTY_PERCENT);
		float value = 100.0F + (float)Maths.shownValue(attribute, this.data.get(attribute));
		String suffix = percent ? "%" : "";
		String out = value(value) + suffix;
		
		tooltip.add((new TranslatableText("gui.playerex.tooltip.melee_crit_damage_1", out)).formatted(Formatting.GRAY));
		
		this.renderTooltip(matrices, tooltip, mouseX, mouseY);
	}
	
	private void rangedCritTT(MatrixStack matrices, int mouseX, int mouseY) {
		List<Text> tooltip = new ArrayList<Text>();
		IPlayerAttribute attribute = PlayerAttributes.MELEE_CRIT_CHANCE.get();
		
		tooltip.add((new TranslatableText("gui.playerex.tooltip.ranged_crit_damage")).formatted(Formatting.GRAY));
		
		boolean percent = attribute.hasProperty(AttributeProperties.PROPERTY_PERCENT);
		float value = 100.0F + (float)Maths.shownValue(attribute, this.data.get(attribute));
		String suffix = percent ? "%" : "";
		String out = value(value) + suffix;
		
		tooltip.add((new TranslatableText("gui.playerex.tooltip.ranged_crit_damage_1", out)).formatted(Formatting.GRAY));
		
		this.renderTooltip(matrices, tooltip, mouseX, mouseY);
	}
	
	private String knockbackTT() {
		IPlayerAttribute attribute = PlayerAttributes.KNOCKBACK_RESISTANCE.get();
		float value = (float)Maths.shownValue(attribute, 10.0D * this.data.get(attribute));
		
		return value(value);
	}
	
	private String reachTT() {
		IPlayerAttribute attribute = PlayerAttributes.ATTACK_RANGE.get();
		boolean percent = attribute.hasProperty(AttributeProperties.PROPERTY_PERCENT);
		float value = (float)Maths.shownValue(attribute, 3.0D + this.data.get(attribute));
		String suffix = percent ? "%" : "";
		
		return value(value) + suffix;
	}
	
	private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
		return mouseX >= (float)x && mouseY >= (float)y && mouseX < (float)(x + width) && mouseY < (float)(y + height);
	}
	
	private void renderTooltips(MatrixStack matrices, int mouseX, int mouseY, float s) {
		if(this.isMouseOver((this.x + 9), (this.y + 37), this.textRenderer.getWidth(this.display(PlayerAttributes.ATTACK_SPEED.get())) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.attack_speed")).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 9), (this.y + 48), this.textRenderer.getWidth(this.display(PlayerAttributes.MELEE_ATTACK_DAMAGE.get())) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.attack_damage")).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 9), (this.y + 59), this.textRenderer.getWidth(this.crit(PlayerAttributes.MELEE_CRIT_DAMAGE.get())) * s, 7, mouseX, mouseY)) {
			this.meleeCritTT(matrices, mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 9), (this.y + 70), this.textRenderer.getWidth(this.crit(PlayerAttributes.MELEE_CRIT_CHANCE.get())) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.melee_crit_chance")).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 9), (this.y + 103), this.textRenderer.getWidth(this.display(PlayerAttributes.ARMOR.get())) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.armor")).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 9), (this.y + 114), this.textRenderer.getWidth(this.display(PlayerAttributes.ARMOR_TOUGHNESS.get())) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.armor_toughness")).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 9), (this.y + 125), this.textRenderer.getWidth(this.knockback()) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.knockback_resistance", this.knockbackTT())).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 9), (this.y + 136), this.textRenderer.getWidth(this.display(PlayerAttributes.EVASION.get())) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.evasion")).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 96), (this.y + 37), this.textRenderer.getWidth(this.ranged()) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.ranged_attack_damage")).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 96), (this.y + 48), this.textRenderer.getWidth(this.crit(PlayerAttributes.RANGED_CRIT_DAMAGE.get())) * s, 7, mouseX, mouseY)) {
			this.rangedCritTT(matrices, mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 96), (this.y + 59), this.textRenderer.getWidth(this.crit(PlayerAttributes.RANGED_CRIT_CHANCE.get())) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.ranged_crit_chance")).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 108), (this.y + 92), this.textRenderer.getWidth(this.display(PlayerAttributes.LIFESTEAL.get())) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.lifesteal")).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 108), (this.y + 103), this.textRenderer.getWidth(this.reach()) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.attack_range", this.reachTT())).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 108), (this.y + 114), this.textRenderer.getWidth(this.magicAmp()) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.magic_amp")).formatted(Formatting.GRAY), mouseX, mouseY);
		}
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		float s = 0.75F;
		
		GlStateManager.pushMatrix();
		GlStateManager.scalef(s, s, 1.0F);
		
		this.textRenderer.draw(matrices, (new TranslatableText("gui.playerex.text.melee")), (this.x + 21) / s, (this.y + 26) / s, 4210752);
		this.textRenderer.draw(matrices, this.display(PlayerAttributes.ATTACK_SPEED.get()), (this.x + 9) / s, (this.y + 37) / s, 4210752);
		this.textRenderer.draw(matrices, this.display(PlayerAttributes.MELEE_ATTACK_DAMAGE.get()), (this.x + 9) / s, (this.y + 48) / s, 4210752);
		this.textRenderer.draw(matrices, this.crit(PlayerAttributes.MELEE_CRIT_DAMAGE.get()), (this.x + 9) / s, (this.y + 59) / s, 4210752);
		this.textRenderer.draw(matrices, this.crit(PlayerAttributes.MELEE_CRIT_CHANCE.get()), (this.x + 9) / s, (this.y + 70) / s, 4210752);
		this.textRenderer.draw(matrices, (new TranslatableText("gui.playerex.text.defence")), (this.x + 21) / s, (this.y + 92) / s, 4210752);
		this.textRenderer.draw(matrices, this.display(PlayerAttributes.ARMOR.get()), (this.x + 9) / s, (this.y + 103) / s, 4210752);
		this.textRenderer.draw(matrices, this.display(PlayerAttributes.ARMOR_TOUGHNESS.get()), (this.x + 9) / s, (this.y + 114) / s, 4210752);
		this.textRenderer.draw(matrices, this.knockback(), (this.x + 9) / s, (this.y + 125) / s, 4210752);
		this.textRenderer.draw(matrices, this.display(PlayerAttributes.EVASION.get()), (this.x + 9) / s, (this.y + 136) / s, 4210752);
		this.textRenderer.draw(matrices, (new TranslatableText("gui.playerex.text.ranged")), (this.x + 108) / s, (this.y + 26) / s, 4210752);
		this.textRenderer.draw(matrices, this.ranged(), (this.x + 96) / s, (this.y + 37) / s, 4210752);
		this.textRenderer.draw(matrices, this.crit(PlayerAttributes.RANGED_CRIT_DAMAGE.get()), (this.x + 96) / s, (this.y + 48) / s, 4210752);
		this.textRenderer.draw(matrices, this.crit(PlayerAttributes.RANGED_CRIT_CHANCE.get()), (this.x + 96) / s, (this.y + 59) / s, 4210752);
		this.textRenderer.draw(matrices, this.display(PlayerAttributes.LIFESTEAL.get()), (this.x + 108) / s, (this.y + 92) / s, 4210752);
		this.textRenderer.draw(matrices, this.reach(), (this.x + 108) / s, (this.y + 103) / s, 4210752);
		this.textRenderer.draw(matrices, this.magicAmp(), (this.x + 108) / s, (this.y + 114) / s, 4210752);
		
		GlStateManager.popMatrix();
		
		this.renderTooltips(matrices, mouseX, mouseY, s);
	}
	
	@Override
	public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		this.client.getTextureManager().bindTexture(AttributesScreen.EX_GUI);
		
		this.drawTexture(matrices, this.x + 9, this.y + 24, 244, 9, 9, 9);
		this.drawTexture(matrices, this.x + 9, this.y + 90, 226, 18, 9, 9);
		this.drawTexture(matrices, this.x + 96, this.y + 24, 235, 18, 9, 9);
		this.drawTexture(matrices, this.x + 96, this.y + 90, 244, 18, 9, 9);
		this.drawTexture(matrices, this.x + 96, this.y + 101, 226, 27, 9, 9);
		this.drawTexture(matrices, this.x + 96, this.y + 112, 235, 27, 9, 9);
	}
	
	@Override
	protected void init() {
		super.init();
		this.data = ExAPI.DATA.get(this.client.player);
	}
}
