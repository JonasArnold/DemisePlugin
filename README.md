# DemisePlugin
Minecraft Server Plugin for the game "Demise".

# Plugin status
Plugin is not finished!
To Be implemented:
 - [ ] End of game detection (not completely sure if it works already)
 - [ ] Option to disable PVP for the participating players
 - [ ] Option to disable scoreboard for not-participating players

# The Game
Demise is a game that can be played on multiplayer servers. The last player alive wins the game.
There are some rules:
- No PVP
- Dead players can build traps to kill the alive players
- Alive players must not build traps to kill other players

Ideas to enhance the game:
- Define a participation cost and the winner gets everything.
- Prescribe lower tier armor to make the game easier.

# How to use the plugin
The players that are participating in the game will get a Prefix before their name "[Alive]" or "[Dead"].
A scoreboard is automatically generated to display the game state and the alive/dead participants.

| Command | OP Only? | Description |  
|--|--|--|
|**/demise**|No| Provides the requesting user with info about the current game state.  |
|**/joinDemise**|No| To join the game (game must not be running).  |
|**/leaveDemise**|No| To leave the game (game must not be running).  |
|**/startDemise**|Yes| Starts the game. |
|**/stopDemise**|Yes| Stops the game. |
