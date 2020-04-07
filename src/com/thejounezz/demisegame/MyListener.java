package com.thejounezz.demisegame;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class MyListener implements Listener 
{
	Game plugin;
	StatusInformer statusInformer;
	ScoreboardHandler scoreboardHandler;
	
	public MyListener(Game plugin, StatusInformer statusInformer, ScoreboardHandler sideboardGenerator)
	{
		this.plugin = plugin;
		this.statusInformer = statusInformer;
		this.scoreboardHandler = sideboardGenerator;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// Send info about players
		this.statusInformer.InformGameStatus(plugin, event.getPlayer(), false);
		
		// create sideboard
		this.scoreboardHandler.GenerateNewScoreboard(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		plugin.EliminatePlayer(event.getEntity());		
	}
}
