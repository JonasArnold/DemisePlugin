package com.thejounezz.demisegame;

public class GameStateHandler 
{
	Main plugin;
	
	public GameStateHandler(Main plugin)
	{
		this.plugin = plugin;
	}
	
	public void Update(GameState state)
	{
		plugin.getConfig().set("gameState", state.name());
		plugin.saveConfig();
	}
	
	public GameState Get()
	{
		String gameState = plugin.getConfig().getString("gameState");
		if(gameState != null)
		{
			return GameState.valueOf(gameState);
		}
		return GameState.UNKNOWN;
	}
}
