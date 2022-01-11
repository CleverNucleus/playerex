package com.github.clevernucleus.playerex.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.dataattributes.api.attribute.IEntityAttribute;
import com.github.clevernucleus.playerex.PlayerEx;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PacketType;
import com.github.clevernucleus.playerex.api.PlayerData;
import com.github.clevernucleus.playerex.api.client.ClientUtil;
import com.github.clevernucleus.playerex.api.client.PageLayer;
import com.github.clevernucleus.playerex.api.client.RenderComponent;
import com.github.clevernucleus.playerex.client.PlayerExClient;
import com.github.clevernucleus.playerex.client.gui.widget.ScreenButtonWidget;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AttributesPageLayer extends PageLayer {
	private static Supplier<Float> scaleX = () -> ExAPI.getConfig().textScaleX();
	private static Supplier<Float> scaleY = () -> ExAPI.getConfig().textScaleY();
	private static float scaleZ = 0.75F;
	
	private static final List<RenderComponent> COMPONENTS = new ArrayList<RenderComponent>();
	private static final List<Identifier> BUTTON_KEYS = ImmutableList.of(
		new Identifier("playerex:level"),
		new Identifier("playerex:constitution"),
		new Identifier("playerex:strength"),
		new Identifier("playerex:dexterity"),
		new Identifier("playerex:intelligence"),
		new Identifier("playerex:luckiness")
	);
	
	private PlayerData playerData;
	
	public AttributesPageLayer(HandledScreen<?> parent, ScreenHandler handler, PlayerInventory inventory, Text title) {
		super(parent, handler, inventory, title);
	}
	
	private boolean canRefund() {
		return this.playerData.refundPoints() > 0;
	}
	
	private void forEachScreenButton(Consumer<ScreenButtonWidget> consumer) {
		this.children().stream().filter(e -> e instanceof ScreenButtonWidget).forEach(e -> consumer.accept((ScreenButtonWidget)e));
	}
	
	private void buttonPressed(ButtonWidget buttonIn) {
		ScreenButtonWidget button = (ScreenButtonWidget)buttonIn;
		Supplier<EntityAttribute> attribute = DataAttributesAPI.getAttribute(button.key());
		DataAttributesAPI.ifPresent(this.client.player, attribute, (Object)null, amount -> {
			double value = this.canRefund() ? -1.0D : 1.0D;
			
			ClientUtil.modifyAttributes(this.canRefund() ? PacketType.REFUND : PacketType.SKILL, c -> c.accept(attribute, value));
			this.client.player.playSound(PlayerEx.SP_SPEND_SOUND, SoundCategory.NEUTRAL, ExAPI.getConfig().skillUpVolume(), 1.5F);
			return (Object)null;
		});
	}
	
	private void buttonTooltip(ButtonWidget buttonIn, MatrixStack matrices, int mouseX, int mouseY) {
		ScreenButtonWidget button = (ScreenButtonWidget)buttonIn;
		Identifier lvl = new Identifier("playerex:level");
		Identifier key = button.key();
		
		if(key.equals(lvl)) {
			int requiredXp = ExAPI.getConfig().requiredXp(this.client.player);
			int currentXp = this.client.player.experienceLevel;
			String progress = "(" + currentXp + "/" + requiredXp + ")";
			Text tooltip = (new TranslatableText("playerex.gui.page.attributes.tooltip.button.level", progress)).formatted(Formatting.GRAY);
			
			this.renderTooltip(matrices, tooltip, mouseX, mouseY);
		} else {
			Supplier<EntityAttribute> attribute = DataAttributesAPI.getAttribute(key);
			DataAttributesAPI.ifPresent(this.client.player, attribute, (Object)null, value -> {
				Text text = new TranslatableText(attribute.get().getTranslationKey());
				String type = "playerex.gui.page.attributes.tooltip.button." + (this.canRefund() ? "refund" : "skill");
				Text tooltip = (new TranslatableText(type)).append(text).formatted(Formatting.GRAY);
				
				this.renderTooltip(matrices, tooltip, mouseX, mouseY);
				return (Object)null;
			});
		}
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		matrices.push();
		matrices.scale(scaleX.get(), scaleY.get(), scaleZ);
		
		COMPONENTS.forEach(component -> component.renderText(this.client.player, matrices, this.textRenderer, this.x, this.y, scaleX.get(), scaleY.get()));
		
		this.textRenderer.draw(matrices, (new TranslatableText("playerex.gui.page.attributes.text.vitality")), (this.x + 105) / scaleX.get(), (this.y + 26) / scaleY.get(), 4210752);
		this.textRenderer.draw(matrices, (new TranslatableText("playerex.gui.page.attributes.text.resistances")), (this.x + 105) / scaleX.get(), (this.y + 81) / scaleY.get(), 4210752);
		
		matrices.pop();
		
		COMPONENTS.forEach(component -> component.renderTooltip(this.client.player, this::renderTooltip, matrices, this.textRenderer, this.x, this.y, mouseX, mouseY, scaleX.get(), scaleY.get()));
	}
	
	@Override
	public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, PlayerExClient.GUI);
		this.drawTexture(matrices, this.x + 9, this.y + 35, 226, 0, 9, 9);
		this.drawTexture(matrices, this.x + 9, this.y + 123, 235, 0, 9, 9);
		this.drawTexture(matrices, this.x + 93, this.y + 24, 226, 9, 9, 9);
		this.drawTexture(matrices, this.x + 93, this.y + 79, 235, 9, 9, 9);
		
		DataAttributesAPI.ifPresent(this.client.player, ExAPI.BREAKING_SPEED, (Object)null, value -> {
			this.drawTexture(matrices, this.x + 9, this.y + 134, 235, 36, 9, 9);
			return (Object)null;
		});
		
		DataAttributesAPI.ifPresent(this.client.player, ExAPI.REACH_DISTANCE, (Object)null, value -> {
			this.drawTexture(matrices, this.x + 9, this.y + 145, 244, 0, 9, 9);
			return (Object)null;
		});
		
		this.forEachScreenButton(button -> {
			Identifier key = button.key();
			Identifier lvl = new Identifier("playerex:level");
			Supplier<EntityAttribute> attribute = DataAttributesAPI.getAttribute(key);
			PlayerEntity player = this.client.player;
			
			DataAttributesAPI.ifPresent(player, attribute, (Object)null, value -> {
				if(BUTTON_KEYS.contains(key)) {
					if(key.equals(lvl)) {
						button.active = (value < ((IEntityAttribute)attribute.get()).getMaxValue()) && (player.experienceLevel >= ExAPI.getConfig().requiredXp(player));
					} else {
						double modifierValue = this.playerData.get(attribute.get());
						
						if(this.canRefund()) {
							button.active = modifierValue >= 1.0D;
						} else {
							button.active = this.playerData.skillPoints() >= 1;
						}
						
						button.alt = this.canRefund();
					}
				}
				
				return (Object)null;
			});
		});
	}
	
	@Override
	protected void init() {
		super.init();
		this.playerData = ExAPI.INSTANCE.get(this.client.player);
		
		this.addDrawableChild(new ScreenButtonWidget(this.parent, 8, 23, 204, 0, 11, 10, BUTTON_KEYS.get(0), btn -> ClientUtil.modifyAttributes(PacketType.LEVEL, c -> c.accept(ExAPI.LEVEL, 1.0D)), this::buttonTooltip));
		this.addDrawableChild(new ScreenButtonWidget(this.parent, 8, 56, 204, 0, 11, 10, BUTTON_KEYS.get(1), this::buttonPressed, this::buttonTooltip));
		this.addDrawableChild(new ScreenButtonWidget(this.parent, 8, 67, 204, 0, 11, 10, BUTTON_KEYS.get(2), this::buttonPressed, this::buttonTooltip));
		this.addDrawableChild(new ScreenButtonWidget(this.parent, 8, 78, 204, 0, 11, 10, BUTTON_KEYS.get(3), this::buttonPressed, this::buttonTooltip));
		this.addDrawableChild(new ScreenButtonWidget(this.parent, 8, 89, 204, 0, 11, 10, BUTTON_KEYS.get(4), this::buttonPressed, this::buttonTooltip));
		this.addDrawableChild(new ScreenButtonWidget(this.parent, 8, 100, 204, 0, 11, 10, BUTTON_KEYS.get(5), this::buttonPressed, this::buttonTooltip));
	}
	
	static {
		COMPONENTS.add(RenderComponent.of(ExAPI.LEVEL, value -> {
			return new TranslatableText("playerex.gui.page.attributes.text.level", Math.round(value));
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.level[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.level[1]")).formatted(Formatting.GRAY));
			tooltip.add(LiteralText.EMPTY);
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.level[2]", ExAPI.getConfig().skillPointsPerLevelUp())).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 21, 26));
		COMPONENTS.add(RenderComponent.of(entity -> {
			return new TranslatableText("playerex.gui.page.attributes.text.skill_points", ExAPI.INSTANCE.get(entity).skillPoints());
		}, entity -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.skill_points[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.skill_points[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 21, 37));
		COMPONENTS.add(RenderComponent.of(ExAPI.CONSTITUTION, value -> {
			return new TranslatableText("playerex.gui.page.attributes.text.constitution", Math.round(value));
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText(ExAPI.CONSTITUTION.get().getTranslationKey())).formatted(Formatting.GRAY));
			tooltip.add(LiteralText.EMPTY);
			
			ClientUtil.appendChildrenToTooltip(tooltip, ExAPI.CONSTITUTION);
			return tooltip;
		}, 21, 59));
		COMPONENTS.add(RenderComponent.of(ExAPI.STRENGTH, value -> {
			return new TranslatableText("playerex.gui.page.attributes.text.strength", Math.round(value));
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText(ExAPI.STRENGTH.get().getTranslationKey())).formatted(Formatting.GRAY));
			tooltip.add(LiteralText.EMPTY);
			
			ClientUtil.appendChildrenToTooltip(tooltip, ExAPI.STRENGTH);
			return tooltip;
		}, 21, 70));
		COMPONENTS.add(RenderComponent.of(ExAPI.DEXTERITY, value -> {
			return new TranslatableText("playerex.gui.page.attributes.text.dexterity", Math.round(value));
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText(ExAPI.DEXTERITY.get().getTranslationKey())).formatted(Formatting.GRAY));
			tooltip.add(LiteralText.EMPTY);
			
			ClientUtil.appendChildrenToTooltip(tooltip, ExAPI.DEXTERITY);
			return tooltip;
		}, 21, 81));
		COMPONENTS.add(RenderComponent.of(ExAPI.INTELLIGENCE, value -> {
			return new TranslatableText("playerex.gui.page.attributes.text.intelligence", Math.round(value));
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText(ExAPI.INTELLIGENCE.get().getTranslationKey())).formatted(Formatting.GRAY));
			tooltip.add(LiteralText.EMPTY);
			
			ClientUtil.appendChildrenToTooltip(tooltip, ExAPI.INTELLIGENCE);
			return tooltip;
		}, 21, 92));
		COMPONENTS.add(RenderComponent.of(ExAPI.LUCKINESS, value -> {
			return new TranslatableText("playerex.gui.page.attributes.text.luckiness", Math.round(value));
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText(ExAPI.LUCKINESS.get().getTranslationKey())).formatted(Formatting.GRAY));
			tooltip.add(LiteralText.EMPTY);
			
			ClientUtil.appendChildrenToTooltip(tooltip, ExAPI.LUCKINESS);
			return tooltip;
		}, 21, 103));
		COMPONENTS.add(RenderComponent.of(entity -> {
			return new TranslatableText("playerex.gui.page.attributes.text.movement_speed", ClientUtil.FORMATTING_3.format(entity.getMovementSpeed()));
		}, entity -> {
			List<Text> tooltip = new ArrayList<Text>();
			String formatted = ClientUtil.FORMATTING_3.format(20.0D * entity.getMovementSpeed());
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.movement_speed", formatted)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 21, 125));
		COMPONENTS.add(RenderComponent.of(ExAPI.BREAKING_SPEED, value -> {
			return new TranslatableText("playerex.gui.page.attributes.text.breaking_speed", ClientUtil.FORMATTING_3.format(value));
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.breaking_speed")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 21, 136));
		COMPONENTS.add(RenderComponent.of(ExAPI.REACH_DISTANCE, value -> {
			return new TranslatableText("playerex.gui.page.attributes.text.reach_distance", ClientUtil.FORMATTING_2.format(4.5F + value));
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.reach_distance", ClientUtil.FORMATTING_2.format(4.5F + value))).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 21, 147));
		COMPONENTS.add(RenderComponent.of(entity -> {
			String current = ClientUtil.FORMATTING_2.format(entity.getHealth());
			String maximum = ClientUtil.FORMATTING_2.format(entity.getMaxHealth());
			
			return new TranslatableText("playerex.gui.page.attributes.text.health", current, maximum);
		}, entity -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.health")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 37));
		COMPONENTS.add(RenderComponent.of(ExAPI.HEALTH_REGENERATION, value -> {
			return new TranslatableText("playerex.gui.page.attributes.text.health_regeneration", value);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.health_regeneration[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.health_regeneration[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 48));
		COMPONENTS.add(RenderComponent.of(ExAPI.HEAL_AMPLIFICATION, value -> {
			String displ = ClientUtil.FORMATTING_2.format(ClientUtil.displayValue(ExAPI.HEAL_AMPLIFICATION, value));
			return new TranslatableText("playerex.gui.page.attributes.text.heal_amplification", displ);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.heal_amplification[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.heal_amplification[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 59));
		COMPONENTS.add(RenderComponent.of(ExAPI.FIRE_RESISTANCE, value -> {
			String displ = ClientUtil.FORMATTING_2.format(ClientUtil.displayValue(ExAPI.FIRE_RESISTANCE, value));
			return new TranslatableText("playerex.gui.page.attributes.text.fire_resistance", displ);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			String displ = ClientUtil.FORMATTING_2.format(100.0F * value);
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.fire_resistance", displ)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 92));
		COMPONENTS.add(RenderComponent.of(ExAPI.FREEZE_RESISTANCE, value -> {
			String displ = ClientUtil.FORMATTING_2.format(ClientUtil.displayValue(ExAPI.FREEZE_RESISTANCE, value));
			return new TranslatableText("playerex.gui.page.attributes.text.freeze_resistance", displ);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			String displ = ClientUtil.FORMATTING_2.format(100.0F * value);
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.freeze_resistance", displ)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 103));
		COMPONENTS.add(RenderComponent.of(ExAPI.LIGHTNING_RESISTANCE, value -> {
			String displ = ClientUtil.FORMATTING_2.format(ClientUtil.displayValue(ExAPI.LIGHTNING_RESISTANCE, value));
			return new TranslatableText("playerex.gui.page.attributes.text.lightning_resistance", displ);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			String displ = ClientUtil.FORMATTING_2.format(100.0F * value);
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.lightning_resistance", displ)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 114));
		COMPONENTS.add(RenderComponent.of(ExAPI.POISON_RESISTANCE, value -> {
			String displ = ClientUtil.FORMATTING_2.format(ClientUtil.displayValue(ExAPI.POISON_RESISTANCE, value));
			return new TranslatableText("playerex.gui.page.attributes.text.poison_resistance", displ);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			String displ = ClientUtil.FORMATTING_2.format(100.0F * value);
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.poison_resistance", displ)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 125));
		COMPONENTS.add(RenderComponent.of(ExAPI.WITHER_RESISTANCE, value -> {
			String displ = ClientUtil.FORMATTING_2.format(ClientUtil.displayValue(ExAPI.WITHER_RESISTANCE, value));
			return new TranslatableText("playerex.gui.page.attributes.text.wither_resistance", displ);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			String displ = ClientUtil.FORMATTING_2.format(100.0F * value);
			tooltip.add((new TranslatableText("playerex.gui.page.attributes.tooltip.wither_resistance", displ)).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 136));
	}
}
