package com.github.clevernucleus.playerex.init;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.github.clevernucleus.playerex.api.attribute.PlayerAttributes;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class LevelCommand {

	/** Error procedure. */
	private static boolean error(final CommandContext<CommandSource> par0, final PlayerEntity par1) {
		if(par1.world.isRemote) return true;
		
		PlayerList var0 = par0.getSource().getServer().getPlayerList();
		
		if(!var0.getPlayers().contains(par1)) {
			par0.getSource().sendFeedback(new StringTextComponent(TextFormatting.RED + par1.getDisplayName().toString() + TextFormatting.GRAY + " does not exist!"), true);
			
			return true;
		}
		
		return false;
	}
	
	private static int level(CommandContext<CommandSource> par0, PlayerEntity par1, int par2) throws CommandSyntaxException {
		if(error(par0, par1)) return 1;
		
		ExAPI.playerAttributes(par1).ifPresent(var -> {
			var.add(par1, PlayerAttributes.LEVEL, par2);
		});
		EventHandler.update(par1);
		EventHandler.sync(par1);
		
		return 1;
	}
	
	/**
	 * Register the command(s) to the game.
	 * @param par0
	 */
	public static void register(CommandDispatcher<CommandSource> par0) {
		par0.register(Commands.literal(ExAPI.MODID).requires(var -> var.hasPermissionLevel(2)).then(Commands.literal("level-add").then(Commands.argument("player", EntityArgument.player()).suggests((var0, var1) -> {
			PlayerList var2 = var0.getSource().getServer().getPlayerList();
			
			return ISuggestionProvider.suggest(var2.getPlayers().stream().map(var3 -> var3.getGameProfile().getName()), var1);
		}).then(Commands.argument("amount", IntegerArgumentType.integer()).executes(var -> {
			return level(var, EntityArgument.getPlayer(var, "player"), IntegerArgumentType.getInteger(var, "amount"));
		})))));
	}
}
