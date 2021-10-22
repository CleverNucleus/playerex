package com.github.clevernucleus.playerex.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.clevernucleus.dataattributes.api.API;
import com.github.clevernucleus.dataattributes.api.attribute.IAttribute;
import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.ModifierData;
import com.github.clevernucleus.playerex.api.client.ClientUtil;
import com.github.clevernucleus.playerex.api.client.RenderComponent;
import com.github.clevernucleus.playerex.api.client.page.PageLayer;
import com.github.clevernucleus.playerex.client.NetworkHandlerClient;
import com.github.clevernucleus.playerex.client.PlayerExClient;
import com.github.clevernucleus.playerex.client.gui.widget.ScreenButtonWidget;
import com.github.clevernucleus.playerex.handler.NetworkHandler.PacketType;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

@Environment(EnvType.CLIENT)
public class AttributesPageLayer extends PageLayer {
	private ModifierData modifierData;
	private static AttributesPageLayer instance;
	private static Supplier<Float> scaleX = () -> ExAPI.CONFIG.get().textSqueezeX();
	private static Supplier<Float> scaleY = () -> ExAPI.CONFIG.get().textSqueezeY();
	private static float scaleZ = 0.75F;
	private static final List<RenderComponent> RENDER_COMPONENTS = new ArrayList<RenderComponent>();
	private static final List<Identifier> BUTTON_KEYS = ImmutableList.of(
		new Identifier("playerex:level"),
		new Identifier("playerex:constitution"),
		new Identifier("playerex:strength"),
		new Identifier("playerex:dexterity"),
		new Identifier("playerex:intelligence"),
		new Identifier("playerex:luckiness")
	);
	
	static {
		register(() -> ExAPI.LEVEL.get() != null, () -> {
			EntityAttribute attribute = ExAPI.LEVEL.get();
			Text text = new TranslatableText("gui.playerex.page.attributes.text.level");
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
		}, 21, 26, scaleX, scaleY);
		register(() -> ExAPI.SKILL_POINTS.get() != null, () -> {
			EntityAttribute attribute = ExAPI.SKILL_POINTS.get();
			Text text = new TranslatableText("gui.playerex.page.attributes.text.skill_points");
			AttributeContainer container = instance.client.player.getAttributes();
			int value = container.hasAttribute(attribute) ? (int)container.getValue(attribute) : 0;
			
			return new LiteralText(text.getString() + ": " + value);
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.skill_points[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.skill_points[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 21, 37, scaleX, scaleY);
		register(() -> ExAPI.CONSTITUTION.get() != null, () -> {
			EntityAttribute attribute = ExAPI.CONSTITUTION.get();
			Text text = new TranslatableText("gui.playerex.page.attributes.text.constitution");
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
		}, 21, 59, scaleX, scaleY);
		register(() -> ExAPI.STRENGTH.get() != null, () -> {
			EntityAttribute attribute = ExAPI.STRENGTH.get();
			Text text = new TranslatableText("gui.playerex.page.attributes.text.strength");
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
		}, 21, 70, scaleX, scaleY);
		register(() -> ExAPI.DEXTERITY.get() != null, () -> {
			EntityAttribute attribute = ExAPI.DEXTERITY.get();
			Text text = new TranslatableText("gui.playerex.page.attributes.text.dexterity");
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
		}, 21, 81, scaleX, scaleY);
		register(() -> ExAPI.INTELLIGENCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.INTELLIGENCE.get();
			Text text = new TranslatableText("gui.playerex.page.attributes.text.intelligence");
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
		}, 21, 92, scaleX, scaleY);
		register(() -> ExAPI.LUCKINESS.get() != null, () -> {
			EntityAttribute attribute = ExAPI.LUCKINESS.get();
			Text text = new TranslatableText("gui.playerex.page.attributes.text.luckiness");
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
		}, 21, 103, scaleX, scaleY);
		register(() -> true, () -> {
			EntityAttribute attribute = EntityAttributes.GENERIC_MOVEMENT_SPEED;
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.movement_speed");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.getValue(attribute);
			
			return text.append(": " + ClientUtil.FORMATTING_3.format(value));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = EntityAttributes.GENERIC_MOVEMENT_SPEED;
			AttributeContainer container = instance.client.player.getAttributes();
			String value = ClientUtil.FORMATTING_3.format(20.0D * container.getValue(attribute));
			
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.speed", value)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 21, 125, scaleX, scaleY);
		register(() -> true, () -> {
			float current = instance.client.player.getHealth();
			float maximum = instance.client.player.getMaxHealth();
			String currentVal = ClientUtil.FORMATTING_2.format(current);
			String maximumVal = ClientUtil.FORMATTING_2.format(maximum);
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.health", currentVal, maximumVal);
			
			return text;
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.health")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 37, scaleX, scaleY);
		register(() -> ExAPI.HEALTH_REGENERATION.get() != null, () -> {
			EntityAttribute attribute = ExAPI.HEALTH_REGENERATION.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.health_regeneration");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.health_regeneration[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.health_regeneration[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 48, scaleX, scaleY);
		register(() -> ExAPI.HEAL_AMPLIFICATION.get() != null, () -> {
			EntityAttribute attribute = ExAPI.HEAL_AMPLIFICATION.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.heal_amplification");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue) + "%");
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.heal_amplification[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.heal_amplification[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 59, scaleX, scaleY);
		register(() -> ExAPI.FIRE_RESISTANCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.FIRE_RESISTANCE.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.fire_resistance");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.FIRE_RESISTANCE.get();
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			String formattedValue = ClientUtil.FORMATTING_2.format(100.0D * value);
			
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.fire_resistance", formattedValue)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 92, scaleX, scaleY);
		register(() -> ExAPI.FREEZE_RESISTANCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.FREEZE_RESISTANCE.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.freeze_resistance");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.FREEZE_RESISTANCE.get();
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			String formattedValue = ClientUtil.FORMATTING_2.format(100.0D * value);
			
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.freeze_resistance", formattedValue)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 103, scaleX, scaleY);
		register(() -> ExAPI.FALLING_RESISTANCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.FALLING_RESISTANCE.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.falling_resistance");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.FALLING_RESISTANCE.get();
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			String formattedValue = ClientUtil.FORMATTING_2.format(100.0D * value);
			
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.falling_resistance", formattedValue)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 114, scaleX, scaleY);
		register(() -> ExAPI.DROWNING_RESISTANCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.DROWNING_RESISTANCE.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.drowning_resistance");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.DROWNING_RESISTANCE.get();
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			String formattedValue = ClientUtil.FORMATTING_2.format(100.0D * value);
			
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.drowning_resistance", formattedValue)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 125, scaleX, scaleY);
		register(() -> ExAPI.WITHER_RESISTANCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.WITHER_RESISTANCE.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.wither_resistance");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.WITHER_RESISTANCE.get();
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			String formattedValue = ClientUtil.FORMATTING_2.format(100.0D * value);
			
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.wither_resistance", formattedValue)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 136, scaleX, scaleY);
		register(() -> ExAPI.MAGIC_RESISTANCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.MAGIC_RESISTANCE.get();
			MutableText text = new TranslatableText("gui.playerex.page.attributes.text.magic_resistance");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = ExAPI.MAGIC_RESISTANCE.get();
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			String formattedValue = ClientUtil.FORMATTING_2.format(100.0D * value);
			
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.magic_resistance", formattedValue)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 147, scaleX, scaleY);
	}
	
	public AttributesPageLayer(HandledScreen<?> parent, ScreenHandler handler, PlayerInventory inventory, Text title) {
		super(parent, handler, inventory, title);
		
		instance = this;
	}
	
	private static RenderComponent register(final Supplier<Boolean> shouldRender, final Supplier<Text> text, final Supplier<List<Text>> tooltip, final int dx, final int dy, final Supplier<Float> sx, final Supplier<Float> sy) {
		RenderComponent renderComponent = new RenderComponent(shouldRender, text, tooltip, dx, dy, sx, sy);
		RENDER_COMPONENTS.add(renderComponent);
		
		return renderComponent;
	}
	
	private boolean canRefund() {
		return this.modifierData.refundPoints() > 0;
	}
	
	private boolean areAttributesPresent() {
		AttributeContainer container = this.client.player.getAttributes();
		
		for(Identifier identifier : BUTTON_KEYS) {
			EntityAttribute attribute = API.getAttribute(identifier).get();
			
			if(attribute == null || !container.hasAttribute(attribute)) return false;
		}
		
		return true;
	}
	
	private Pair<EntityAttribute, Double> pair(final EntityAttribute key, final double value) {
		return new Pair<EntityAttribute, Double>(key, value);
	}
	
	private void forEachScreenButton(Consumer<ScreenButtonWidget> consumer) {
		this.children().stream().filter(e -> e instanceof ScreenButtonWidget).forEach(e -> consumer.accept((ScreenButtonWidget)e));
	}
	
	private void buttonPressed(ButtonWidget buttonIn) {
		ScreenButtonWidget button = (ScreenButtonWidget)buttonIn;
		EntityAttribute attribute = API.getAttribute(button.key()).get();
		
		if(attribute == null) return;
		
		double value = this.canRefund() ? -1.0D : 1.0D;
		
		NetworkHandlerClient.modifyAttributes(this.canRefund() ? PacketType.REFUND : PacketType.SKILL, this.pair(attribute, value), this.pair(ExAPI.SKILL_POINTS.get(), (-1) * value));
		this.client.player.playSound(PlayerEx.SP_SPEND_SOUND, SoundCategory.NEUTRAL, ExAPI.CONFIG.get().skillUpVolume(), 1.5F);
	}
	
	private void buttonTooltip(ButtonWidget buttonIn, MatrixStack matrices, int mouseX, int mouseY) {
		ScreenButtonWidget button = (ScreenButtonWidget)buttonIn;
		Identifier level = new Identifier("playerex:level");
		Identifier key = button.key();
		
		if(key.equals(level)) {
			int requiredXp = ExAPI.CONFIG.get().requiredXp(this.client.player);
			int currentXp = this.client.player.experienceLevel;
			
			String progress = "(" + currentXp + "/" + requiredXp + ")";
			Text tooltip = (new TranslatableText("gui.playerex.page.attributes.tooltip.button.level", progress)).formatted(Formatting.GRAY);
			
			this.renderTooltip(matrices, tooltip, mouseX, mouseY);
		} else {
			EntityAttribute attribute = API.getAttribute(key).get();
			
			if(attribute == null) return;
			
			Text text = new TranslatableText(attribute.getTranslationKey());
			String type = "gui.playerex.page.attributes.tooltip.button." + (this.canRefund() ? "refund" : "skill");
			Text tooltip = (new TranslatableText(type)).append(text).formatted(Formatting.GRAY);
			
			this.renderTooltip(matrices, tooltip, mouseX, mouseY);
		}
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		matrices.push();
		matrices.scale(scaleX.get(), scaleY.get(), scaleZ);
		
		RENDER_COMPONENTS.forEach(c -> c.renderText(matrices, this.textRenderer, this.x, this.y));
		
		this.textRenderer.draw(matrices, (new TranslatableText("gui.playerex.page.attributes.text.vitality")), (this.x + 105) / scaleX.get(), (this.y + 26) / scaleY.get(), 4210752);
		this.textRenderer.draw(matrices, (new TranslatableText("gui.playerex.page.attributes.text.resistances")), (this.x + 105) / scaleX.get(), (this.y + 81) / scaleY.get(), 4210752);
		
		matrices.pop();
		
		RENDER_COMPONENTS.forEach(c -> c.renderTooltip(this::renderTooltip, matrices, this.textRenderer, this.x, this.y, mouseX, mouseY));
	}
	
	@Override
	public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, PlayerExClient.GUI);
		
		this.drawTexture(matrices, this.x + 9, this.y + 35, 226, 0, 9, 9);
		this.drawTexture(matrices, this.x + 9, this.y + 123, 235, 0, 9, 9);
		this.drawTexture(matrices, this.x + 93, this.y + 24, 226, 9, 9, 9);
		this.drawTexture(matrices, this.x + 93, this.y + 79, 235, 9, 9, 9);
		
		this.forEachScreenButton(button -> {
			Identifier key = button.key();
			Identifier level = new Identifier("playerex:level");
			
			if(BUTTON_KEYS.contains(key) && this.areAttributesPresent()) {
				PlayerEntity player = this.client.player;
				
				if(key.equals(level)) {
					EntityAttribute attribute = ExAPI.LEVEL.get();
					IAttribute instance = (IAttribute)attribute;
					
					button.active = (player.getAttributeValue(attribute) < instance.getMaxValue()) && (player.experienceLevel >= ExAPI.CONFIG.get().requiredXp(player));
				} else {
					EntityAttribute attribute = API.getAttribute(key).get();
					IAttribute instance = (IAttribute)attribute;
					double value = this.modifierData.get(attribute);
					
					if(this.canRefund()) {
						button.active = value >= 1.0D;
					} else {
						EntityAttribute skillPoints = ExAPI.SKILL_POINTS.get();
						
						button.active = (value < instance.getMaxValue()) && (player.getAttributeValue(skillPoints) >= 1.0D);
					}
					
					button.alt = this.canRefund();
				}
			}
		});
	}
	
	@Override
	protected void init() {
		super.init();
		this.modifierData = ExAPI.DATA.get(this.client.player);
		
		this.addDrawableChild(new ScreenButtonWidget(this.parent, 8, 23, 204, 0, 11, 10, BUTTON_KEYS.get(0), btn -> NetworkHandlerClient.modifyAttributes(PacketType.LEVEL, this.pair(ExAPI.LEVEL.get(), 1.0D)), this::buttonTooltip));
		this.addDrawableChild(new ScreenButtonWidget(this.parent, 8, 56, 204, 0, 11, 10, BUTTON_KEYS.get(1), this::buttonPressed, this::buttonTooltip));
		this.addDrawableChild(new ScreenButtonWidget(this.parent, 8, 67, 204, 0, 11, 10, BUTTON_KEYS.get(2), this::buttonPressed, this::buttonTooltip));
		this.addDrawableChild(new ScreenButtonWidget(this.parent, 8, 78, 204, 0, 11, 10, BUTTON_KEYS.get(3), this::buttonPressed, this::buttonTooltip));
		this.addDrawableChild(new ScreenButtonWidget(this.parent, 8, 89, 204, 0, 11, 10, BUTTON_KEYS.get(4), this::buttonPressed, this::buttonTooltip));
		this.addDrawableChild(new ScreenButtonWidget(this.parent, 8, 100, 204, 0, 11, 10, BUTTON_KEYS.get(5), this::buttonPressed, this::buttonTooltip));
	}
}
