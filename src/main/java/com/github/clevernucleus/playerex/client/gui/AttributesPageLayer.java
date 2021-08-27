package com.github.clevernucleus.playerex.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.github.clevernucleus.dataattributes.api.attribute.IAttribute;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.ModifierData;
import com.github.clevernucleus.playerex.api.client.ClientUtil;
import com.github.clevernucleus.playerex.api.client.PageLayer;
import com.github.clevernucleus.playerex.api.client.RenderComponent;
import com.github.clevernucleus.playerex.client.PlayerExClient;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class AttributesPageLayer extends PageLayer {
	private ModifierData modifierData;
	private static AttributesPageLayer instance;
	private static final float SCALE = 0.75F;
	private static final List<RenderComponent> RENDER_COMPONENTS = new ArrayList<RenderComponent>();
	
	static {
		register(() -> ExAPI.LEVEL.get() != null, () -> {
			EntityAttribute attribute = ExAPI.LEVEL.get();
			Text text = new TranslatableText(attribute.getTranslationKey());
			AttributeContainer container = instance.client.player.getAttributes();
			int value = container.hasAttribute(attribute) ? (int)container.getValue(attribute) : 0;
			
			return new LiteralText(text.getString() + ": " + value);
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.level[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.level[1]")).formatted(Formatting.GRAY));
			tooltip.add(LiteralText.EMPTY);
			
			EntityAttribute attribute = ExAPI.LEVEL.get();
			ClientUtil.appendFunctionsToTooltip(tooltip, attribute);
			
			return tooltip;
		}, 21, 26, SCALE);
		register(() -> ExAPI.SKILL_POINTS.get() != null, () -> {
			EntityAttribute attribute = ExAPI.SKILL_POINTS.get();
			Text text = new TranslatableText(attribute.getTranslationKey());
			AttributeContainer container = instance.client.player.getAttributes();
			int value = container.hasAttribute(attribute) ? (int)container.getValue(attribute) : 0;
			
			return new LiteralText(text.getString() + ": " + value);
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.skill_points[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.skill_points[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 21, 37, SCALE);
		register(() -> ExAPI.CONSTITUTION.get() != null, () -> {
			EntityAttribute attribute = ExAPI.CONSTITUTION.get();
			Text text = new TranslatableText(attribute.getTranslationKey());
			AttributeContainer container = instance.client.player.getAttributes();
			MutableText result = new LiteralText(text.getString() + ": ");
			
			if(!container.hasAttribute(attribute)) return result.append("0");
			int value = (int)container.getValue(attribute);
			
			return result.append(String.valueOf(value));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.CONSTITUTION.get();
			tooltip.add((new TranslatableText(attribute.getTranslationKey())).formatted(Formatting.GRAY));
			tooltip.add(LiteralText.EMPTY);
			
			ClientUtil.appendFunctionsToTooltip(tooltip, attribute);
			
			return tooltip;
		}, 21, 59, SCALE);
		register(() -> ExAPI.STRENGTH.get() != null, () -> {
			EntityAttribute attribute = ExAPI.STRENGTH.get();
			Text text = new TranslatableText(attribute.getTranslationKey());
			AttributeContainer container = instance.client.player.getAttributes();
			MutableText result = new LiteralText(text.getString() + ": ");
			
			if(!container.hasAttribute(attribute)) return result.append("0");
			int value = (int)container.getValue(attribute);
			
			return result.append(String.valueOf(value));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.STRENGTH.get();
			tooltip.add((new TranslatableText(attribute.getTranslationKey())).formatted(Formatting.GRAY));
			tooltip.add(LiteralText.EMPTY);
			
			ClientUtil.appendFunctionsToTooltip(tooltip, attribute);
			
			return tooltip;
		}, 21, 70, SCALE);
		register(() -> ExAPI.DEXTERITY.get() != null, () -> {
			EntityAttribute attribute = ExAPI.DEXTERITY.get();
			Text text = new TranslatableText(attribute.getTranslationKey());
			AttributeContainer container = instance.client.player.getAttributes();
			MutableText result = new LiteralText(text.getString() + ": ");
			
			if(!container.hasAttribute(attribute)) return result.append("0");
			int value = (int)container.getValue(attribute);
			
			return result.append(String.valueOf(value));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.DEXTERITY.get();
			tooltip.add((new TranslatableText(attribute.getTranslationKey())).formatted(Formatting.GRAY));
			tooltip.add(LiteralText.EMPTY);
			
			ClientUtil.appendFunctionsToTooltip(tooltip, attribute);
			
			return tooltip;
		}, 21, 81, SCALE);
		register(() -> ExAPI.INTELLIGENCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.INTELLIGENCE.get();
			Text text = new TranslatableText(attribute.getTranslationKey());
			AttributeContainer container = instance.client.player.getAttributes();
			MutableText result = new LiteralText(text.getString() + ": ");
			
			if(!container.hasAttribute(attribute)) return result.append("0");
			int value = (int)container.getValue(attribute);
			
			return result.append(String.valueOf(value));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.INTELLIGENCE.get();
			tooltip.add((new TranslatableText(attribute.getTranslationKey())).formatted(Formatting.GRAY));
			tooltip.add(LiteralText.EMPTY);
			
			ClientUtil.appendFunctionsToTooltip(tooltip, attribute);
			
			return tooltip;
		}, 21, 92, SCALE);
		register(() -> ExAPI.LUCKINESS.get() != null, () -> {
			EntityAttribute attribute = ExAPI.LUCKINESS.get();
			Text text = new TranslatableText(attribute.getTranslationKey());
			AttributeContainer container = instance.client.player.getAttributes();
			MutableText result = new LiteralText(text.getString() + ": ");
			
			if(!container.hasAttribute(attribute)) return result.append("0");
			int value = (int)container.getValue(attribute);
			
			return result.append(String.valueOf(value));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.LUCKINESS.get();
			tooltip.add((new TranslatableText(attribute.getTranslationKey())).formatted(Formatting.GRAY));
			tooltip.add(LiteralText.EMPTY);
			
			ClientUtil.appendFunctionsToTooltip(tooltip, attribute);
			
			return tooltip;
		}, 21, 103, SCALE);
		register(() -> true, () -> {
			EntityAttribute attribute = EntityAttributes.GENERIC_MOVEMENT_SPEED;
			MutableText text = new TranslatableText(attribute.getTranslationKey());
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.getValue(attribute);
			
			return text.append(": " + ClientUtil.FORMATTING.format(value));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = EntityAttributes.GENERIC_MOVEMENT_SPEED;
			AttributeContainer container = instance.client.player.getAttributes();
			String value = ClientUtil.FORMATTING.format(20.0D * container.getValue(attribute));
			
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.speed", value)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 21, 125, SCALE);
		register(() -> true, () -> {
			float current = instance.client.player.getHealth();
			float maximum = instance.client.player.getMaxHealth();
			String currentVal = ClientUtil.FORMATTING.format(current);
			String maximumVal = ClientUtil.FORMATTING.format(maximum);
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.health", currentVal, maximumVal);
			
			return text;
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.health")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 96, 37, SCALE);
		register(() -> ExAPI.HEALTH_REGENERATION.get() != null, () -> {
			EntityAttribute attribute = ExAPI.HEALTH_REGENERATION.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.health_regeneration");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.health_regeneration[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.health_regeneration[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 96, 48, SCALE);
		register(() -> ExAPI.HEAL_AMPLIFICATION.get() != null, () -> {
			EntityAttribute attribute = ExAPI.HEAL_AMPLIFICATION.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.heal_amplification");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING.format(displayValue) + "%");
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.heal_amplification[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.heal_amplification[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 96, 59, SCALE);
		register(() -> ExAPI.FIRE_RESISTANCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.FIRE_RESISTANCE.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.fire");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.FIRE_RESISTANCE.get();
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			String formattedValue = ClientUtil.FORMATTING.format(100.0D * value);
			
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.fire", formattedValue)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 96, 92, SCALE);
		register(() -> ExAPI.FREEZE_RESISTANCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.FREEZE_RESISTANCE.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.freeze");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.FREEZE_RESISTANCE.get();
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			String formattedValue = ClientUtil.FORMATTING.format(100.0D * value);
			
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.freeze", formattedValue)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 96, 103, SCALE);
		register(() -> ExAPI.FALLING_RESISTANCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.FALLING_RESISTANCE.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.falling");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.FALLING_RESISTANCE.get();
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			String formattedValue = ClientUtil.FORMATTING.format(100.0D * value);
			
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.falling", formattedValue)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 96, 114, SCALE);
		register(() -> ExAPI.DROWNING_RESISTANCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.DROWNING_RESISTANCE.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.drowning");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.DROWNING_RESISTANCE.get();
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			String formattedValue = ClientUtil.FORMATTING.format(100.0D * value);
			
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.drowning", formattedValue)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 96, 125, SCALE);
		register(() -> ExAPI.WITHER_RESISTANCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.WITHER_RESISTANCE.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.wither");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.WITHER_RESISTANCE.get();
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			String formattedValue = ClientUtil.FORMATTING.format(100.0D * value);
			
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.wither", formattedValue)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 96, 136, SCALE);
		register(() -> ExAPI.MAGIC_RESISTANCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.MAGIC_RESISTANCE.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.magic");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.MAGIC_RESISTANCE.get();
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			String formattedValue = ClientUtil.FORMATTING.format(100.0D * value);
			
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.magic", formattedValue)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 96, 147, SCALE);
	}
	
	public AttributesPageLayer(ScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		
		instance = this;
	}
	
	private static RenderComponent register(final Supplier<Boolean> shouldRender, final Supplier<Text> text, final Supplier<List<Text>> tooltip, final int dx, final int dy, final float scale) {
		RenderComponent renderComponent = new RenderComponent(shouldRender, text, tooltip, dx, dy, scale);
		RENDER_COMPONENTS.add(renderComponent);
		
		return renderComponent;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		matrices.push();
		matrices.scale(SCALE, SCALE, SCALE);
		
		RENDER_COMPONENTS.forEach(c -> c.renderText(matrices, this.textRenderer, this.x, this.y));
		this.textRenderer.draw(matrices, (new TranslatableText("gui.playerex.page.attributes.text.vitality")), (this.x + 108) / SCALE, (this.y + 26) / SCALE, 4210752);
		this.textRenderer.draw(matrices, (new TranslatableText("gui.playerex.page.attributes.text.resistances")), (this.x + 108) / SCALE, (this.y + 81) / SCALE, 4210752);
		
		matrices.pop();
		
		RENDER_COMPONENTS.forEach(c -> c.renderTooltip(this::renderTooltip, matrices, this.textRenderer, this.x, this.y, mouseX, mouseY));
	}
	
	@Override
	public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, PlayerExClient.GUI);
		
		this.drawTexture(matrices, this.x + 9, this.y + 35, 226, 0, 9, 9);
		this.drawTexture(matrices, this.x + 9, this.y + 123, 235, 0, 9, 9);
		this.drawTexture(matrices, this.x + 96, this.y + 24, 226, 9, 9, 9);
		this.drawTexture(matrices, this.x + 96, this.y + 79, 235, 9, 9, 9);
	}
	
	@Override
	protected void init() {
		super.init();
		this.modifierData = ExAPI.DATA.get(this.client.player);
	}
}
