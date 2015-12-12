package com.brianstempin.vindiniumclient.bot.simple;

import java.util.ArrayList;
import java.util.List;

import com.brianstempin.vindiniumclient.dto.GameState.Position;

public class HeroInformation {

	public static Integer heroMineAmount;
	public static List<Position> heroMineLocations = new ArrayList<Position>();
	
	public HeroInformation()
	{
		
	}
	
	public HeroInformation(Integer amount, Position position)
	{
		this.heroMineAmount = amount;
		heroMineLocations.add(position);
	}

	/**
	 * @return the heroMineAmount
	 */
	public static Integer getHeroMineAmount() {
		return heroMineAmount;
	}

	/**
	 * @param heroMineAmount the heroMineAmount to set
	 */
	public static void setHeroMineAmount(Integer heroMineAmount) {
		HeroInformation.heroMineAmount = heroMineAmount;
	}

	/**
	 * @return the heroMineLocations
	 */
	public static List<Position> getHeroMineLocations() {
		return heroMineLocations;
	}

	/**
	 * @param heroMineLocations the heroMineLocations to set
	 */
	public static void setHeroMineLocations(List<Position> heroMineLocations) {
		HeroInformation.heroMineLocations = heroMineLocations;
	}
	
	public static void addMineLocation(Position position)
	{
		HeroInformation.heroMineLocations.add(position);
	}
}
