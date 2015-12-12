package com.brianstempin.vindiniumclient.bot.simple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.advanced.murderbot.HealDecisioner;
import com.brianstempin.vindiniumclient.dto.GameState;
import com.brianstempin.vindiniumclient.dto.GameState.Board;
import com.brianstempin.vindiniumclient.dto.GameState.Game;
import com.brianstempin.vindiniumclient.dto.GameState.Hero;
import com.brianstempin.vindiniumclient.dto.GameState.Position;

import edu.csula.cs460.graph.Edge;
import edu.csula.cs460.graph.Graph;
import edu.csula.cs460.graph.Node;

/**
 * Example bot
 */
public class RandomBot implements SimpleBot {
	
	private static final Logger logger = LogManager.getLogger(RandomBot.class);
	
	public static Hero myHero;
	public static Position myInitialPosition;
	public static Boolean retreat;
	public static Game game;
	
	public static Map<String, Position> enemyLocations;
	public static Map<String, Integer> enemyMineAmount;
	public static Map<String, Position> tavernLocations;
	
	//Movement choices
	public enum MoveChoice
	{
		//Possible directions
		STAY("Stay"), NORTH("North"), SOUTH("South"), EAST("East"), WEST("West");
		
		private String move;
		
		private MoveChoice(String move)
		{
			this.move = move;
		}
	}
	
    @Override
    public BotMove move(GameState gameState) {
        int randomNumber = (int)(Math.random() * 4);

        switch(randomNumber) {
            case 1:
                return BotMove.NORTH;
            case 2:
                return BotMove.SOUTH;
            case 3:
                return BotMove.EAST;
            case 4:
                return BotMove.WEST;
            default:
                return BotMove.STAY;
        }
    }

    @Override
    public void setup(Game game, Hero hero) {
        //Set up needed information
    	setupGameGraph(game);
    	setupHero(hero);
    	setRetreat(false);
    }
    
    /**
     * Set up hero information
     * @param hero
     */
    public static void setupHero(Hero hero)
    {
    	RandomBot.myHero = hero;
    	RandomBot.myInitialPosition = hero.getPos();
    }
    
    /**
     * Changes game info so that it is similar to a graph
     * @param game
     */
    public static void setupGameGraph(Game game)
    {
    	Board gameBoard = game.getBoard();
    	int boardSize = gameBoard.getSize();
    	
    	enemyLocations = new HashMap<String, Position>();
    	enemyMineAmount = new HashMap<String, Integer>();
    	tavernLocations = new HashMap<String, Position>();
    	
    	List<String> ignoredCharacters = new ArrayList<String>();
    	ignoredCharacters.add("+");
    	ignoredCharacters.add("|");
    	
    	List<Node> gameNodes = new LinkedList<Node>();
    	int nodeID = 0;
    	int heroID = -1;
    	int tavernID = 0;
    	
    	for(int row = 0; row < boardSize; row++)
    	{
    		for(int col = 0; col < boardSize; col++)
    		{
    			GameState.Position position = new GameState.Position(row, col);
    			Node node = new Node(nodeID, position);
    			int tile = row * boardSize * 2 + (col * 2);
    			String gameTile = gameBoard.getTiles().substring(tile, tile + 2);
    			String tileType = RandomBot.setTileType(gameTile);
    			
    			//Set level is now for tile type information
    			node.setLevel(tileType);
    			gameNodes.add(node);
    			
    			if(tileType.equals("Tavern"))
    			{
    				String tavern = "Tavern" + tavernID;
    				tavernLocations.put(tavern, position);
    			}
    		}
    	}
    }
    
    public static String setTileType(String tile)
    {
    	String type = "";
    	Boolean containsHeroIdentifier = tile.startsWith("@");
    	Boolean containsMineIdentifier = tile.startsWith("$");
    	
    	if(tile.equals("##"))
    	{
    		type = "Wood";
    	}else if(tile.equals("[]"))
    	{
    		type = "Tavern";
    	}else if(containsHeroIdentifier)
    	{
    		type = "Hero";
    	}else if(containsMineIdentifier)
    	{
    		type = "Mine";
    	}else if(tile.equals("__"))
    	{
    		type = "Empty";
    	}
    
    	return type;
    }

    /**
	 * @return the retreat mode that decides bot's movement
	 */
	public static Boolean getRetreat() {
		return retreat;
	}

	/**
	 * @param retreat true to retreat, false to carry-on
	 */
	public static void setRetreat(Boolean retreat) {
		RandomBot.retreat = retreat;
	}

	@Override
    public void shutdown() {
        // No-op
    }
}
