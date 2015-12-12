package com.brianstempin.vindiniumclient.bot.simple;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Dijkstra{
    static List<String> graphMethod;
    static boolean DEBUG = false;
	
    /*
     * 1.Map everything
     * 1.5 Map the cost to get to each node
     * 2. Use dijkstra to determine to shortest path
     */
    public List<Edge> search(Graph graph, Node source, Node dist) {
    	/*
    	 * Attempting to change teacher's BFS to Dijkstra (e.g. have it consider edge weight)
    	 */
    	List<Edge> result = new ArrayList<>();
        Map<Node, Node> parents = new HashMap<>();
        Map<Node, Integer> distances = new HashMap<>();

        Queue<Node> queue = new LinkedList<>();
        Comparator<Node> nodeCompare = new nodeDataComparator();
        PriorityQueue<Node> potentialMovements = new PriorityQueue<Node>(nodeCompare);

        distances.put(source, 0);
        parents.put(source, null);
        queue.add(source);
        
        //get 4's child
        

        while (!queue.isEmpty()) {
            Node parent = queue.poll();
            
            // for all the neighbors (possible next moves)
            graph.neighbors(parent).stream().filter(child -> !distances.containsKey(child)).forEach(child -> {
            	System.out.println("Parent: " + parent);
            	System.out.println("Child: " + child);
                distances.put(child, graph.distance(parent, child));
                child.setData(graph.distance(parent, child));
                System.out.println(child + " distance from the parent, " + parent + ": " + graph.distance(parent, child));
                parents.put(child, parent);
                queue.add(child);
                System.out.println();
//                showQueueElements(queue);
            });
        }
        
        Node currentNode = dist;

        // build the list of moves by recursively calling parent
        while (!currentNode.equals(source)) {
            Node parent = parents.get(currentNode);

            
            if (parent != null) {
                result.add(new Edge(parent, currentNode, graph.distance(parent, currentNode)));
            }

            currentNode = parents.get(currentNode);

        }

        Collections.reverse(result);

        return result;
    }
    
    private void showQueueElements(Queue<Node> queue)
    {
    	System.out.println();
    	System.out.println("------------------------");
    	System.out.println("\tShowing Queue");
    	for(Node node : queue)
    	{
    		System.out.println(node);
    	}
    	System.out.println("------------------------");
    }
    
    private class nodeDataComparator implements Comparator<Node>
    {

		@Override
		public int compare(Node nodeOne, Node nodeTwo) {
			int nodeOneDistance = (int) nodeOne.getData();
			int nodeTwoDistance = (int) nodeTwo.getData();
			
			if(nodeOneDistance > nodeTwoDistance)
			{
				return 1;
			}else if(nodeOneDistance < nodeTwoDistance)
			{
				return -1;
			}
			return 0;
		}
    	
    }
}
