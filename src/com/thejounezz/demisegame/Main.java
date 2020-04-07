package com.thejounezz.demisegame;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Main extends JavaPlugin
{
	GameStateHandler gameState;
	StatusInformer statusInformer;
	ScoreboardHandler scoreboardHandler;
	ScoreboardManager scoreboardManager;
	Scoreboard scoreboard;
	String teamDead = "Dead";
	String teamAlive = "Alive";
	
	@Override
	public void onEnable()
	{
		// log message
		getLogger().info("DeadpoolGame has been enabled.");
		
		// create instances
		this.gameState = new GameStateHandler(this);
		this.statusInformer = new StatusInformer(this.gameState);
		this.scoreboardHandler = new ScoreboardHandler(this, this.gameState);
		
		// get scoreboard manager (also makes teams)
		scoreboard = this.scoreboardHandler.GetScoreboard();
		
		// register listeners
		PluginManager pm = getServer().getPluginManager();
		MyListener listener = new MyListener(this, this.statusInformer, this.scoreboardHandler);
		pm.registerEvents(listener, this);
		
		// reset plugin values if first start or undefined values
		if(getConfig().getString("gameState") == null)
		{
			this.gameState.Update(GameState.NOT_RUNNING);
			this.saveConfig();
		}
	}

	private void UpdateAllPlayersScoreboard()
	{
		for(Player p : Bukkit.getOnlinePlayers())
		{
			this.scoreboardHandler.GenerateNewScoreboard(p);
		}
	}

	@Override
	public void onDisable()
	{
		// log message
		getLogger().info("DeadpoolGame has been disabled.");
	}
	
	public Set<String> GetAlivePlayers()
	{
		Set<String> players = scoreboard.getTeam(teamAlive).getEntries();
		if(players == null)
		{
			players = new HashSet<String>();
		}
		return players;
	}
	
	public Set<String> GetDeadPlayers()
	{
		Set<String> players = scoreboard.getTeam(teamDead).getEntries();
		if(players == null)
		{
			players = new HashSet<String>();
		}
		return players;
	}
	
	public String EliminatePlayer(Player player)
	{
		// check if game is even running
		if(gameState.Get() != GameState.RUNNING)
		{
			return "Game not running";
		}
		
		// check if player exists
		if(player == null)
		{
			return "Player name not recognized.";
		}
		
		// check if player plays in game
		if(this.GetAlivePlayers().contains(player.getName()))
		{
			// send message to player to inform about elimination
			player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "You were eliminated in the Deadpool Game");
			
			// broadcast message
			statusInformer.BroadcastToAllPlayers(ChatColor.RED + "" + ChatColor.BOLD + player.getName() + " was eliminated from the DeadPool Game!", false);
			
			// remove from list of alive, add to list of dead players
			scoreboard.getTeam(teamAlive).removeEntry(player.getName());
			scoreboard.getTeam(teamDead).addEntry(player.getName());
		}
		
		// game ended because of last player death
		if(scoreboard.getTeam(teamAlive).getEntries().size() < 1)
		{
			this.gameState.Update(GameState.NOT_RUNNING);
			statusInformer.BroadcastToAllPlayers("DeadPool Game has just ended with the death of " + player.getName() + "!");
		}
		
		this.UpdateAllPlayersScoreboard();
		
		return "Player was eliminated from the game.";
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Player player = (Player) sender;
		
		// check if not console
		if (sender instanceof Player)
		{
			String lowerCmd = cmd.getName().toLowerCase();
			
			switch(lowerCmd) 
			{
				case "deadpool":
					statusInformer.InformDeadpoolStatus(this, ((Player) sender).getPlayer(), true);
					
					break;
					
				case "joindeadpool":
					if(gameState.Get() != GameState.RUNNING)
					{
						player.sendMessage(ChatColor.GOLD + "You were added to the Deadpool Game");
	
						// add to list of alive players
						scoreboard.getTeam(teamAlive).addEntry(player.getName());
					}
					else
					{
						player.sendMessage("Game already running, sorry!");
					}
					
					break;
				
				case "leavedeadpool":
					if(gameState.Get() != GameState.RUNNING)
					{
						player.sendMessage(ChatColor.GOLD + "You left the Deadpool Game!");
	
						// add to list of alive players
						scoreboard.getTeam(teamAlive).removeEntry(player.getName());
						scoreboard.getTeam(teamDead).removeEntry(player.getName());
					}
					else
					{
						player.sendMessage("Game already running, you cannot leave now!");
					}
					
					break;
								
				case "startgame":
					if(player.isOp())
					{
						// starts the game
						this.gameState.Update(GameState.RUNNING);
						statusInformer.BroadcastToAllPlayers("DeadPool Game has started!!!");
					}
					else
					{
						player.sendMessage("You need to be OP to run this command");
					}
					break;
					
				case "stopgame":
					if(player.isOp())
					{
						// stops the game
						this.gameState.Update(GameState.NOT_RUNNING);
						statusInformer.BroadcastToAllPlayers("DeadPool Game has been stopped.");
					}
					else
					{
						player.sendMessage("You need to be OP to run this command");
					}
					break;
				
				case "resetgame":
					if(player.isOp())
					{
						if(gameState.Get() != GameState.RUNNING)
						{
							// reinstanciation generates every thing new
							this.scoreboardHandler = new ScoreboardHandler(this, this.gameState);
							
							// remove scoreboards of all players
							for(Player p : Bukkit.getOnlinePlayers())
							{
								this.scoreboardHandler.ClearScoreboard(p);
							}
							
							player.sendMessage("Dead Pool was reset.");
							return true; // end here => no new scoreboard
						}
						else
						{
							player.sendMessage("You cannot reset the game when it's running!");
						}
					}
					else
					{
						player.sendMessage("You need to be OP to run this command");
					}
					break;
				
				default:
					player.sendMessage("Your command was not recognized!");
					break;
			}
			
			this.UpdateAllPlayersScoreboard();
			return true;
		}
		
		player.sendMessage("Your command was not recognized!");
		return true;
	}
	
}
