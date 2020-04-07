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
	Main plugin;
	GameStateHandler gameState;
	Scoreboard b;
	String teamDead = "Dead";
	String teamAlive = "Alive";
	String objectiveName = "DeadPoolGame";
	
	public ScoreboardHandler(Main plugin, GameStateHandler gameState)
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
		Objective o = b.registerNewObjective("DeadPoolGame", "");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		o.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Dead Pool Game");	
		
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
		if(this.b.getTeam(teamAlive) == null)
		{		
			this.b.registerNewTeam(teamAlive);
			Bukkit.getLogger().info("Team " + teamAlive + " was created");
		}
		if(b.getTeam(teamDead) == null)
		{
			this.b.registerNewTeam(teamDead);
			Bukkit.getLogger().info("Team " + teamDead + " was created");
		}
	}
	
	private void ResetName(Player p)
	{
		p.setPlayerListName(p.getName());
	}
}
