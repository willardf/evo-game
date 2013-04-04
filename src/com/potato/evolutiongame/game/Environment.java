package com.potato.evolutiongame.game;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.potato.evolutiongame.game.cards.CardGroup;
import com.potato.evolutiongame.game.cards.Card;
import com.potato.evolutiongame.game.cards.CardTag;
import com.potato.evolutiongame.game.cards.Deck;

public class Environment implements Serializable{
	private static final long serialVersionUID = -4265701456364986604L;
	private static final int FEATURE_CNT = 3;
	private static final int NO_TAG_PENALTY = -5;
	private static final int TAG_MATCH_BONUS = 1;
	
	private Card temperature; 
	private Card rainfall;
	private Card[] features;
	private ArrayList<Card> catastrophies;
	
	public Environment()
	{
		features = new Card[FEATURE_CNT];
		catastrophies = new ArrayList<Card>();
	}
	public Environment(JSONObject o) {
		
		temperature = Deck.getCardInstance(o.getInt("temp"));
		rainfall = Deck.getCardInstance(o.getInt("rainfall"));
		
		JSONArray feats = o.getJSONArray("features");
		features = new Card[feats.length()];
		for (int i = 0; i < feats.length(); ++i)
			features[i] = Deck.getCardInstance(feats.getInt(i));
		
		JSONArray cats = o.getJSONArray("catastrophies");
		catastrophies = new ArrayList<Card>();
		for (int i = 0; i < cats.length(); ++i)
			catastrophies.add(Deck.getCardInstance(cats.getInt(i)));
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
			output = c;
		}
		else if (c.getGroup() == CardGroup.Catastrophy)
		{
			catastrophies.add(c);
		}
		return output;
	}
	public Card playFeatureCard(Card c, int idx)
	{
		Card output = features[idx];
		features[idx] = c;
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

	public JSONObject getJSONObject() {
		JSONObject o = new JSONObject();
		o.put("temp", temperature != null ? temperature.getCardIdx() : -1);
		
		o.put("rainfall", rainfall != null ? rainfall.getCardIdx() : -1);
		
		JSONArray feats = new JSONArray();
		for (Card c : features) 
			feats.put(c != null ? c.getCardIdx() : -1);
		o.put("features", feats);
		
		JSONArray cats = new JSONArray();
		for (Card c : catastrophies) cats.put(c.getCardIdx());
		o.put("catastrophies", cats);

		return o;
	}

}
