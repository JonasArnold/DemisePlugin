package com.server.deadpoolgame;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class MyListener implements Listener 
{
	Main plugin;
	StatusInformer statusInformer;
	ScoreboardHandler sideboardGenerator;
	
	public MyListener(Main plugin, StatusInformer statusInformer, ScoreboardHandler sideboardGenerator)
	{
		this.plugin = plugin;
		this.statusInformer = statusInformer;
		this.sideboardGenerator = sideboardGenerator;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// Send info about players
		this.statusInformer.InformDeadpoolStatus(plugin, event.getPlayer(), false);
		
		// create sideboard
		this.sideboardGenerator.GenerateNewScoreboard(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		plugin.EliminatePlayer(event.getEntity());		
	}
}
