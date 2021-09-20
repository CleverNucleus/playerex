package com.github.clevernucleus.playerex.handler;

import java.util.UUID;

import com.github.clevernucleus.dataattributes.api.attribute.IAttribute;
import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.ModifierData;
import com.github.clevernucleus.playerex.impl.PersistentPlayerCacheManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

public final class CommandsHandler {
	private static void resetCommand(CommandNode<ServerCommandSource> node) {
		LiteralCommandNode<ServerCommandSource> resetNode = CommandManager
				.literal("reset")
				.build();
		
		ArgumentCommandNode<ServerCommandSource, EntitySelector> playerNode = CommandManager
				.argument("player", EntityArgumentType.player())
				.executes(CommandsHandler::reset)
				.build();
		
		node.addChild(resetNode);
		resetNode.addChild(playerNode);
	}
	
	private static int reset(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		ModifierData modifierData = ExAPI.DATA.get(player);
		modifierData.reset();
		
		if(player.getHealth() > player.getMaxHealth()) {
			player.setHealth(player.getMaxHealth());
		}
		
		context.getSource().sendFeedback(new TranslatableText("command.playerex.reset", player.getName()), false);
		
		return 1;
	}
	
	private static void refundCommand(CommandNode<ServerCommandSource> node) {
		LiteralCommandNode<ServerCommandSource> refundNode = CommandManager
				.literal("refund")
				.build();
		
		ArgumentCommandNode<ServerCommandSource, EntitySelector> playerNode = CommandManager
				.argument("player", EntityArgumentType.player())
				.executes(CommandsHandler::refundAlt)
				.build();
		
		ArgumentCommandNode<ServerCommandSource, Integer> amountNode = CommandManager
				.argument("amount", IntegerArgumentType.integer(1))
				.executes(CommandsHandler::refund)
				.build();
		
		node.addChild(refundNode);
		refundNode.addChild(playerNode);
		playerNode.addChild(amountNode);
	}
	
	private static int refundAlt(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		ModifierData modifierData = ExAPI.DATA.get(player);
		int refunded = modifierData.addRefundPoints(1);
		
		if(refunded == 1) {
			context.getSource().sendFeedback(new TranslatableText("command.playerex.refund_alt", player.getName()), false);
		} else {
			context.getSource().sendFeedback(new TranslatableText("command.playerex.refund", refunded, player.getName()), false);
		}
		
		return refunded;
	}
	
	private static int refund(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		ModifierData modifierData = ExAPI.DATA.get(player);
		int amount = IntegerArgumentType.getInteger(context, "amount");
		int refunded = modifierData.addRefundPoints(amount);
		
		if(refunded == 1) {
			context.getSource().sendFeedback(new TranslatableText("command.playerex.refund_alt", player.getName()), false);
		} else {
			context.getSource().sendFeedback(new TranslatableText("command.playerex.refund", refunded, player.getName()), false);
		}
		
		return refunded;
	}
	
	private static void levelUpCommand(CommandNode<ServerCommandSource> node) {
		LiteralCommandNode<ServerCommandSource> levelUpNode = CommandManager
				.literal("levelup")
				.build();
		
		ArgumentCommandNode<ServerCommandSource, EntitySelector> playerNode = CommandManager
				.argument("player", EntityArgumentType.player())
				.executes(CommandsHandler::levelUpAlt)
				.build();
		
		ArgumentCommandNode<ServerCommandSource, Integer> amountNode = CommandManager
				.argument("amount", IntegerArgumentType.integer(1))
				.executes(CommandsHandler::levelUp)
				.build();
		
		node.addChild(levelUpNode);
		levelUpNode.addChild(playerNode);
		playerNode.addChild(amountNode);
	}
	
	private static int levelUpAlt(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		ModifierData modifierData = ExAPI.DATA.get(player);
		EntityAttribute attribute = ExAPI.LEVEL.get();
		AttributeContainer container = player.getAttributes();
		
		if(attribute == null || !container.hasAttribute(attribute)) return -1;
		
		IAttribute instance = (IAttribute)attribute;
		int max = (int)Math.round(instance.getMaxValue());
		int current = (int)Math.round(container.getValue(attribute));
		int ceiling = max - current;
		
		if(ceiling < 1) {
			context.getSource().sendFeedback((new TranslatableText("command.playerex.levelup_max_error", player.getName()).formatted(Formatting.RED)), false);
			
			return -1;
		}
		
		modifierData.add(attribute, 1);
		context.getSource().sendFeedback(new TranslatableText("command.playerex.levelup_alt", player.getName()), false);
		
		return 1;
	}
	
	private static int levelUp(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		ModifierData modifierData = ExAPI.DATA.get(player);
		EntityAttribute attribute = ExAPI.LEVEL.get();
		AttributeContainer container = player.getAttributes();
		
		if(attribute == null || !container.hasAttribute(attribute)) return -1;
		
		IAttribute instance = (IAttribute)attribute;
		int amount = IntegerArgumentType.getInteger(context, "amount");
		int max = (int)Math.round(instance.getMaxValue());
		int current = (int)Math.round(container.getValue(attribute));
		int ceiling = max - current;
		int value = MathHelper.clamp(amount, 1, ceiling);
		
		if(ceiling < 1) {
			context.getSource().sendFeedback((new TranslatableText("command.playerex.levelup_max_error", player.getName()).formatted(Formatting.RED)), false);
			
			return -1;
		}
		
		modifierData.add(attribute, value);
		context.getSource().sendFeedback(new TranslatableText("command.playerex.levelup", value, player.getName()), false);
		
		return value % 16;
	}
	
	private static void cacheCommand(CommandNode<ServerCommandSource> node) {
		LiteralCommandNode<ServerCommandSource> cacheNode = CommandManager
				.literal("cache")
				.build();
		
		LiteralCommandNode<ServerCommandSource> clearNode = CommandManager
				.literal("clear")
				.executes(CommandsHandler::clearCache)
				.build();
		
		LiteralCommandNode<ServerCommandSource> removeNode = CommandManager
				.literal("remove")
				.build();
		
		ArgumentCommandNode<ServerCommandSource, UUID> uuidNode = CommandManager
				.argument("uuid", UuidArgumentType.uuid())
				.executes(CommandsHandler::removeFromCache)
				.build();
		
		node.addChild(cacheNode);
		cacheNode.addChild(clearNode);
		cacheNode.addChild(removeNode);
		removeNode.addChild(uuidNode);
	}
	
	private static int clearCache(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		MinecraftServer server = context.getSource().getServer();
		PersistentPlayerCacheManager cache = (PersistentPlayerCacheManager)ExAPI.persistentPlayerCache(server);
		cache.clear();
		
		context.getSource().sendFeedback(new TranslatableText("command.playerex.clear_cache"), false);
		
		return 1;
	}
	
	private static int removeFromCache(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		UUID uuid = UuidArgumentType.getUuid(context, "uuid");
		MinecraftServer server = context.getSource().getServer();
		PersistentPlayerCacheManager cache = (PersistentPlayerCacheManager)ExAPI.persistentPlayerCache(server);
		cache.removePlayer(uuid);
		
		context.getSource().sendFeedback(new TranslatableText("command.playerex.remove_from_cache", uuid), false);
		
		return 1;
	}
	
	public static void init(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
		LiteralCommandNode<ServerCommandSource> node = CommandManager
				.literal("playerex")
				.requires(source -> source.hasPermissionLevel(2))
				.build();
		
		dispatcher.getRoot().addChild(node);
		
		resetCommand(node);
		refundCommand(node);
		levelUpCommand(node);
		cacheCommand(node);
	}
}
