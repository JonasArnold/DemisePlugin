package com.thejounezz.demisegame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StatusInformer 
{
	GameStateHandler gameState;
	
	public StatusInformer(GameStateHandler gameState)
	{
		this.gameState = gameState;
	}
	
	public void InformDeadpoolStatus(Game plugin, Player player, boolean listPlayers)
	{
		if(gameState.Get() == GameState.RUNNING)
		{
			player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + Globals.GameName + " Game is currently running!");
		}
		else
		{
			player.sendMessage(ChatColor.GOLD + Globals.GameName + " Game is currently not running.");
		}
		
		// list players only when flag set
		if(listPlayers)
		{
			player.sendMessage(ChatColor.GREEN + "Players alive:");
			for(String s : plugin.GetAlivePlayers())
			{
				player.sendMessage(ChatColor.GREEN + "- " + s);
			}
			player.sendMessage(ChatColor.RED + "Players dead:");
			for(String s : plugin.GetDeadPlayers())
			{
				player.sendMessage(ChatColor.RED + "- " + s);
			}
		}
	}
	
	public void BroadcastToAllPlayers(String message)
	{
		BroadcastToAllPlayers(message, true);
	}
	
	public void BroadcastToAllPlayers(String message, boolean boldAndGold)
	{
		if(boldAndGold)
		{
			Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + message);
		}
		else
		{
			Bukkit.broadcastMessage(message);
		}
		
	}

}
