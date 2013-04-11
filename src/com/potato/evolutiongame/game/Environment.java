package com.potato.evolutiongame.game;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.potato.evolutiongame.game.cards.Card;
import com.potato.evolutiongame.game.cards.CardGroup;
import com.potato.evolutiongame.game.cards.EnvironmentCard;
import com.potato.evolutiongame.game.cards.EnvironmentDeck;
import com.potato.evolutiongame.game.cards.PlayerCard;
import com.potato.evolutiongame.game.cards.CardTag;
import com.potato.evolutiongame.game.cards.PlayerDeck;

public class Environment implements Serializable{
	private static final long serialVersionUID = -4265701456364986604L;
	private static final int FEATURE_COUNT = 5;
	private static final int NO_TAG_PENALTY = -5;
	private static final int TAG_MATCH_BONUS = 1;
	
	private ArrayList<EnvironmentCard> features;
	private ArrayList<PlayerCard> catastrophies;
	
	public Environment()
	{
		features = new ArrayList<EnvironmentCard>();
		catastrophies = new ArrayList<PlayerCard>();
	}
	public Environment(JSONObject o) {
		this();
		
		JSONArray feats = o.getJSONArray("features");
		for (int i = 0; i < feats.length(); ++i)
			features.add(EnvironmentDeck.getCardInstance(feats.getInt(i)));
		
		JSONArray cats = o.getJSONArray("catastrophies");
		for (int i = 0; i < cats.length(); ++i)
			catastrophies.add(PlayerDeck.getCardInstance(cats.getInt(i)));
	}
	
	//Returns displaced card, if any
	public Card playCard(Card c)
	{
		Card output = null;
		
		if (c instanceof EnvironmentCard)
		{
			features.add((EnvironmentCard)c);
			if (features.size() > FEATURE_COUNT)
			{
				output = features.remove(0);
			}
		}
		else
		{
			PlayerCard pc = (PlayerCard)c;
			if (pc.getGroup() == CardGroup.Catastrophy)
			{
				catastrophies.add(pc);
			}
		}
		return output;
	}
	
	public EnvironmentCard[] getFeatureCards() {
		EnvironmentCard[] l = new EnvironmentCard[features.size()];
		for (int i = 0; i < features.size(); ++i)
			l[i] = features.get(i);
		return l;
	}

	public int calculatePopulationChange(Player p)
	{
		int output = 0;
		for (CardTag t : p.getCreature().getTags())
		{			
			for(Card c : features)
			{
				if (c == null) continue;
				for (CardTag eT : c.getTags())
					if (t == eT) 
					{
						output += TAG_MATCH_BONUS;
						break;
					}
			}
		}
		return output == 0 && features.size() > 0 ? NO_TAG_PENALTY : output;
	}

	public JSONObject getJSONObject() {
		JSONObject o = new JSONObject();
		JSONArray feats = new JSONArray();
		for (Card c : features)
			feats.put(c != null ? c.getCardIdx() : -1);
		o.put("features", feats);
		
		JSONArray cats = new JSONArray();
		for (PlayerCard c : catastrophies) cats.put(c.getCardIdx());
		o.put("catastrophies", cats);

		return o;
	}
}
