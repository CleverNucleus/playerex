package com.github.clevernucleus.playerex.client.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerAttributes;
import com.github.clevernucleus.playerex.api.attribute.AttributeData;
import com.github.clevernucleus.playerex.api.attribute.AttributeProperties;
import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction;
import com.github.clevernucleus.playerex.api.attribute.IAttributeFunction.Type;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.api.client.PageScreen;
import com.github.clevernucleus.playerex.api.util.Maths;
import com.github.clevernucleus.playerex.client.gui.widget.ScreenButtonWidget;
import com.github.clevernucleus.playerex.client.network.ClientNetworkHandler;
import com.github.clevernucleus.playerex.handler.NetworkHandler.PacketType;
import com.mojang.blaze3d.platform.GlStateManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class AttributesPage extends PageScreen {
	private AttributeData data;
	
	public AttributesPage(HandledScreen<?> parent, ScreenHandler handler, PlayerInventory inventory) {
		super(parent, handler, inventory, new TranslatableText("gui.playerex.page.attributes"), new ItemStack(Items.BOOK));
	}
	
	private IPlayerAttribute fromKey(final Identifier keyIn) {
		return ExAPI.REGISTRY.get().getAttribute(keyIn);
	}
	
	private Pair<IPlayerAttribute, Double> pair(final IPlayerAttribute key, final double value) {
		return new Pair<IPlayerAttribute, Double>(key, value);
	}
	
	private boolean refunds() {
		return this.data.refundPoints() > 0;
	}
	
	private void buttonPressed(ButtonWidget button) {
		ScreenButtonWidget btn = (ScreenButtonWidget)button;
		
		double value = this.refunds() ? -1.0D : 1.0D;
		
		IPlayerAttribute attribute = this.fromKey(btn.attribute());
		ClientNetworkHandler.modifyAttributes(this.refunds() ? PacketType.REFUND : PacketType.SKILL, pair(attribute, value), pair(PlayerAttributes.SKILLPOINTS.get(), (-1) * value));
		
		if(!ExAPI.CONFIG.get().playSkillUpSound()) return;
		this.client.player.playSound(PlayerEx.SP_SPEND, SoundCategory.NEUTRAL, 0.5F, 1.5F);
	}
	
	private String value(IPlayerAttribute attribute, double value) {
		DecimalFormat format = new DecimalFormat("###.##");
		String text = format.format(value);
		String suffix = attribute.hasProperty(AttributeProperties.PROPERTY_PERCENT) ? "%" : "";
		String result = text + suffix;
		
		return (value > 0) ? "+" + result : result;
	}
	
	private String display(final IPlayerAttribute attribute) {
		Text text = new TranslatableText(attribute.translationKey());
		float value = (float)Maths.shownValue(attribute, this.data.get(attribute));
		
		return text.getString() + ": " + value(attribute, value);
	}
	
	private String reach() {
		IPlayerAttribute attribute = PlayerAttributes.REACH_DISTANCE.get();
		Text text = new TranslatableText(attribute.translationKey());
		float value = (float)Maths.shownValue(attribute, 4.5D + this.data.get(attribute));
		
		return text.getString() + ": " + value(attribute, value);
	}
	
	private String level() {
		IPlayerAttribute attribute = PlayerAttributes.LEVEL.get();
		Text text = new TranslatableText(attribute.translationKey());
		
		return text.getString() + ": " + (int)this.data.get(attribute);
	}
	
	private String health() {
		IPlayerAttribute attribute = PlayerAttributes.MAX_HEALTH.get();
		Text text = new TranslatableText("gui.playerex.text.health");
		DecimalFormat format = new DecimalFormat("###.##");
		String max = format.format((float)this.data.get(attribute));
		String current = format.format(this.client.player.getHealth());
		
		return text.getString() + ": " + current + "/" + max;
	}
	
	private String healthRegen() {
		IPlayerAttribute attribute = PlayerAttributes.HEALTH_REGEN.get();
		Text text = new TranslatableText("gui.playerex.text.health_regen");
		String value = value(attribute, Maths.shownValue(attribute, this.data.get(attribute)));
		
		return text.getString() + ": " + value;
	}
	
	private String healAmp() {
		IPlayerAttribute attribute = PlayerAttributes.HEAL_AMPLIFICATION.get();
		Text text = new TranslatableText("gui.playerex.text.heal_amp");
		String value = value(attribute, Maths.shownValue(attribute, this.data.get(attribute)));
		
		return text.getString() + ": " + value;
	}
	
	private String resistance(final IPlayerAttribute attribute) {
		String key = attribute.translationKey();
		String name = key.substring(0, key.length() - 11).substring(23);
		Text text = new TranslatableText("gui.playerex.text" + name);
		String value = value(attribute, Maths.shownValue(attribute, this.data.get(attribute)));
		
		return text.getString() + ": " + value;
	}
	
	private void levelBtnTT(ButtonWidget button, MatrixStack matrices, int mouseX, int mouseY) {
		DecimalFormat format = new DecimalFormat("##.##");
		int requiredXp = Maths.requiredXp(this.client.player);
		int currentXp = this.client.player.experienceLevel;
		float t = 100F * (float)currentXp / (float)requiredXp;
		float progress = MathHelper.clamp(t, 0.0F, 100.0F);
		
		this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.level_up", format.format(progress) + "%")).formatted(Formatting.GRAY), mouseX, mouseY);
	}
	
	private void attributeBtnTT(ButtonWidget button, MatrixStack matrices, int mouseX, int mouseY) {
		ScreenButtonWidget btn = (ScreenButtonWidget)button;
		
		TranslatableText skill = new TranslatableText("gui.playerex.tooltip.skill");
		TranslatableText refund = new TranslatableText("gui.playerex.tooltip.refund");
		IPlayerAttribute attribute = this.fromKey(btn.attribute());
		TranslatableText attributeText = new TranslatableText(attribute.translationKey());
		Text tooltip = this.refunds() ? refund.append(attributeText) : skill.append(attributeText);
		
		this.renderTooltip(matrices, ((MutableText)tooltip).formatted(Formatting.GRAY), mouseX, mouseY);
	}
	
	private void bonusAttributesTT(List<Text> tooltip, final IPlayerAttribute attribute) {
		for(IAttributeFunction function : attribute.functions()) {
			Identifier attributeKey = function.attributeKey();
			IPlayerAttribute subAttribute = ExAPI.REGISTRY.get().getAttribute(attributeKey);
			String value = this.value(subAttribute, Maths.shownValue(subAttribute, function.multiplier()));
			Text attributeText = new TranslatableText(subAttribute.translationKey());
			MutableText text = new LiteralText(value + " " + attributeText.getString()).formatted(Formatting.GRAY);
			Text dim = new TranslatableText("gui.playerex.tooltip.diminishing");
			
			if(function.type().equals(Type.DIMINISHING)) {
				text.append(dim);
			}
			
			tooltip.add(text);
		}
	}
	
	private void levelTT(MatrixStack matrices, int mouseX, int mouseY) {
		List<Text> tooltip = new ArrayList<Text>();
		
		tooltip.add((new TranslatableText("gui.playerex.tooltip.level")).formatted(Formatting.GRAY));
		tooltip.add(LiteralText.EMPTY);
		
		this.bonusAttributesTT(tooltip, PlayerAttributes.LEVEL.get());
		this.renderTooltip(matrices, tooltip, mouseX, mouseY);
	}
	
	private void skptTT(MatrixStack matrices, int mouseX, int mouseY) {
		this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.skillpoints")).formatted(Formatting.GRAY), mouseX, mouseY);
	}
	
	private void attributeTT(final IPlayerAttribute attribute, MatrixStack matrices, int mouseX, int mouseY) {
		List<Text> tooltip = new ArrayList<Text>();
		
		tooltip.add((new TranslatableText(attribute.translationKey())).formatted(Formatting.GRAY));
		
		this.bonusAttributesTT(tooltip, attribute);
		this.renderTooltip(matrices, tooltip, mouseX, mouseY);
	}
	
	private void resistanceTT(IPlayerAttribute attribute, MatrixStack matrices, int mouseX, int mouseY) {
		String key = attribute.translationKey().substring(24);
		String value = value(attribute, 10.0D * Maths.shownValue(attribute, this.data.get(attribute)));
		this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip." + key, value)).formatted(Formatting.GRAY), mouseX, mouseY);
	}
	
	private boolean isMouseOver(float x, float y, float width, float height, int mouseX, int mouseY) {
		return mouseX >= (float)x && mouseY >= (float)y && mouseX < (float)(x + width) && mouseY < (float)(y + height);
	}
	
	private void renderTooltips(MatrixStack matrices, int mouseX, int mouseY, float s) {
		if(this.isMouseOver((this.x + 21), (this.y + 26), this.textRenderer.getWidth(this.level()) * s, 7, mouseX, mouseY)) {
			this.levelTT(matrices, mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 21), (this.y + 37), this.textRenderer.getWidth(this.display(PlayerAttributes.SKILLPOINTS.get())) * s, 7, mouseX, mouseY)) {
			this.skptTT(matrices, mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 21), (this.y + 59), this.textRenderer.getWidth(this.display(PlayerAttributes.CONSTITUTION.get())) * s, 7, mouseX, mouseY)) {
			this.attributeTT(PlayerAttributes.CONSTITUTION.get(), matrices, mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 21), (this.y + 70), this.textRenderer.getWidth(this.display(PlayerAttributes.STRENGTH.get())) * s, 7, mouseX, mouseY)) {
			this.attributeTT(PlayerAttributes.STRENGTH.get(), matrices, mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 21), (this.y + 81), this.textRenderer.getWidth(this.display(PlayerAttributes.DEXTERITY.get())) * s, 7, mouseX, mouseY)) {
			this.attributeTT(PlayerAttributes.DEXTERITY.get(), matrices, mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 21), (this.y + 92), this.textRenderer.getWidth(this.display(PlayerAttributes.INTELLIGENCE.get())) * s, 7, mouseX, mouseY)) {
			this.attributeTT(PlayerAttributes.INTELLIGENCE.get(), matrices, mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 21), (this.y + 103), this.textRenderer.getWidth(this.display(PlayerAttributes.LUCKINESS.get())) * s, 7, mouseX, mouseY)) {
			this.attributeTT(PlayerAttributes.LUCKINESS.get(), matrices, mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 21), (this.y + 125), this.textRenderer.getWidth(this.display(PlayerAttributes.MOVEMENT_SPEED.get())) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.movement_speed", this.display(PlayerAttributes.MOVEMENT_SPEED.get()))).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 21), (this.y + 136), this.textRenderer.getWidth(this.reach()) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.reach_distance", this.reach())).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 96), (this.y + 37), this.textRenderer.getWidth(this.health()) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.health")).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 96), (this.y + 48), this.textRenderer.getWidth(this.healthRegen()) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.health_regen")).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 96), (this.y + 59), this.textRenderer.getWidth(this.healAmp()) * s, 7, mouseX, mouseY)) {
			this.renderTooltip(matrices, (new TranslatableText("gui.playerex.tooltip.heal_amp")).formatted(Formatting.GRAY), mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 96), (this.y + 92), this.textRenderer.getWidth(this.resistance(PlayerAttributes.FIRE_RESISTANCE.get())) * s, 7, mouseX, mouseY)) {
			this.resistanceTT(PlayerAttributes.FIRE_RESISTANCE.get(), matrices, mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 96), (this.y + 103), this.textRenderer.getWidth(this.resistance(PlayerAttributes.FALLING_RESISTANCE.get())) * s, 7, mouseX, mouseY)) {
			this.resistanceTT(PlayerAttributes.FALLING_RESISTANCE.get(), matrices, mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 96), (this.y + 114), this.textRenderer.getWidth(this.resistance(PlayerAttributes.DROWNING_RESISTANCE.get())) * s, 7, mouseX, mouseY)) {
			this.resistanceTT(PlayerAttributes.DROWNING_RESISTANCE.get(), matrices, mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 96), (this.y + 125), this.textRenderer.getWidth(this.resistance(PlayerAttributes.WITHER_RESISTANCE.get())) * s, 7, mouseX, mouseY)) {
			this.resistanceTT(PlayerAttributes.WITHER_RESISTANCE.get(), matrices, mouseX, mouseY);
		}
		
		if(this.isMouseOver((this.x + 96), (this.y + 136), this.textRenderer.getWidth(this.resistance(PlayerAttributes.MAGIC_RESISTANCE.get())) * s, 7, mouseX, mouseY)) {
			this.resistanceTT(PlayerAttributes.MAGIC_RESISTANCE.get(), matrices, mouseX, mouseY);
		}
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		float s = 0.75F;
		
		GlStateManager.pushMatrix();
		GlStateManager.scalef(s, s, 1.0F);
		this.textRenderer.draw(matrices, this.level(), (this.x + 21) / s, (this.y + 26) / s, 4210752);
		this.textRenderer.draw(matrices, this.display(PlayerAttributes.SKILLPOINTS.get()), (this.x + 21) / s, (this.y + 37) / s, 4210752);
		this.textRenderer.draw(matrices, this.display(PlayerAttributes.CONSTITUTION.get()), (this.x + 21) / s, (this.y + 59) / s, 4210752);
		this.textRenderer.draw(matrices, this.display(PlayerAttributes.STRENGTH.get()), (this.x + 21) / s, (this.y + 70) / s, 4210752);
		this.textRenderer.draw(matrices, this.display(PlayerAttributes.DEXTERITY.get()), (this.x + 21) / s, (this.y + 81) / s, 4210752);
		this.textRenderer.draw(matrices, this.display(PlayerAttributes.INTELLIGENCE.get()), (this.x + 21) / s, (this.y + 92) / s, 4210752);
		this.textRenderer.draw(matrices, this.display(PlayerAttributes.LUCKINESS.get()), (this.x + 21) / s, (this.y + 103) / s, 4210752);
		this.textRenderer.draw(matrices, this.display(PlayerAttributes.MOVEMENT_SPEED.get()), (this.x + 21) / s, (this.y + 125) / s, 4210752);
		this.textRenderer.draw(matrices, this.reach(), (this.x + 21) / s, (this.y + 136) / s, 4210752);
		this.textRenderer.draw(matrices, (new TranslatableText("gui.playerex.text.vitality")), (this.x + 108) / s, (this.y + 26) / s, 4210752);
		this.textRenderer.draw(matrices, this.health(), (this.x + 96) / s, (this.y + 37) / s, 4210752);
		this.textRenderer.draw(matrices, this.healthRegen(), (this.x + 96) / s, (this.y + 48) / s, 4210752);
		this.textRenderer.draw(matrices, this.healAmp(), (this.x + 96) / s, (this.y + 59) / s, 4210752);
		this.textRenderer.draw(matrices, (new TranslatableText("gui.playerex.text.resistances")), (this.x + 108) / s, (this.y + 81) / s, 4210752);
		this.textRenderer.draw(matrices, this.resistance(PlayerAttributes.FIRE_RESISTANCE.get()), (this.x + 96) / s, (this.y + 92) / s, 4210752);
		this.textRenderer.draw(matrices, this.resistance(PlayerAttributes.FALLING_RESISTANCE.get()), (this.x + 96) / s, (this.y + 103) / s, 4210752);
		this.textRenderer.draw(matrices, this.resistance(PlayerAttributes.DROWNING_RESISTANCE.get()), (this.x + 96) / s, (this.y + 114) / s, 4210752);
		this.textRenderer.draw(matrices, this.resistance(PlayerAttributes.WITHER_RESISTANCE.get()), (this.x + 96) / s, (this.y + 125) / s, 4210752);
		this.textRenderer.draw(matrices, this.resistance(PlayerAttributes.MAGIC_RESISTANCE.get()), (this.x + 96) / s, (this.y + 136) / s, 4210752);
		GlStateManager.popMatrix();
		
		this.renderTooltips(matrices, mouseX, mouseY, s);
	}
	
	@Override
	public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		this.client.getTextureManager().bindTexture(AttributesScreen.EX_GUI);
		
		this.drawTexture(matrices, this.x + 9, this.y + 35, 226, 0, 9, 9);
		this.drawTexture(matrices, this.x + 9, this.y + 123, 235, 0, 9, 9);
		this.drawTexture(matrices, this.x + 9, this.y + 134, 244, 0, 9, 9);
		this.drawTexture(matrices, this.x + 96, this.y + 24, 226, 9, 9, 9);
		this.drawTexture(matrices, this.x + 96, this.y + 79, 235, 9, 9, 9);
		
		
		this.buttons.forEach(btn -> {
			ScreenButtonWidget button = (ScreenButtonWidget)btn;
			Identifier key = button.attribute();
			Identifier level = new Identifier("playerex:level");
			
			if(key.equals(level)) {
				button.active = (this.data.get(PlayerAttributes.LEVEL.get()) < PlayerAttributes.LEVEL.get().maxValue()) && (this.client.player.experienceLevel >= Maths.requiredXp(this.client.player));
			} else {
				IPlayerAttribute attribute = this.fromKey(key);
				
				if(this.refunds()) {
					button.active = this.data.get(attribute) >= 1.0D;
				} else {
					button.active = (this.data.get(attribute) < attribute.maxValue()) && (this.data.get(PlayerAttributes.SKILLPOINTS.get()) >= 1.0D);
				}
				
				button.alt = this.refunds();
			}
		});
	}
	
	@Override
	protected void init() {
		super.init();
		this.data = ExAPI.DATA.get(this.client.player);
		
		this.addButton(new ScreenButtonWidget(this.parent, 8, 23, 204, 0, 11, 10, new Identifier("playerex:level"), btn -> ClientNetworkHandler.modifyAttributes(PacketType.LEVEL, pair(PlayerAttributes.LEVEL.get(), 1.0D)), this::levelBtnTT));
		this.addButton(new ScreenButtonWidget(this.parent, 8, 56, 204, 0, 11, 10, new Identifier("playerex:constitution"), this::buttonPressed, this::attributeBtnTT));
		this.addButton(new ScreenButtonWidget(this.parent, 8, 67, 204, 0, 11, 10, new Identifier("playerex:strength"), this::buttonPressed, this::attributeBtnTT));
		this.addButton(new ScreenButtonWidget(this.parent, 8, 78, 204, 0, 11, 10, new Identifier("playerex:dexterity"), this::buttonPressed, this::attributeBtnTT));
		this.addButton(new ScreenButtonWidget(this.parent, 8, 89, 204, 0, 11, 10, new Identifier("playerex:intelligence"), this::buttonPressed, this::attributeBtnTT));
		this.addButton(new ScreenButtonWidget(this.parent, 8, 100, 204, 0, 11, 10, new Identifier("playerex:luckiness"), this::buttonPressed, this::attributeBtnTT));
	}
}
