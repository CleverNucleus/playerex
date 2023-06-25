package com.github.clevernucleus.playerex.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.client.ClientUtil;
import com.github.clevernucleus.playerex.api.client.PageLayer;
import com.github.clevernucleus.playerex.api.client.RenderComponent;
import com.github.clevernucleus.playerex.client.PlayerExClient;
import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.projectile_damage.api.EntityAttributes_ProjectileDamage;

@Environment(EnvType.CLIENT)
public class CombatPageLayer extends PageLayer {
	private static Supplier<Float> scaleX = () -> ExAPI.getConfig().textScaleX();
	private static Supplier<Float> scaleY = () -> ExAPI.getConfig().textScaleY();
	private static float scaleZ = 0.75F;
	
	private static final List<RenderComponent> COMPONENTS = new ArrayList<RenderComponent>();
	
	public CombatPageLayer(HandledScreen<?> parent, ScreenHandler handler, PlayerInventory inventory, Text title) {
		super(parent, handler, inventory, title);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		matrices.push();
		matrices.scale(scaleX.get(), scaleY.get(), scaleZ);
		
		COMPONENTS.forEach(component -> component.renderText(this.client.player, matrices, this.textRenderer, this.x, this.y, scaleX.get(), scaleY.get()));
		
		this.textRenderer.draw(matrices, Text.translatable("playerex.gui.page.combat.text.melee").formatted(Formatting.DARK_GRAY), (this.x + 21) / scaleX.get(), (this.y + 26) / scaleY.get(), 4210752);
		this.textRenderer.draw(matrices, Text.translatable("playerex.gui.page.combat.text.defense").formatted(Formatting.DARK_GRAY), (this.x + 21) / scaleX.get(), (this.y + 92) / scaleY.get(), 4210752);
		this.textRenderer.draw(matrices, Text.translatable("playerex.gui.page.combat.text.ranged").formatted(Formatting.DARK_GRAY), (this.x + 105) / scaleX.get(), (this.y + 26) / scaleY.get(), 4210752);
		
		matrices.pop();
		
		COMPONENTS.forEach(component -> component.renderTooltip(this.client.player, this::renderTooltip, matrices, this.textRenderer, this.x, this.y, mouseX, mouseY, scaleX.get(), scaleY.get()));
	}
	
	@Override
	public void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, PlayerExClient.GUI);
		this.drawTexture(matrices, this.x + 9, this.y + 24, 244, 9, 9, 9);
		this.drawTexture(matrices, this.x + 9, this.y + 90, 226, 18, 9, 9);
		this.drawTexture(matrices, this.x + 93, this.y + 24, 235, 18, 9, 9);
		
		DataAttributesAPI.ifPresent(this.client.player, ExAPI.ATTACK_RANGE, (Object)null, value -> {
			this.drawTexture(matrices, this.x + 93, this.y + 79, 226, 27, 9, 9);
			return (Object)null;
		});
		
		DataAttributesAPI.ifPresent(this.client.player, ExAPI.LIFESTEAL, (Object)null, value -> {
			this.drawTexture(matrices, this.x + 93, this.y + 90, 244, 18, 9, 9);
			return (Object)null;
		});
	}
	
	static {
		COMPONENTS.add(RenderComponent.of(() -> EntityAttributes.GENERIC_ATTACK_SPEED, value -> {
			return Text.translatable("playerex.gui.page.combat.text.attack_speed", ClientUtil.FORMATTING_2.format(value)).formatted(Formatting.DARK_GRAY);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();

			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.attack_speed[0]")).formatted(Formatting.GRAY));
			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.attack_speed[1]")).formatted(Formatting.GRAY));

			return tooltip;
		}, 9, 37));
		COMPONENTS.add(RenderComponent.of(() -> EntityAttributes.GENERIC_ATTACK_DAMAGE, value -> {
			return Text.translatable("playerex.gui.page.combat.text.attack_damage", ClientUtil.FORMATTING_2.format(value)).formatted(Formatting.DARK_GRAY);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();

			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.attack_damage[0]")).formatted(Formatting.GRAY));
			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.attack_damage[1]")).formatted(Formatting.GRAY));

			return tooltip;
		}, 9, 48));
		COMPONENTS.add(RenderComponent.of(ExAPI.MELEE_CRIT_DAMAGE, value -> {
			double disp = 100.0D + ClientUtil.displayValue(ExAPI.MELEE_CRIT_DAMAGE, value);
			return Text.translatable("playerex.gui.page.combat.text.melee_crit_damage", ClientUtil.FORMATTING_2.format(disp)).formatted(Formatting.DARK_GRAY);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();

			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.melee_crit_damage[0]")).formatted(Formatting.GRAY));
			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.melee_crit_damage[1]")).formatted(Formatting.GRAY));

			return tooltip;
		}, 9, 59));
		COMPONENTS.add(RenderComponent.of(ExAPI.MELEE_CRIT_CHANCE, value -> {
			double disp = ClientUtil.displayValue(ExAPI.MELEE_CRIT_CHANCE, value);
			return Text.translatable("playerex.gui.page.combat.text.melee_crit_chance", ClientUtil.FORMATTING_2.format(disp)).formatted(Formatting.DARK_GRAY);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();

			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.melee_crit_chance[0]")).formatted(Formatting.GRAY));
			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.melee_crit_chance[1]")).formatted(Formatting.GRAY));

			return tooltip;
		}, 9, 71));
		COMPONENTS.add(RenderComponent.of(() -> EntityAttributes.GENERIC_ARMOR, value -> {
			return Text.translatable("playerex.gui.page.combat.text.armor", ClientUtil.FORMATTING_2.format(value)).formatted(Formatting.DARK_GRAY);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();

			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.armor[0]")).formatted(Formatting.GRAY));
			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.armor[1]")).formatted(Formatting.GRAY));

			return tooltip;
		}, 9, 103));
		COMPONENTS.add(RenderComponent.of(() -> EntityAttributes.GENERIC_ARMOR_TOUGHNESS, value -> {
			return Text.translatable("playerex.gui.page.combat.text.armor_toughness", ClientUtil.FORMATTING_2.format(value)).formatted(Formatting.DARK_GRAY);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.armor_toughness[0]")).formatted(Formatting.GRAY));
			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.armor_toughness[1]")).formatted(Formatting.GRAY));

			return tooltip;
		}, 9, 114));
		COMPONENTS.add(RenderComponent.of(() -> EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, value -> {
			double disp = ClientUtil.displayValue(() -> EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, value);
			return Text.translatable("playerex.gui.page.combat.text.knockback_resistance", ClientUtil.FORMATTING_2.format(disp)).formatted(Formatting.DARK_GRAY);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();
			double disp = 100.0D * value;
			
			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.knockback_resistance", ClientUtil.FORMATTING_2.format(disp))).formatted(Formatting.GRAY));

			return tooltip;
		}, 9, 125));
		COMPONENTS.add(RenderComponent.of(ExAPI.EVASION, value -> {
			double disp = ClientUtil.displayValue(ExAPI.EVASION, value);
			return Text.translatable("playerex.gui.page.combat.text.evasion", ClientUtil.FORMATTING_2.format(disp)).formatted(Formatting.DARK_GRAY);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();

			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.evasion[0]")).formatted(Formatting.GRAY));
			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.evasion[1]")).formatted(Formatting.GRAY));

			return tooltip;
		}, 9, 136));
		COMPONENTS.add(RenderComponent.of(() -> EntityAttributes_ProjectileDamage.GENERIC_PROJECTILE_DAMAGE, value -> {
			double disp = ClientUtil.displayValue(() -> EntityAttributes_ProjectileDamage.GENERIC_PROJECTILE_DAMAGE, value);
			return Text.translatable("playerex.gui.page.combat.text.ranged_damage", ClientUtil.FORMATTING_2.format(disp)).formatted(Formatting.DARK_GRAY);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();

			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.ranged_damage[0]")).formatted(Formatting.GRAY));
			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.ranged_damage[1]")).formatted(Formatting.GRAY));

			return tooltip;
		}, 93, 37));
		COMPONENTS.add(RenderComponent.of(ExAPI.RANGED_CRIT_DAMAGE, value -> {
			double disp = 100.0D + ClientUtil.displayValue(ExAPI.RANGED_CRIT_DAMAGE, value);
			return Text.translatable("playerex.gui.page.combat.text.ranged_crit_damage", ClientUtil.FORMATTING_2.format(disp)).formatted(Formatting.DARK_GRAY);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();

			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.ranged_crit_damage[0]")).formatted(Formatting.GRAY));
			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.ranged_crit_damage[1]")).formatted(Formatting.GRAY));

			return tooltip;
		}, 93, 48));
		COMPONENTS.add(RenderComponent.of(ExAPI.RANGED_CRIT_CHANCE, value -> {
			double disp = ClientUtil.displayValue(ExAPI.RANGED_CRIT_CHANCE, value);
			return Text.translatable("playerex.gui.page.combat.text.ranged_crit_chance", ClientUtil.FORMATTING_2.format(disp)).formatted(Formatting.DARK_GRAY);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();

			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.ranged_crit_chance[0]")).formatted(Formatting.GRAY));
			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.ranged_crit_chance[1]")).formatted(Formatting.GRAY));

			return tooltip;
		}, 93, 59));
		COMPONENTS.add(RenderComponent.of(ExAPI.ATTACK_RANGE, value -> {
			return Text.translatable("playerex.gui.page.combat.text.attack_range", ClientUtil.FORMATTING_2.format(3.0F + value)).formatted(Formatting.DARK_GRAY);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();

			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.attack_range", ClientUtil.FORMATTING_2.format(3.0F + value))).formatted(Formatting.GRAY));

			return tooltip;
		}, 105, 81));
		COMPONENTS.add(RenderComponent.of(ExAPI.LIFESTEAL, value -> {
			double disp = ClientUtil.displayValue(ExAPI.LIFESTEAL, value);
			return Text.translatable("playerex.gui.page.combat.text.lifesteal", ClientUtil.FORMATTING_2.format(disp)).formatted(Formatting.DARK_GRAY);
		}, value -> {
			List<Text> tooltip = new ArrayList<Text>();

			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.lifesteal[0]")).formatted(Formatting.GRAY));
			tooltip.add((Text.translatable("playerex.gui.page.combat.tooltip.lifesteal[1]")).formatted(Formatting.GRAY));

			return tooltip;
		}, 105, 92));
	}
}
