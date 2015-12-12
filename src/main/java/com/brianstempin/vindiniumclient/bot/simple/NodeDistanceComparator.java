package com.brianstempin.vindiniumclient.bot.simple;

import java.util.Comparator;

public class NodeDistanceComparator implements Comparator<Node> {

	@Override
	public int compare(Node nodeOne, Node nodeTwo) {
		if(nodeOne.getDistance() < nodeTwo.getDistance())
		{
			return 1;
		}
		
		if(nodeOne.getDistance() > nodeTwo.getDistance())
		{
			return -1;
		}
		return 0;
	}

}
