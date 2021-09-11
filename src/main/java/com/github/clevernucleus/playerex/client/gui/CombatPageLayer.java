package com.github.clevernucleus.playerex.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.github.clevernucleus.dataattributes.api.attribute.IAttribute;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.ClientUtil;
import com.github.clevernucleus.playerex.api.client.RenderComponent;
import com.github.clevernucleus.playerex.api.client.page.PageLayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class CombatPageLayer extends PageLayer {
	private static CombatPageLayer instance;
	private static Supplier<Float> scaleX = () -> ExAPI.CONFIG.get().textSqueezeX();
	private static Supplier<Float> scaleY = () -> ExAPI.CONFIG.get().textSqueezeY();
	private static float scaleZ = 0.75F;
	private static final List<RenderComponent> RENDER_COMPONENTS = new ArrayList<RenderComponent>();
	
	static {
		register(() -> true, () -> {
			EntityAttribute attribute = EntityAttributes.GENERIC_ATTACK_SPEED;
			MutableText text = new TranslatableText("gui.playerex.page.combat.text.attack_speed");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.getValue(attribute);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(value));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.attack_speed[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.attack_speed[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 9, 37, scaleX, scaleY);
		register(() -> true, () -> {
			EntityAttribute attribute = EntityAttributes.GENERIC_ATTACK_DAMAGE;
			MutableText text = new TranslatableText("gui.playerex.page.combat.text.attack_damage");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.getValue(attribute);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(value));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.attack_damage[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.attack_damage[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 9, 48, scaleX, scaleY);
		register(() -> ExAPI.MELEE_CRIT_DAMAGE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.MELEE_CRIT_DAMAGE.get();
			IAttribute attributeInstance = (IAttribute)attribute;
			MutableText text = new TranslatableText("gui.playerex.page.combat.text.melee_crit_damage");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue(attributeInstance, value);
			
			if(attributeInstance.hasProperty(ExAPI.PERCENTAGE_PROPERTY)) {
				displayValue += (double)attributeInstance.getProperty(ExAPI.PERCENTAGE_PROPERTY);
			}
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue) + "%");
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.melee_crit_damage[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.melee_crit_damage[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 9, 59, scaleX, scaleY);
		register(() -> ExAPI.MELEE_CRIT_CHANCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.MELEE_CRIT_CHANCE.get();
			IAttribute attributeInstance = (IAttribute)attribute;
			MutableText text = new TranslatableText("gui.playerex.page.combat.text.melee_crit_chance");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue(attributeInstance, value);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue) + "%");
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.melee_crit_chance[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.melee_crit_chance[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 9, 71, scaleX, scaleY);
		register(() -> true, () -> {
			EntityAttribute attribute = EntityAttributes.GENERIC_ARMOR;
			MutableText text = new TranslatableText("gui.playerex.page.combat.text.armor");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.getValue(attribute);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(value));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.armor[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.armor[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 9, 103, scaleX, scaleY);
		register(() -> true, () -> {
			EntityAttribute attribute = EntityAttributes.GENERIC_ARMOR_TOUGHNESS;
			MutableText text = new TranslatableText("gui.playerex.page.combat.text.armor_toughness");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.getValue(attribute);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(value));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.armor_toughness[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.armor_toughness[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 9, 114, scaleX, scaleY);
		register(() -> true, () -> {
			EntityAttribute attribute = EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE;
			MutableText text = new TranslatableText("gui.playerex.page.combat.text.knockback_resistance");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.getValue(attribute);
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			EntityAttribute attribute = EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE;
			AttributeContainer container = instance.client.player.getAttributes();
			double value = 100.0D * container.getValue(attribute);
			
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.knockback_resistance", ClientUtil.FORMATTING_2.format(value))).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 9, 125, scaleX, scaleY);
		register(() -> ExAPI.EVASION.get() != null, () -> {
			EntityAttribute attribute = ExAPI.EVASION.get();
			IAttribute attributeInstance = (IAttribute)attribute;
			MutableText text = new TranslatableText("gui.playerex.page.combat.text.evasion");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue(attributeInstance, value);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue) + "%");
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.evasion[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.evasion[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 9, 136, scaleX, scaleY);
		register(() -> ExAPI.RANGED_DAMAGE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.RANGED_DAMAGE.get();
			IAttribute attributeInstance = (IAttribute)attribute;
			MutableText text = new TranslatableText("gui.playerex.page.combat.text.ranged_damage");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue(attributeInstance, value);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue));
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.ranged_damage[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.ranged_damage[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 37, scaleX, scaleY);
		register(() -> ExAPI.RANGED_CRIT_DAMAGE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.RANGED_CRIT_DAMAGE.get();
			IAttribute attributeInstance = (IAttribute)attribute;
			MutableText text = new TranslatableText("gui.playerex.page.combat.text.ranged_crit_damage");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue(attributeInstance, value);
			
			if(attributeInstance.hasProperty(ExAPI.PERCENTAGE_PROPERTY)) {
				displayValue += (double)attributeInstance.getProperty(ExAPI.PERCENTAGE_PROPERTY);
			}
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue) + "%");
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.ranged_crit_damage[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.ranged_crit_damage[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 48, scaleX, scaleY);
		register(() -> ExAPI.RANGED_CRIT_CHANCE.get() != null, () -> {
			EntityAttribute attribute = ExAPI.RANGED_CRIT_CHANCE.get();
			IAttribute attributeInstance = (IAttribute)attribute;
			MutableText text = new TranslatableText("gui.playerex.page.combat.text.ranged_crit_chance");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue(attributeInstance, value);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue) + "%");
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.ranged_crit_chance[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.ranged_crit_chance[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 93, 59, scaleX, scaleY);
		register(() -> ExAPI.MAGIC_AMPLIFICATION.get() != null, () -> {
			EntityAttribute attribute = ExAPI.MAGIC_AMPLIFICATION.get();
			MutableText text = new TranslatableText("gui.playerex.page.combat.text.magic_amplification");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue) + "%");
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.magic_amplification[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.attributes.tooltip.magic_amplification[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 105, 92, scaleX, scaleY);
		register(() -> ExAPI.LIFESTEAL.get() != null, () -> {
			EntityAttribute attribute = ExAPI.LIFESTEAL.get();
			MutableText text = new TranslatableText("gui.playerex.page.combat.text.lifesteal");
			AttributeContainer container = instance.client.player.getAttributes();
			double value = container.hasAttribute(attribute) ? container.getValue(attribute) : 0.0D;
			double displayValue = ClientUtil.displayValue((IAttribute)attribute, value);
			
			return text.append(": " + ClientUtil.FORMATTING_2.format(displayValue) + "%");
		}, () -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.lifesteal[0]")).formatted(Formatting.GRAY));
			tooltip.add((new TranslatableText("gui.playerex.page.combat.tooltip.lifesteal[1]")).formatted(Formatting.GRAY));
			
			return tooltip;
		}, 105, 103, scaleX, scaleY);
	}
	
	public CombatPageLayer(HandledScreen<?> parent, ScreenHandler handler, PlayerInventory inventory, Text title) {
		super(parent, handler, inventory, title);
		
		instance = this;
	}
	
	private static RenderComponent register(final Supplier<Boolean> shouldRender, final Supplier<Text> text, final Supplier<List<Text>> tooltip, final int dx, final int dy, final Supplier<Float> sx, final Supplier<Float> sy) {
		RenderComponent renderComponent = new RenderComponent(shouldRender, text, tooltip, dx, dy, sx, sy);
		RENDER_COMPONENTS.add(renderComponent);
		
		return renderComponent;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		matrices.push();
		matrices.scale(scaleX.get(), scaleY.get(), scaleZ);
		
		RENDER_COMPONENTS.forEach(c -> c.renderText(matrices, this.textRenderer, this.x, this.y));
		
		this.textRenderer.draw(matrices, (new TranslatableText("gui.playerex.page.combat.text.melee")), (this.x + 21) / scaleX.get(), (this.y + 26) / scaleY.get(), 4210752);
		this.textRenderer.draw(matrices, (new TranslatableText("gui.playerex.page.combat.text.defense")), (this.x + 21) / scaleX.get(), (this.y + 92) / scaleY.get(), 4210752);
		this.textRenderer.draw(matrices, (new TranslatableText("gui.playerex.page.combat.text.ranged")), (this.x + 105) / scaleX.get(), (this.y + 26) / scaleY.get(), 4210752);
		
		matrices.pop();
		
		RENDER_COMPONENTS.forEach(c -> c.renderTooltip(this::renderTooltip, matrices, this.textRenderer, this.x, this.y, mouseX, mouseY));
	}
	
	@Override
	public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		this.drawTexture(matrices, this.x + 9, this.y + 24, 244, 9, 9, 9);
		this.drawTexture(matrices, this.x + 9, this.y + 90, 226, 18, 9, 9);
		this.drawTexture(matrices, this.x + 93, this.y + 24, 235, 18, 9, 9);
		this.drawTexture(matrices, this.x + 93, this.y + 90, 235, 27, 9, 9);
		this.drawTexture(matrices, this.x + 93, this.y + 101, 244, 18, 9, 9);
	}
}
