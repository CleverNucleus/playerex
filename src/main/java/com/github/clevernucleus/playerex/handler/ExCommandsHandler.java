package com.github.clevernucleus.playerex.handler;

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI;
import com.github.clevernucleus.dataattributes.api.attribute.IEntityAttribute;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

public final class ExCommandsHandler {
	private static void registerReset(CommandNode<ServerCommandSource> root) {
		LiteralCommandNode<ServerCommandSource> reset = CommandManager.literal("reset").build();
		root.addChild(reset);
		
		ArgumentCommandNode<ServerCommandSource, EntitySelector> player = CommandManager.argument("player", EntityArgumentType.player()).executes(ctx -> {
			ServerPlayerEntity serverPlayerEntity = EntityArgumentType.getPlayer(ctx, "player");
			PlayerData playerData = ExAPI.INSTANCE.get(serverPlayerEntity);
			playerData.reset();
			
			ctx.getSource().sendFeedback(new TranslatableText("playerex.command.reset", serverPlayerEntity.getName()), false);
			
			return 1;
		}).build();
		reset.addChild(player);
	}
	
	private static void registerRefund(CommandNode<ServerCommandSource> root) {
		LiteralCommandNode<ServerCommandSource> refund = CommandManager.literal("refund").build();
		root.addChild(refund);
		
		ArgumentCommandNode<ServerCommandSource, EntitySelector> player = CommandManager.argument("player", EntityArgumentType.player()).executes(ctx -> {
			ServerPlayerEntity serverPlayerEntity = EntityArgumentType.getPlayer(ctx, "player");
			PlayerData playerData = ExAPI.INSTANCE.get(serverPlayerEntity);
			int refunded = playerData.addRefundPoints(1);
			
			if(refunded == 1) {
				ctx.getSource().sendFeedback(new TranslatableText("playerex.command.refund_alt", serverPlayerEntity.getName()), false);
			} else {
				ctx.getSource().sendFeedback(new TranslatableText("playerex.command.refund", refunded, serverPlayerEntity.getName()), false);
			}
			
			return refunded % 16;
		}).build();
		refund.addChild(player);
		
		ArgumentCommandNode<ServerCommandSource, Integer> amount = CommandManager.argument("amount", IntegerArgumentType.integer(1)).executes(ctx -> {
			ServerPlayerEntity serverPlayerEntity = EntityArgumentType.getPlayer(ctx, "player");
			PlayerData playerData = ExAPI.INSTANCE.get(serverPlayerEntity);
			int value = IntegerArgumentType.getInteger(ctx, "amount");
			int refunded = playerData.addRefundPoints(value);
			
			if(refunded == 1) {
				ctx.getSource().sendFeedback(new TranslatableText("playerex.command.refund_alt", serverPlayerEntity.getName()), false);
			} else {
				ctx.getSource().sendFeedback(new TranslatableText("playerex.command.refund", refunded, serverPlayerEntity.getName()), false);
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
			PlayerData playerData = ExAPI.INSTANCE.get(serverPlayerEntity);
			return DataAttributesAPI.ifPresent(serverPlayerEntity, ExAPI.LEVEL, -1, value -> {
				IEntityAttribute attribute = (IEntityAttribute)ExAPI.LEVEL.get();
				
				if(attribute.maxValue() - value < 1) {
					ctx.getSource().sendFeedback((new TranslatableText("playerex.command.levelup_max_error", serverPlayerEntity.getName())).formatted(Formatting.RED), false);
					return -1;
				}
				
				playerData.add(ExAPI.LEVEL.get(), 1);
				playerData.addSkillPoints(ExAPI.getConfig().skillPointsPerLevelUp());
				ctx.getSource().sendFeedback(new TranslatableText("playerex.command.levelup_alt", serverPlayerEntity.getName()), false);
				return 1;
			});
		}).build();
		levelup.addChild(player);
		
		ArgumentCommandNode<ServerCommandSource, Integer> amount = CommandManager.argument("amount", IntegerArgumentType.integer(1)).executes(ctx -> {
			ServerPlayerEntity serverPlayerEntity = EntityArgumentType.getPlayer(ctx, "player");
			PlayerData playerData = ExAPI.INSTANCE.get(serverPlayerEntity);
			int levels = IntegerArgumentType.getInteger(ctx, "amount");
			return DataAttributesAPI.ifPresent(serverPlayerEntity, ExAPI.LEVEL, -1, value -> {
				IEntityAttribute attribute = (IEntityAttribute)ExAPI.LEVEL.get();
				int max = Math.round((float)attribute.maxValue() - value);
				
				if(max < 1) {
					ctx.getSource().sendFeedback((new TranslatableText("playerex.command.levelup_max_error", serverPlayerEntity.getName())).formatted(Formatting.RED), false);
					return -1;
				}
				
				int adding = MathHelper.clamp(levels, 1, max);
				playerData.add(ExAPI.LEVEL.get(), adding);
				playerData.addSkillPoints(adding * ExAPI.getConfig().skillPointsPerLevelUp());
				
				if(adding == 1) {
					ctx.getSource().sendFeedback(new TranslatableText("playerex.command.levelup_alt", serverPlayerEntity.getName()), false);
				} else {
					ctx.getSource().sendFeedback(new TranslatableText("playerex.command.levelup", adding, serverPlayerEntity.getName()), false);
				}
				
				return adding % 16;
			});
		}).build();
		player.addChild(amount);
	}
	
	public static void init(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
		LiteralCommandNode<ServerCommandSource> root = CommandManager.literal("playerex").requires(source -> source.hasPermissionLevel(2)).build();
		dispatcher.getRoot().addChild(root);
		
		registerReset(root);
		registerRefund(root);
		registerLevelUp(root);
	}
}
