package com.github.clevernucleus.playerex.impl;

import java.util.Collection;
import java.util.function.Supplier;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.dataattributes.api.attribute.IEntityAttribute;
import com.github.clevernucleus.playerex.api.EntityAttributeSupplier;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PacketType;
import com.github.clevernucleus.playerex.api.PlayerData;
import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public final class CommandsImpl {
	private static final Supplier<Collection<Identifier>> PRIMARIES = () -> Sets.newHashSet(ExAPI.CONSTITUTION.getId(), ExAPI.STRENGTH.getId(), ExAPI.DEXTERITY.getId(), ExAPI.INTELLIGENCE.getId(), ExAPI.LUCKINESS.getId());
	private static final SuggestionProvider<ServerCommandSource> SUGGESTION_PROVIDER = (context, builder) -> CommandSource.suggestIdentifiers(PRIMARIES.get(), builder);
	
	private static void registerReset(CommandNode<ServerCommandSource> root) {
		LiteralCommandNode<ServerCommandSource> reset = CommandManager.literal("reset").build();
		root.addChild(reset);
		
		ArgumentCommandNode<ServerCommandSource, EntitySelector> player = CommandManager.argument("player", EntityArgumentType.player()).executes(ctx -> {
			ServerPlayerEntity serverPlayerEntity = EntityArgumentType.getPlayer(ctx, "player");
			PlayerData playerData = ExAPI.PLAYER_DATA.get(serverPlayerEntity);
			playerData.reset();
			Supplier<Text> text = () -> Text.translatable("playerex.command.reset", serverPlayerEntity.getName());
			ctx.getSource().sendFeedback(text, false);
			
			return 1;
		}).build();
		reset.addChild(player);
	}
	
	private static void registerRefund(CommandNode<ServerCommandSource> root) {
		LiteralCommandNode<ServerCommandSource> refund = CommandManager.literal("refund").build();
		root.addChild(refund);
		
		ArgumentCommandNode<ServerCommandSource, EntitySelector> player = CommandManager.argument("player", EntityArgumentType.player()).executes(ctx -> {
			ServerPlayerEntity serverPlayerEntity = EntityArgumentType.getPlayer(ctx, "player");
			PlayerData playerData = ExAPI.PLAYER_DATA.get(serverPlayerEntity);
			int refunded = playerData.addRefundPoints(1);
			
			if(refunded == 1) {
				Supplier<Text> text = () -> Text.translatable("playerex.command.refund_alt", serverPlayerEntity.getName());
				ctx.getSource().sendFeedback(text, false);
			} else {
				Supplier<Text> text = () -> Text.translatable("playerex.command.refund", refunded, serverPlayerEntity.getName());
				ctx.getSource().sendFeedback(text, false);
			}
			
			return refunded % 16;
		}).build();
		refund.addChild(player);
		
		ArgumentCommandNode<ServerCommandSource, Integer> amount = CommandManager.argument("amount", IntegerArgumentType.integer(1)).executes(ctx -> {
			ServerPlayerEntity serverPlayerEntity = EntityArgumentType.getPlayer(ctx, "player");
			PlayerData playerData = ExAPI.PLAYER_DATA.get(serverPlayerEntity);
			int value = IntegerArgumentType.getInteger(ctx, "amount");
			int refunded = playerData.addRefundPoints(value);
			
			if(refunded == 1) {
				Supplier<Text> text = () -> Text.translatable("playerex.command.refund_alt", serverPlayerEntity.getName());
				ctx.getSource().sendFeedback(text, false);
			} else {
				Supplier<Text> text = () -> Text.translatable("playerex.command.refund", refunded, serverPlayerEntity.getName());
				ctx.getSource().sendFeedback(text, false);
			}
			
			return refunded % 16;
		}).build();
		player.addChild(amount);
	}
	
	private static void registerLevelUp(CommandNode<ServerCommandSource> root) {
		LiteralCommandNode<ServerCommandSource> levelup = CommandManager.literal("levelup").build();
		root.addChild(levelup);
		
		ArgumentCommandNode<ServerCommandSource, EntitySelector> player = CommandManager.argument("player", EntityArgumentType.player()).executes(ctx -> {
			ServerPlayerEntity serverPlayerEntity = EntityArgumentType.getPlayer(ctx, "player");
			PlayerData playerData = ExAPI.PLAYER_DATA.get(serverPlayerEntity);
			return DataAttributesAPI.ifPresent(serverPlayerEntity, ExAPI.LEVEL, -1, value -> {
				EntityAttribute attribute = ExAPI.LEVEL.get();
				
				if(((IEntityAttribute)attribute).maxValue() - value < 1) {
					Supplier<Text> text = () -> Text.translatable("playerex.command.levelup_max", serverPlayerEntity.getName()).formatted(Formatting.RED);
					ctx.getSource().sendFeedback(text, false);
					return -1;
				}
				
				playerData.add(ExAPI.LEVEL, 1);
				playerData.addSkillPoints(ExAPI.getConfig().skillPointsPerLevelUp());
				Supplier<Text> text = () -> Text.translatable("playerex.command.levelup", serverPlayerEntity.getName());
				ctx.getSource().sendFeedback(text, false);
				return 1;
			});
		}).build();
		levelup.addChild(player);
		
		ArgumentCommandNode<ServerCommandSource, Integer> amount = CommandManager.argument("amount", IntegerArgumentType.integer(1)).executes(ctx -> {
			ServerPlayerEntity serverPlayerEntity = EntityArgumentType.getPlayer(ctx, "player");
			PlayerData playerData = ExAPI.PLAYER_DATA.get(serverPlayerEntity);
			int levels = IntegerArgumentType.getInteger(ctx, "amount");
			return DataAttributesAPI.ifPresent(serverPlayerEntity, ExAPI.LEVEL, -1, value -> {
				EntityAttribute attribute = ExAPI.LEVEL.get();
				int max = Math.round((float)(((IEntityAttribute)attribute).maxValue() - value));
				
				if(max < 1) {
					Supplier<Text> text = () -> Text.translatable("playerex.command.attribute_max_error", Text.translatable(attribute.getTranslationKey()), serverPlayerEntity.getName()).formatted(Formatting.RED);
					ctx.getSource().sendFeedback(text, false);
					return -1;
				}
				
				int adding = MathHelper.clamp(levels, 1, max);
				playerData.add(ExAPI.LEVEL, adding);
				playerData.addSkillPoints(adding * ExAPI.getConfig().skillPointsPerLevelUp());
				
				if(adding == 1) {
					Supplier<Text> text = () -> Text.translatable("playerex.command.levelup_alt", serverPlayerEntity.getName());
					ctx.getSource().sendFeedback(text, false);
				} else {
					Supplier<Text> text = () -> Text.translatable("playerex.command.levelup", adding, serverPlayerEntity.getName());
					ctx.getSource().sendFeedback(text, false);
				}
				
				return adding % 16;
			});
		}).build();
		player.addChild(amount);
	}
	
	private static void registerSkillAttribute(CommandNode<ServerCommandSource> root) {
		LiteralCommandNode<ServerCommandSource> skillAttribute = CommandManager.literal("skillAttribute").build();
		root.addChild(skillAttribute);
		
		ArgumentCommandNode<ServerCommandSource, EntitySelector> player = CommandManager.argument("player", EntityArgumentType.player()).build();
		skillAttribute.addChild(player);
		
		ArgumentCommandNode<ServerCommandSource, Identifier> attribute = CommandManager.argument("attribute", IdentifierArgumentType.identifier()).suggests(SUGGESTION_PROVIDER).executes(ctx -> {
			ServerPlayerEntity serverPlayerEntity = EntityArgumentType.getPlayer(ctx, "player");
			PlayerData playerData = ExAPI.PLAYER_DATA.get(serverPlayerEntity);
			Identifier identifier = IdentifierArgumentType.getIdentifier(ctx, "attribute");
			EntityAttributeSupplier primary = EntityAttributeSupplier.of(identifier);
			return DataAttributesAPI.ifPresent(serverPlayerEntity, primary, -1, value -> {
				EntityAttribute attr = primary.get();
				
				if(playerData.get(primary) < ((IEntityAttribute)attr).maxValue()) {
					if(PacketType.SKILL.test(ctx.getSource().getServer(), serverPlayerEntity, playerData)) {
						playerData.add(primary, 1);
						Supplier<Text> text = () -> Text.translatable("playerex.command.skill_attribute", Text.translatable(attr.getTranslationKey()), serverPlayerEntity.getName());
						ctx.getSource().sendFeedback(text, false);
						return 1;
					} else {
						Supplier<Text> text = () -> Text.translatable("playerex.command.skill_attribute_error", serverPlayerEntity.getName()).formatted(Formatting.RED);
						ctx.getSource().sendFeedback(text, false);
						return -1;
					}
				} else {
					Supplier<Text> text = () -> Text.translatable("playerex.command.attribute_max_error", Text.translatable(attr.getTranslationKey()), serverPlayerEntity.getName()).formatted(Formatting.RED);
					ctx.getSource().sendFeedback(text, false);
					return -1;
				}
			});
		}).build();
		player.addChild(attribute);
		
		ArgumentCommandNode<ServerCommandSource, String> requiresSkillPoints = CommandManager.argument("requires", StringArgumentType.word()).suggests((ctx, builder) -> CommandSource.suggestMatching(Sets.newHashSet("true", "false"), builder)).executes(ctx -> {
			ServerPlayerEntity serverPlayerEntity = EntityArgumentType.getPlayer(ctx, "player");
			PlayerData playerData = ExAPI.PLAYER_DATA.get(serverPlayerEntity);
			Identifier identifier = IdentifierArgumentType.getIdentifier(ctx, "attribute");
			EntityAttributeSupplier primary = EntityAttributeSupplier.of(identifier);
			String requires = StringArgumentType.getString(ctx, "requires");
			return DataAttributesAPI.ifPresent(serverPlayerEntity, primary, -1, value -> {
				EntityAttribute attr = primary.get();
				
				if(playerData.get(primary) < ((IEntityAttribute)attr).maxValue()) {
					if(!requires.equals("true")) {
						playerData.add(primary, 1);
						Supplier<Text> text = () -> Text.translatable("playerex.command.skill_attribute", Text.translatable(attr.getTranslationKey()), serverPlayerEntity.getName());
						ctx.getSource().sendFeedback(text, false);
						return 1;
					} else if(PacketType.SKILL.test(ctx.getSource().getServer(), serverPlayerEntity, playerData)) {
						playerData.add(primary, 1);
						Supplier<Text> text = () -> Text.translatable("playerex.command.skill_attribute", Text.translatable(attr.getTranslationKey()), serverPlayerEntity.getName());
						ctx.getSource().sendFeedback(text, false);
						return 1;
					} else {
						Supplier<Text> text = () -> Text.translatable("playerex.command.skill_attribute_error", serverPlayerEntity.getName()).formatted(Formatting.RED);
						ctx.getSource().sendFeedback(text, false);
						return -1;
					}
				} else {
					Supplier<Text> text = () -> Text.translatable("playerex.command.attribute_max_error", Text.translatable(attr.getTranslationKey()), serverPlayerEntity.getName()).formatted(Formatting.RED);
					ctx.getSource().sendFeedback(text, false);
					return -1;
				}
			});
		}).build();
		attribute.addChild(requiresSkillPoints);
	}
	
	private static void registerRefundAttribute(CommandNode<ServerCommandSource> root) {
		LiteralCommandNode<ServerCommandSource> refundAttribute = CommandManager.literal("refundAttribute").build();
		root.addChild(refundAttribute);
		
		ArgumentCommandNode<ServerCommandSource, EntitySelector> player = CommandManager.argument("player", EntityArgumentType.player()).build();
		refundAttribute.addChild(player);
		
		ArgumentCommandNode<ServerCommandSource, Identifier> attribute = CommandManager.argument("attribute", IdentifierArgumentType.identifier()).suggests(SUGGESTION_PROVIDER).executes(ctx -> {
			ServerPlayerEntity serverPlayerEntity = EntityArgumentType.getPlayer(ctx, "player");
			PlayerData playerData = ExAPI.PLAYER_DATA.get(serverPlayerEntity);
			Identifier identifier = IdentifierArgumentType.getIdentifier(ctx, "attribute");
			EntityAttributeSupplier primary = EntityAttributeSupplier.of(identifier);
			return DataAttributesAPI.ifPresent(serverPlayerEntity, primary, -1, value -> {
				EntityAttribute attr = primary.get();
				
				if(playerData.get(primary) > 0) {
					if(PacketType.REFUND.test(ctx.getSource().getServer(), serverPlayerEntity, playerData)) {
						playerData.add(primary, -1);
						Supplier<Text> text = () -> Text.translatable("playerex.command.refund_attribute", Text.translatable(attr.getTranslationKey()), serverPlayerEntity.getName());
						ctx.getSource().sendFeedback(text, false);
						return 1;
					} else {
						Supplier<Text> text = () -> Text.translatable("playerex.command.refund_attribute_error", serverPlayerEntity.getName()).formatted(Formatting.RED);
						ctx.getSource().sendFeedback(text, false);
						return -1;
					}
				} else {
					Supplier<Text> text = () -> Text.translatable("playerex.command.refund_attribute_unskilled", Text.translatable(attr.getTranslationKey()), serverPlayerEntity.getName()).formatted(Formatting.RED);
					ctx.getSource().sendFeedback(text, false);
					return -1;
				}
			});
		}).build();
		player.addChild(attribute);
		
		ArgumentCommandNode<ServerCommandSource, String> requiresRefundPoints = CommandManager.argument("requires", StringArgumentType.word()).suggests((ctx, builder) -> CommandSource.suggestMatching(Sets.newHashSet("true", "false"), builder)).executes(ctx -> {
			ServerPlayerEntity serverPlayerEntity = EntityArgumentType.getPlayer(ctx, "player");
			PlayerData playerData = ExAPI.PLAYER_DATA.get(serverPlayerEntity);
			Identifier identifier = IdentifierArgumentType.getIdentifier(ctx, "attribute");
			EntityAttributeSupplier primary = EntityAttributeSupplier.of(identifier);
			String requires = StringArgumentType.getString(ctx, "requires");
			return DataAttributesAPI.ifPresent(serverPlayerEntity, primary, -1, value -> {
				EntityAttribute attr = primary.get();
				
				if(playerData.get(primary) > 0) {
					if(!requires.equals("true")) {
						playerData.add(primary, -1);
						Supplier<Text> text = () -> Text.translatable("playerex.command.refund_attribute", Text.translatable(attr.getTranslationKey()), serverPlayerEntity.getName());
						ctx.getSource().sendFeedback(text, false);
						return 1;
					} else if(PacketType.REFUND.test(ctx.getSource().getServer(), serverPlayerEntity, playerData)) {
						playerData.add(primary, -1);
						Supplier<Text> text = () -> Text.translatable("playerex.command.refund_attribute", Text.translatable(attr.getTranslationKey()), serverPlayerEntity.getName());
						ctx.getSource().sendFeedback(text, false);
						return 1;
					} else {
						Supplier<Text> text = () -> Text.translatable("playerex.command.refund_attribute_error", serverPlayerEntity.getName()).formatted(Formatting.RED);
						ctx.getSource().sendFeedback(text, false);
						return -1;
					}
				} else {
					Supplier<Text> text = () -> Text.translatable("playerex.command.refund_attribute_unskilled", Text.translatable(attr.getTranslationKey()), serverPlayerEntity.getName()).formatted(Formatting.RED);
					ctx.getSource().sendFeedback(text, false);
					return -1;
				}
			});
		}).build();
		attribute.addChild(requiresRefundPoints);
	}
	
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
		LiteralCommandNode<ServerCommandSource> root = CommandManager.literal("playerex").requires(source -> source.hasPermissionLevel(2)).build();
		dispatcher.getRoot().addChild(root);
		
		registerReset(root);
		registerRefund(root);
		registerLevelUp(root);
		registerSkillAttribute(root);
		registerRefundAttribute(root);
	}
}
