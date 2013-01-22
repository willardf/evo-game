package com.potato.evolutiongame.game;

import java.io.Serializable;
import java.util.ArrayList;
import com.potato.evolutiongame.game.cards.CardGroup;
import com.potato.evolutiongame.game.cards.Card;
import com.potato.evolutiongame.game.cards.CardTag;

public class Environment implements Serializable {
	private static final long serialVersionUID = -4265701456364986604L;
	private static final int FEATURE_CNT = 3;
	private static final int NO_TAG_PENALTY = -5;
	private static final int TAG_MATCH_BONUS = 1;
	
	private Card temperature; 
	private Card rainfall;
	private Card[] features;
	private ArrayList<Card> catastrophies;
	private int lastFeature;
	
	public Environment()
	{
		lastFeature = 0;
		features = new Card[FEATURE_CNT];
		catastrophies = new ArrayList<Card>();
	}
	
	//Returns displaced card, if any
	public Card playCard(Card c)
	{
		Card output = null;
		if (c.getGroup() == CardGroup.Temperature) 
		{
			output = temperature;
			temperature = c;
		}
		else if (c.getGroup() == CardGroup.Rainfall) 
		{
			output = rainfall;
			rainfall = c;
		}
		else if (c.getGroup() == CardGroup.Feature)
		{
			if (features[lastFeature] != null)
			{
				output = features[lastFeature];
			}
			features[lastFeature] = c;
			lastFeature = (lastFeature + 1) % FEATURE_CNT;
			
		}
		else if (c.getGroup() == CardGroup.Catastrophy)
		{
			catastrophies.add(c);
		}
		return output;
	}
	public Card getTemperatureCard() {
		return temperature;
	}
	public Card getRainfallCard() {
		return rainfall;
	}
	public Card[] getFeatureCards() {
		return features;
	}

	public CardTag[] getTags()
	{
		ArrayList<CardTag> output = new ArrayList<CardTag>();
		CardTag[] tags;
		if (temperature != null)
		{
			tags = temperature.getTags();
			for (CardTag t : tags) output.add(t);
		}
		if (rainfall != null)
		{
			tags = rainfall.getTags();
			for (CardTag t : tags) output.add(t);
		}
		for(Card c : features)
		{
			if (c != null)
			{
				tags = c.getTags();
				for (CardTag t : tags)
					output.add(t);
			}
		}
		
		return (CardTag[])output.toArray();
	}
//	for(Card c : catastrophies)
//	{
//		tags = c.getTags();
//		for (CardTag t : tags)
//			output.remove(t);
//	}
	public int calculatePopulationChange(CardTag[] tags)
	{
		CardTag[] envTags = getTags();
		int output = 0;
		for (CardTag t : tags)
		{
			for(CardTag eT : envTags)
			{
				if (t == eT)
				{
					output += TAG_MATCH_BONUS;
					break;
				}
			}
		}
		return output == 0 ? NO_TAG_PENALTY : output;
	}
}
