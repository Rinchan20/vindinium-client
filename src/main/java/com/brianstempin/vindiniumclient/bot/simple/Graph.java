package com.brianstempin.vindiniumclient.bot.simple;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


public class Graph {

    private List<Node> nodes;
    private List<Edge> edges;
    private Dijkstra dijkstra;

    public Graph()
    {    	
    	nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
        dijkstra = new Dijkstra();
    }

    /**
     * Return true if node x is connecting to y false otherwise
     */
    public boolean adjacent(Node x, Node y) {
    	for (Edge edge: edges) {
            if (edge.getFrom().equals(x) && edge.getTo().equals(y)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return all neighbor nodes (that has at least one edge connected from node x)
     */
    public List<Node> neighbors(Node x) {
    	List<Node> result = new ArrayList<Node>();

        if (!nodes.contains(x)) {
            return result;
        }

        for (Edge edge: edges) {
            if (edge.getFrom().equals(x)) {
                result.add(edge.getTo());
            }
        }

        return result;
    }

    /**
     * Add a node to graph
     *
     * Return false if node is already in graph, true if node is added to graph
     * successfully
     */
    public boolean addNode(Node x) {
    	if (nodes.contains(x)) {
            return false;
        }

        nodes.add(x);
        return true;
    }

    /**
     * Remove a node to graph (note that you also need to remove edge if there
     * is any edge connecting to/from this node)
     *
     * Return true if the node is removed successfully, false if the node
     * doesn't exist in graph
     */
    public boolean removeNode(Node x) {
    	if (!nodes.contains(x)) {
            return false;
        }

        Iterator<Edge> iterator = edges.iterator();
        while (iterator.hasNext()) {
            Edge edge = iterator.next();
            if (edge.getTo().equals(x) || edge.getFrom().equals(x)) {
                iterator.remove();
            }
        }

        return nodes.remove(x);
    }

    /**
     * Add an edge to graph (connecting two nodes)
     *
     * Return true if the edge is added successfully, return false if the edge
     * already exists in graph
     */
    public boolean addEdge(Edge x) {
    	if (!nodes.contains(x.getFrom())) {
            nodes.add(x.getFrom());
        }
        if (!nodes.contains(x.getTo())) {
            nodes.add(x.getTo());
        }

        if (edges.contains(x)) {
            return false;
        }

        edges.add(x);

        return true;
    }

    /**
     * Remove an edge from graph (remember not to remove node)
     *
     * Return true if edge is removed successfully, return false if the edge is
     * not presented in graph
     */
    public boolean removeEdge(Edge x) {
    	return edges.remove(x);
    }

    /**
     * Get edge value between from node to to node
     */
    public int distance(Node from, Node to) {
    	for (Edge edge: edges) {
            if (edge.getFrom().equals(from) && edge.getTo().equals(to)) {
                return edge.getValue();
            }
        }

        return 0;
    }

    /**
     * Search through this graph from sourceNode to distNode and return a list
     * of edges in between
     */
    public List<Edge> dijkstra(Graph graph, Node source, Node dist) {   
        List<Edge> path = dijkstra.search(graph, source, dist);       
        return path;
    }

    /**
     * A simple method to get a node out of graph
     */
    public Node getNode(int index) {
        Node node = this.nodes.get(index);
        return node;
    }

    @Override
    public String toString() {
        String result = "";
        
        for(Edge edge : edges)
        {
        	Node nodeOne = edge.getFrom();
        	Node nodeTwo = edge.getTo();
        	
        	result += nodeOne + " -> " + nodeTwo + "\n";
        }
        
        return result;
    }
}
