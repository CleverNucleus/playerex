package com.github.clevernucleus.playerex.impl;

import java.util.UUID;
import java.util.function.Function;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.PlayerAttributes;
import com.github.clevernucleus.playerex.api.attribute.AttributeData;
import com.github.clevernucleus.playerex.api.attribute.AttributeType;
import com.github.clevernucleus.playerex.api.attribute.IPlayerAttribute;
import com.github.clevernucleus.playerex.api.util.Maths;
import com.github.clevernucleus.playerex.impl.attribute.AttributeDataManager;
import com.github.clevernucleus.playerex.impl.attribute.PlayerAttribute;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public final class CommandsImpl {
	private static final SuggestionProvider<ServerCommandSource> PLAYER_SUGGESTIONS = (context, builder) -> {
		PlayerManager players = context.getSource().getMinecraftServer().getPlayerManager();
		
		players.getPlayerList().stream().map(player -> player.getGameProfile().getName()).forEach(builder::suggest);
		
		return builder.buildFuture();
	};
	private static final SuggestionProvider<ServerCommandSource> ATTRIBUTE_SUGGESTIONS = (context, builder) -> {
		ExAPI.REGISTRY.get().attributes()
		.entrySet()
		.stream()
		.filter(entry -> entry.getValue().type().equals(AttributeType.GAME))
		.map(entry -> entry.getKey().toString())
		.forEach(builder::suggest);
		
		return builder.buildFuture();
	};
	
	public static void init(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
		LiteralCommandNode<ServerCommandSource> node = CommandManager
				.literal("playerex")
				.requires(source -> source.hasPermissionLevel(2))
				.build();
		
		dispatcher.getRoot().addChild(node);
		
		resetCommand(node);
		refundCommand(node);
		levelUpCommand(node);
		attributeCommand(node);
	}
	
	private static void resetCommand(CommandNode<ServerCommandSource> node) {
		LiteralCommandNode<ServerCommandSource> resetNode = CommandManager
				.literal("reset")
				.build();
		
		ArgumentCommandNode<ServerCommandSource, EntitySelector> playerNode = CommandManager
				.argument("player", EntityArgumentType.player())
				.suggests(PLAYER_SUGGESTIONS)
				.executes(CommandsImpl::reset)
				.build();
		
		node.addChild(resetNode);
		resetNode.addChild(playerNode);
	}
	
	private static void refundCommand(CommandNode<ServerCommandSource> node) {
		LiteralCommandNode<ServerCommandSource> refundNode = CommandManager
				.literal("refund")
				.build();
		
		ArgumentCommandNode<ServerCommandSource, Integer> amountNode = CommandManager
				.argument("amount", IntegerArgumentType.integer(1))
				.build();
		
		Function<Command<ServerCommandSource>, ArgumentCommandNode<ServerCommandSource, EntitySelector>> playerNode = command -> {
			return CommandManager
					.argument("player", EntityArgumentType.player())
					.suggests(PLAYER_SUGGESTIONS)
					.executes(command)
					.build();
		};
		
		node.addChild(refundNode);
		refundNode.addChild(playerNode.apply(CommandsImpl::refund));
		refundNode.addChild(amountNode);
		amountNode.addChild(playerNode.apply(CommandsImpl::refundAlt));
	}
	
	private static void levelUpCommand(CommandNode<ServerCommandSource> node) {
		LiteralCommandNode<ServerCommandSource> resetNode = CommandManager
				.literal("levelup")
				.build();
		
		ArgumentCommandNode<ServerCommandSource, Integer> amountNode = CommandManager
				.argument("amount", IntegerArgumentType.integer(1))
				.build();
		
		Function<Command<ServerCommandSource>, ArgumentCommandNode<ServerCommandSource, EntitySelector>> playerNode = command -> {
			return CommandManager
					.argument("player", EntityArgumentType.player())
					.suggests(PLAYER_SUGGESTIONS)
					.executes(command)
					.build();
		};
		
		node.addChild(resetNode);
		resetNode.addChild(playerNode.apply(CommandsImpl::levelUp));
		resetNode.addChild(amountNode);
		amountNode.addChild(playerNode.apply(CommandsImpl::levelUpAlt));
	}
	
	private static void attributeCommand(CommandNode<ServerCommandSource> node) {
		LiteralCommandNode<ServerCommandSource> attributeNode = CommandManager
				.literal("attribute")
				.build();
		
		ArgumentCommandNode<ServerCommandSource, EntitySelector> playerNode = CommandManager
				.argument("player", EntityArgumentType.player())
				.suggests(PLAYER_SUGGESTIONS)
				.build();
		
		ArgumentCommandNode<ServerCommandSource, Identifier> attributeIdNode = CommandManager
				.argument("attribute", IdentifierArgumentType.identifier())
				.suggests(ATTRIBUTE_SUGGESTIONS)
				.build();
		
		LiteralCommandNode<ServerCommandSource> attributeGetNode = CommandManager
				.literal("get")
				.executes(CommandsImpl::attributeGet)
				.build();
		
		LiteralCommandNode<ServerCommandSource> attributeSetNode = CommandManager
				.literal("set")
				.build();
		
		LiteralCommandNode<ServerCommandSource> attributeAddNode = CommandManager
				.literal("add")
				.build();
		
		Function<Command<ServerCommandSource>, ArgumentCommandNode<ServerCommandSource, Double>> attributeValueNode = command -> {
			return CommandManager.argument("value", DoubleArgumentType.doubleArg()).executes(command).build();
		};
		
		LiteralCommandNode<ServerCommandSource> attributeModifierNode = CommandManager
				.literal("modifier")
				.build();
		
		LiteralCommandNode<ServerCommandSource> modifierAddNode = CommandManager
				.literal("add")
				.build();
		
		ArgumentCommandNode<ServerCommandSource, UUID> modifierAddUUIDNode = CommandManager
				.argument("uuid", UuidArgumentType.uuid())
				.build();
		
		ArgumentCommandNode<ServerCommandSource, String> modifierAddNameNode = CommandManager
				.argument("name", StringArgumentType.word())
				.build();
		
		ArgumentCommandNode<ServerCommandSource, Double> modifierAddValueNode = CommandManager
				.argument("value", DoubleArgumentType.doubleArg())
				.build();
		
		LiteralCommandNode<ServerCommandSource> modifierAddAddNode = CommandManager
				.literal("add")
				.executes(context -> modifierAdd(context, 0))
				.build();
		
		LiteralCommandNode<ServerCommandSource> modifierAddMultiplyNode = CommandManager
				.literal("multiply")
				.executes(context -> modifierAdd(context, 2))
				.build();
		
		LiteralCommandNode<ServerCommandSource> modifierAddMultiplyBaseNode = CommandManager
				.literal("multiply_base")
				.executes(context -> modifierAdd(context, 1))
				.build();
		
		LiteralCommandNode<ServerCommandSource> modifierRemoveNode = CommandManager
				.literal("remove")
				.build();
		
		ArgumentCommandNode<ServerCommandSource, UUID> modifierRemoveUUIDNode = CommandManager
				.argument("uuid", UuidArgumentType.uuid())
				.build();
		
		ArgumentCommandNode<ServerCommandSource, String> modifierRemoveNameNode = CommandManager
				.argument("name", StringArgumentType.word())
				.build();
		
		ArgumentCommandNode<ServerCommandSource, Double> modifierRemoveValueNode = CommandManager
				.argument("value", DoubleArgumentType.doubleArg())
				.build();
		
		LiteralCommandNode<ServerCommandSource> modifierRemoveAddNode = CommandManager
				.literal("add")
				.executes(context -> modifierRemove(context, 0))
				.build();
		
		LiteralCommandNode<ServerCommandSource> modifierRemoveMultiplyNode = CommandManager
				.literal("multiply")
				.executes(context -> modifierRemove(context, 2))
				.build();
		
		LiteralCommandNode<ServerCommandSource> modifierRemoveMultiplyBaseNode = CommandManager
				.literal("multiply_base")
				.executes(context -> modifierRemove(context, 1))
				.build();
		
		LiteralCommandNode<ServerCommandSource> modifierValueNode = CommandManager
				.literal("value")
				.build();
		
		LiteralCommandNode<ServerCommandSource> modifierGetNode = CommandManager
				.literal("get")
				.build();
		
		ArgumentCommandNode<ServerCommandSource, UUID> modifierGetUUIDNode = CommandManager
				.argument("uuid", UuidArgumentType.uuid())
				.executes(CommandsImpl::modifierGet)
				.build();
		
		node.addChild(attributeNode);
		attributeNode.addChild(playerNode);
		playerNode.addChild(attributeIdNode);
		attributeIdNode.addChild(attributeGetNode);
		
		attributeIdNode.addChild(attributeSetNode);
		attributeSetNode.addChild(attributeValueNode.apply(CommandsImpl::attributeSet));
		
		attributeIdNode.addChild(attributeAddNode);
		attributeAddNode.addChild(attributeValueNode.apply(CommandsImpl::attributeAdd));
		
		attributeIdNode.addChild(attributeModifierNode);
		attributeModifierNode.addChild(modifierAddNode);
		modifierAddNode.addChild(modifierAddUUIDNode);
		modifierAddUUIDNode.addChild(modifierAddNameNode);
		modifierAddNameNode.addChild(modifierAddValueNode);
		modifierAddValueNode.addChild(modifierAddAddNode);
		modifierAddValueNode.addChild(modifierAddMultiplyNode);
		modifierAddValueNode.addChild(modifierAddMultiplyBaseNode);
		
		attributeModifierNode.addChild(modifierRemoveNode);
		modifierRemoveNode.addChild(modifierRemoveUUIDNode);
		modifierRemoveUUIDNode.addChild(modifierRemoveNameNode);
		modifierRemoveNameNode.addChild(modifierRemoveValueNode);
		modifierRemoveValueNode.addChild(modifierRemoveAddNode);
		modifierRemoveValueNode.addChild(modifierRemoveMultiplyNode);
		modifierRemoveValueNode.addChild(modifierRemoveMultiplyBaseNode);
		
		attributeModifierNode.addChild(modifierValueNode);
		modifierValueNode.addChild(modifierGetNode);
		modifierGetNode.addChild(modifierGetUUIDNode);
	}
	
	private static int reset(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		AttributeDataManager data = (AttributeDataManager)ExAPI.DATA.get(player);
		
		data.reset();
		
		if(player.getHealth() > player.getMaxHealth()) {
			player.setHealth(player.getMaxHealth());
		}
		
		ExAPI.DATA.sync(player);
		context.getSource().sendFeedback((new TranslatableText("command.playerex.reset")).formatted(Formatting.GRAY, Formatting.ITALIC), false);
		
		return 1;
	}
	
	private static int refund(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		AttributeData data = ExAPI.DATA.get(player);
		int amount = 1;
		int refunded = data.addRefundPoints(amount);
		boolean error = refunded <= 0;
		String key = "refund" + (error ? ".error" : "");
		Formatting[] formatting = error ? new Formatting[] {Formatting.RED} : new Formatting[] {Formatting.GRAY, Formatting.ITALIC};
		context.getSource().sendFeedback((new TranslatableText("command.playerex." + key, refunded)).formatted(formatting), false);
		
		return error ? -1 : refunded % 16;
	}
	
	private static int refundAlt(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		AttributeData data = ExAPI.DATA.get(player);
		int amount = IntegerArgumentType.getInteger(context, "amount");
		int refunded = data.addRefundPoints(amount);
		boolean error = refunded <= 0;
		String key = "refund" + (error ? ".error" : "");
		Formatting[] formatting = error ? new Formatting[] {Formatting.RED} : new Formatting[] {Formatting.GRAY, Formatting.ITALIC};
		context.getSource().sendFeedback((new TranslatableText("command.playerex." + key, refunded)).formatted(formatting), false);
		
		return error ? -1 : refunded % 16;
	}
	
	private static int levelUp(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		AttributeData data = ExAPI.DATA.get(player);
		int amount = 1;
		
		data.add(PlayerAttributes.LEVEL.get(), (double)amount);
		context.getSource().sendFeedback((new TranslatableText("command.playerex.levelup")).formatted(Formatting.GRAY, Formatting.ITALIC), false);
		
		return amount;
	}
	
	private static int levelUpAlt(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		AttributeData data = ExAPI.DATA.get(player);
		int amount = IntegerArgumentType.getInteger(context, "amount");
		
		data.add(PlayerAttributes.LEVEL.get(), (double)amount);
		context.getSource().sendFeedback((new TranslatableText("command.playerex.levelup.alt", amount)).formatted(Formatting.GRAY, Formatting.ITALIC), false);
		
		return amount % 16;
	}
	
	private static int attributeGet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		AttributeData data = ExAPI.DATA.get(player);
		Identifier id = IdentifierArgumentType.getIdentifier(context, "attribute");
		IPlayerAttribute attribute = ExAPI.REGISTRY.get().getAttribute(id);
		
		if(attribute == null || attribute.type().equals(AttributeType.DATA)) {
			context.getSource().sendFeedback((new TranslatableText("command.playerex.attribute.error", id)).formatted(Formatting.RED), false);
			
			return -1;
		}
		
		double value = data.get(attribute);
		
		context.getSource().sendFeedback((new TranslatableText("command.playerex.attribute.get", value)).formatted(Formatting.GRAY, Formatting.ITALIC), false);
		
		double valueOut = Maths.positive(value);
		
		return Math.round((float)valueOut) % 16;
	}
	
	private static int attributeSet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		AttributeData data = ExAPI.DATA.get(player);
		Identifier id = IdentifierArgumentType.getIdentifier(context, "attribute");
		IPlayerAttribute attribute = ExAPI.REGISTRY.get().getAttribute(id);
		
		if(attribute == null || attribute.type().equals(AttributeType.DATA)) {
			context.getSource().sendFeedback((new TranslatableText("command.playerex.attribute.error", id)).formatted(Formatting.RED), false);
			
			return -1;
		}
		
		double value = DoubleArgumentType.getDouble(context, "value");
		
		data.set(attribute, value);
		context.getSource().sendFeedback((new TranslatableText("command.playerex.attribute.set", value)).formatted(Formatting.GRAY, Formatting.ITALIC), false);
		
		double valueOut = Maths.positive(value);
		
		return Math.round((float)valueOut) % 16;
	}
	
	private static int attributeAdd(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		AttributeData data = ExAPI.DATA.get(player);
		Identifier id = IdentifierArgumentType.getIdentifier(context, "attribute");
		IPlayerAttribute attribute = ExAPI.REGISTRY.get().getAttribute(id);
		
		if(attribute == null || attribute.type().equals(AttributeType.DATA)) {
			context.getSource().sendFeedback((new TranslatableText("command.playerex.attribute.error", id)).formatted(Formatting.RED), false);
			
			return -1;
		}
		
		double value = DoubleArgumentType.getDouble(context, "value");
		
		data.add(attribute, value);
		context.getSource().sendFeedback((new TranslatableText("command.playerex.attribute.add", value)).formatted(Formatting.GRAY, Formatting.ITALIC), false);
		
		double valueOut = Maths.positive(value);
		
		return Math.round((float)valueOut) % 16;
	}
	
	private static int modifierAdd(CommandContext<ServerCommandSource> context, int type) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		AttributeData data = ExAPI.DATA.get(player);
		Identifier id = IdentifierArgumentType.getIdentifier(context, "attribute");
		IPlayerAttribute attribute = ExAPI.REGISTRY.get().getAttribute(id);
		
		if(attribute == null || attribute.type().equals(AttributeType.DATA)) {
			context.getSource().sendFeedback((new TranslatableText("command.playerex.attribute.error", id)).formatted(Formatting.RED), false);
			
			return -1;
		}
		
		UUID uuid = UuidArgumentType.getUuid(context, "uuid");
		
		EntityAttributeInstance instance = player.getAttributeInstance(((PlayerAttribute)attribute).get());
		String name = StringArgumentType.getString(context, "name");
		double value = DoubleArgumentType.getDouble(context, "value");
		EntityAttributeModifier.Operation operation = EntityAttributeModifier.Operation.fromId(type);
		EntityAttributeModifier modifier = new EntityAttributeModifier(uuid, name, value, operation);
		
		if(instance.hasModifier(modifier)) {
			context.getSource().sendFeedback((new TranslatableText("command.playerex.modifier.already_present")).formatted(Formatting.RED), false);
			
			return -1;
		}
		
		data.applyAttributeModifier(attribute, modifier);
		context.getSource().sendFeedback((new TranslatableText("command.playerex.modifier.add")).formatted(Formatting.GRAY, Formatting.ITALIC), false);
		
		return 1;
	}
	
	private static int modifierRemove(CommandContext<ServerCommandSource> context, int type) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		AttributeData data = ExAPI.DATA.get(player);
		Identifier id = IdentifierArgumentType.getIdentifier(context, "attribute");
		IPlayerAttribute attribute = ExAPI.REGISTRY.get().getAttribute(id);
		
		if(attribute == null || attribute.type().equals(AttributeType.DATA)) {
			context.getSource().sendFeedback((new TranslatableText("command.playerex.attribute.error", id)).formatted(Formatting.RED), false);
			
			return -1;
		}
		
		UUID uuid = UuidArgumentType.getUuid(context, "uuid");
		
		EntityAttributeInstance instance = player.getAttributeInstance(((PlayerAttribute)attribute).get());
		String name = StringArgumentType.getString(context, "name");
		double value = DoubleArgumentType.getDouble(context, "value");
		EntityAttributeModifier.Operation operation = EntityAttributeModifier.Operation.fromId(type);
		EntityAttributeModifier modifier = new EntityAttributeModifier(uuid, name, value, operation);
		
		if(!instance.hasModifier(modifier)) {
			context.getSource().sendFeedback((new TranslatableText("command.playerex.modifier.error")).formatted(Formatting.RED), false);
			
			return -1;
		}
		
		data.removeAttributeModifier(attribute, modifier);
		context.getSource().sendFeedback((new TranslatableText("command.playerex.modifier.remove")).formatted(Formatting.GRAY, Formatting.ITALIC), false);
		
		return 1;
	}
	
	private static int modifierGet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		Identifier id = IdentifierArgumentType.getIdentifier(context, "attribute");
		IPlayerAttribute attribute = ExAPI.REGISTRY.get().getAttribute(id);
		
		if(attribute == null || attribute.type().equals(AttributeType.DATA)) {
			context.getSource().sendFeedback((new TranslatableText("command.playerex.attribute.error", id)).formatted(Formatting.RED), false);
			
			return -1;
		}
		
		UUID uuid = UuidArgumentType.getUuid(context, "uuid");
		
		EntityAttributeInstance instance = player.getAttributeInstance(((PlayerAttribute)attribute).get());
		EntityAttributeModifier modifier = instance.getModifier(uuid);
		
		if(modifier == null) {
			context.getSource().sendFeedback((new TranslatableText("command.playerex.modifier.error")).formatted(Formatting.RED), false);
			
			return -1;
		}
		
		double value = modifier.getValue();
		
		context.getSource().sendFeedback((new TranslatableText("command.playerex.modifier.get", value)).formatted(Formatting.GRAY, Formatting.ITALIC), false);
		
		double valueOut = Maths.positive(value);
		
		return Math.round((float)valueOut) % 16;
	}
}
