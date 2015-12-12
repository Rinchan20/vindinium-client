package com.brianstempin.vindiniumclient.bot.simple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.brianstempin.vindiniumclient.bot.BotMove;
import com.brianstempin.vindiniumclient.bot.BotUtils;
import com.brianstempin.vindiniumclient.bot.advanced.murderbot.HealDecisioner;
import com.brianstempin.vindiniumclient.bot.simple.Node;
import com.brianstempin.vindiniumclient.bot.simple.Edge;
import com.brianstempin.vindiniumclient.bot.simple.Graph;
import com.brianstempin.vindiniumclient.dto.GameState;
import com.brianstempin.vindiniumclient.dto.GameState.Board;
import com.brianstempin.vindiniumclient.dto.GameState.Game;
import com.brianstempin.vindiniumclient.dto.GameState.Hero;
import com.brianstempin.vindiniumclient.dto.GameState.Position;

/**
 * Example bot
 */
public class RandomBot implements SimpleBot {
	
	private static final Logger logger = LogManager.getLogger(RandomBot.class);
	
	public static Hero myHero;
	public static Node myNode;
	public static Position myInitialPosition;
	public static Boolean retreat;
	public static Game game;
	public static Graph gameGraph;
	
	public static Map<Node, Integer> mineLocations;
	public static Map<String, HeroInformation> enemyMineInformation;
	public static Map<Node, Position> tavernLocations;
	
	
    @Override
    public BotMove move(GameState gameState) {
    	logger.info("Moving...");
    	
    	//Get closest pub/tavern
    	NodeDistanceComparator distanceComparator = new NodeDistanceComparator();
    	Queue<Node> tavernDistances = new PriorityQueue<Node>(distanceComparator);
    	
    	myNode.setData(myHero.getPos());
    	
    	Node closestPub = null;
    	if(closestPub == null)
    	{
    		Set<Node> taverns = tavernLocations.keySet();
        	for(Node tavern : taverns)
        	{
        		double distance = distanceFormula(myNode, tavern);
        		tavern.setDistance(distance);
        		tavernDistances.add(tavern);
        	}
    	}
    	
    	closestPub = tavernDistances.poll();
    	tavernDistances.clear();
    	
    	Queue<Node> mineDistances = new PriorityQueue<Node>(distanceComparator);
    	
    	//Check if need HP
    	if(gameState.getHero().getGold() >= 2 && gameState.getHero().getLife() <= 35)
    	{
    		setRetreat(true);
    		List<Edge> target = RandomBot.gameGraph.dijkstra(gameGraph, myNode, closestPub);
    		int targetSize = target.size();
    		Edge pathway = target.get(targetSize);
    		Node pub = pathway.getTo();
    		Position pubPosition = (Position) pub.getData();
    		return BotUtils.directionTowards(gameState.getHero().getPos(), pubPosition);
    	}else
    	{
    		setRetreat(false);
    		Set<Node> mines = mineLocations.keySet();
    		for(Node mine : mines)
    		{
    			int id = mineLocations.get(mine);
    			if(id != myHero.getId())
    			{
    				double distance = distanceFormula(myNode, mine);
            		mine.setDistance(distance);
            		mineDistances.add(mine);
    			}
    		}
    		Node closestMine = mineDistances.poll();
    		mineDistances.clear();
    		List<Edge> target = RandomBot.gameGraph.dijkstra(gameGraph, myNode, closestPub);
    		int targetSize = target.size();
    		Edge pathway = target.get(targetSize);
    		Node mine = pathway.getTo();
    		Position minePosition = (Position) mine.getData();
    		return BotUtils.directionTowards(gameState.getHero().getPos(), minePosition);
    	}
    	
//        int randomNumber = (int)(Math.random() * 4);
//
//        switch(randomNumber) {
//            case 1:
//                return BotMove.NORTH;
//            case 2:
//                return BotMove.SOUTH;
//            case 3:
//                return BotMove.EAST;
//            case 4:
//                return BotMove.WEST;
//            default:
//                return BotMove.STAY;
//        }
    }
    
    public double distanceFormula(Node nodeOne, Node nodeTwo)
    {
    	double result = -1;
    	
    	Position one = (Position) nodeOne.getData();
    	Position two = (Position) nodeTwo.getData();
    	
    	int xResult = two.getX() - one.getX();
    	int yResult = two.getY() - one.getY();
    	result = Math.sqrt(xResult + yResult);
    	
    	return result;
    }

    @Override
    public void setup(Game game, Hero hero) {
        //Set up needed information
    	mineLocations = new HashMap<Node, Integer>();
    	enemyMineInformation = new HashMap<String, HeroInformation>();
    	tavernLocations = new HashMap<Node, Position>();
    	
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
    	RandomBot.myInitialPosition = hero.getSpawnPos();
    }
    
    /**
     * Changes game info so that it is similar to a graph
     * @param game
     */
    public static void setupGameGraph(Game game)
    {
    	RandomBot.game = game;
    	Board gameBoard = RandomBot.game.getBoard();
    	int boardSize = gameBoard.getSize();
    	
    	List<String> ignoredCharacters = new ArrayList<String>();
    	ignoredCharacters.add("+");
    	ignoredCharacters.add("|");
    	
    	List<Node> gameNodes = new LinkedList<Node>();
    	int nodeID = 0;
    	int tavernID = 0;
    	
    	for(int row = 0; row < boardSize; row++)
    	{
    		for(int col = 0; col < boardSize; col++)
    		{
    			GameState.Position position = new GameState.Position(row, col);
    			Node node = new Node(nodeID, position);
    			int tile = row * boardSize * 2 + (col * 2);
    			String gameTile = gameBoard.getTiles().substring(tile, tile + 2);
    			if(gameTile.contains(ignoredCharacters.get(0)) || gameTile.contains(ignoredCharacters.get(1)))
    			{
    				continue;
    			}
    			String tileType = RandomBot.setTileType(gameTile);
    			
    			node.setTileStatus(tileType);
    			gameNodes.add(node);
    			
    			//Get tavern, hero, and mine locations and information
    			if(tileType.equals("Tavern"))
    			{
    				tavernLocations.put(node, position);
    				tavernID++;
    			}else if(tileType.equals("Mine"))
    			{
    				int id = Integer.parseInt(getIdentifier(gameTile));
    				mineLocations.put(node, id);
    			}	
    			nodeID++;
    		}
    	}
    	
    	Node currentNode = null;
    	Edge currentEdge = null;
    	
    	//Add in edges
    	for(int index = 0; index < (boardSize * boardSize); index++)
    	{
    		int row = index % boardSize;
    		int col = index / boardSize;
    		
    		currentNode = gameNodes.get(index);
    		Position nodePosition = (Position) currentNode.getData();
    		Position heroPosition = RandomBot.myHero.getPos();
    		
    		//Check if current node is me
    		if((nodePosition.getX() == heroPosition.getX()) && (nodePosition.getY() == heroPosition.getY()))
    		{
    			myNode = currentNode;
    		}
    		
    		//Skip impassable tiles
    		String nodeTileType = (String) currentNode.getTileStatus();
    		if(nodeTileType.equals("Wood") || nodeTileType.equals("Tavern") || nodeTileType.equals("Mine"))
    		{
    			continue;
    		}
    		
    		//Give edges to neighboring nodes by column and row
    		for(int mark = col - 1; mark <= col + 1; mark += 2)
    		{
    			if(mark >= 0 && mark < boardSize)
    			{
    				Node neighborNode = gameNodes.get(mark * boardSize + row);
    				currentEdge = new Edge(currentNode, neighborNode, 1);
    				RandomBot.gameGraph.addEdge(currentEdge);
    			}
    		}
    		
    		for(int mark = row - 1; mark <= row + 1; mark += 2)
    		{
    			if(mark >= 0 && mark < boardSize)
    			{
    				Node neighborNode = gameNodes.get(mark * boardSize + row);
    				currentEdge = new Edge(currentNode, neighborNode, 1);
    				RandomBot.gameGraph.addEdge(currentEdge);
    			}
    		}
    	}
    	
    	//Graph constructed
    }

    
    
    /**
     * Gets the last character that identifies the hero and who owns what mine
     * @param tile
     * @return
     */
    public static String getIdentifier(String tile)
    {
    	return tile.substring(tile.length() - 1);
    }
    
    /**
     * Get the tile type e.g. Impassable wood, tavern, mine, hero, empty
     * @param tile
     * @return
     */
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
