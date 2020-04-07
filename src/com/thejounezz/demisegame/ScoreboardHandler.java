package com.thejounezz.demisegame;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import net.md_5.bungee.api.ChatColor;

public class ScoreboardHandler 
{
	Game plugin;
	GameStateHandler gameState;
	Scoreboard b;
	String objectiveName = Globals.GameName + "Game";
	
	public ScoreboardHandler(Game plugin, GameStateHandler gameState)
	{
		this.plugin = plugin;
		this.gameState = gameState;
		ScoreboardManager m = Bukkit.getScoreboardManager();
		b = m.getNewScoreboard();
		this.CreateTeams();
	}

	public Scoreboard GetScoreboard()
	{
		return b;
	}
	
	public void GenerateNewScoreboard(Player sender)
	{
		ScoreboardManager m = Bukkit.getScoreboardManager();
		b = m.getNewScoreboard();
		
		@SuppressWarnings("deprecation")
		Objective o = b.registerNewObjective(objectiveName, "");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		o.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + Globals.GameName + " Game");	
		
		// Bottom up lines
		int i = 0;
		
		for(String p : plugin.GetDeadPlayers())
		{
			Score dead = o.getScore(ChatColor.WHITE + "- " + p);
			dead.setScore(i++);
		}
		Score deadTitle = o.getScore(ChatColor.RED + "Players Dead:");
		deadTitle.setScore(i++);

		for(String p : plugin.GetAlivePlayers())
		{
			Score alive = o.getScore(ChatColor.WHITE + "- " + p);
			alive.setScore(i++);
		}
		Score aliveTitle = o.getScore(ChatColor.GREEN + "Players Alive:");
		aliveTitle.setScore(i++);
		
		Score empty = o.getScore("");
		empty.setScore(i++);
		
		Score gameStateScore;
		if(gameState.Get() == GameState.RUNNING)
		{
			gameStateScore = o.getScore(ChatColor.GOLD + "Game: " + ChatColor.BOLD + "RUNNING");
		}
		else
		{
			gameStateScore = o.getScore(ChatColor.GOLD + "Game: Not running");
		}
		gameStateScore.setScore(i++);
		
		Score empty2 = o.getScore("  ");
		empty2.setScore(i++);
		
		// Set Tab List names
		for(Player p : Bukkit.getOnlinePlayers())   // reset
		{
			ResetName(p);
		}
		for(String s : plugin.GetAlivePlayers())
		{
			Player p = Bukkit.getPlayer(s);
			p.setDisplayName(ChatColor.GREEN + "[Alive] " + ChatColor.RESET + p.getName());
			p.setPlayerListName(p.getDisplayName());
		}
		
		for(String s : plugin.GetDeadPlayers())
		{
			Player p = Bukkit.getPlayer(s);
			p.setDisplayName(ChatColor.RED + "[Dead] " + ChatColor.RESET + p.getName());
			p.setPlayerListName(p.getDisplayName());
		}
	
		sender.setScoreboard(b);
	}
	
	public void ClearScoreboard(Player sender)
	{
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
			public void run() {
			sender.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			}
			}, 20L);
		
		// reset player names
		for(Player p : Bukkit.getOnlinePlayers())
		{
			p.setPlayerListName(p.getName());
		}
	}
	
	private void CreateTeams() 
	{
		if(this.b.getTeam(Globals.TeamAliveName) == null)
		{		
			this.b.registerNewTeam(Globals.TeamAliveName);
			Bukkit.getLogger().info("Team " + Globals.TeamAliveName + " was created");
		}
		if(b.getTeam(Globals.TeamDeadName) == null)
		{
			this.b.registerNewTeam(Globals.TeamDeadName);
			Bukkit.getLogger().info("Team " + Globals.TeamDeadName + " was created");
		}
	}
	
	private void ResetName(Player p)
	{
		p.setPlayerListName(p.getName());
	}
}
