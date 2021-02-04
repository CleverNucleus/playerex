package com.github.clevernucleus.playerex.init;

import com.github.clevernucleus.playerex.api.ExAPI;
import com.mojang.brigadier.CommandDispatcher;
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

public class ResetCommand {
	
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
	
	/** A partially formed entitled command. */
	private static int run(CommandContext<CommandSource> par0) throws CommandSyntaxException {
        par0.getSource().sendFeedback(new StringTextComponent(TextFormatting.RED + "/playerex player reset"), true);
        
        return 1;
    }
	
	private static int reset(CommandContext<CommandSource> par0, PlayerEntity par1) throws CommandSyntaxException {
		if(error(par0, par1)) return 1;
		
		EventHandler.reset(par1, true);
		EventHandler.update(par1);
		EventHandler.sync(par1);
		
		return 1;
	}
	
	/**
	 * Register the command(s) to the game.
	 * @param par0
	 */
	public static void register(CommandDispatcher<CommandSource> par0) {
		par0.register(Commands.literal(ExAPI.MODID)
				.requires(var -> var.hasPermissionLevel(2))
				.executes(ResetCommand::run).then(Commands.argument("player", EntityArgument.player()).suggests((var0, var1) -> {
					PlayerList var2 = var0.getSource().getServer().getPlayerList();
					
					return ISuggestionProvider.suggest(var2.getPlayers().stream().map(var3 -> var3.getGameProfile().getName()), var1);
				}).then(Commands.literal("reset").executes(var -> reset(var, EntityArgument.getPlayer(var, "player"))))));
	}
}
