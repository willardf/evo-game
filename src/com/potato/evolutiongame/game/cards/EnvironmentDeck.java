package com.potato.evolutiongame.game.cards;

	import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

	import org.json.JSONArray;

	import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.BitmapDrawable;

import com.potato.evolutiongame.R;

public class EnvironmentDeck implements Serializable {
	private static final long serialVersionUID = -4513592919288316751L;
	private static ArrayList<EnvironmentCard> environmentCards;
	private static EnvironmentCard emptyCard;
	private ArrayList<EnvironmentCard> deck;
	
	public EnvironmentDeck(boolean isEmpty)
	{
		deck = new ArrayList<EnvironmentCard>();
		if (!isEmpty)
		{
			deck.addAll(environmentCards);
		}
	}
	public EnvironmentDeck(JSONArray arr)
	{
		deck = new ArrayList<EnvironmentCard>();
		for (int i = 0; i < arr.length(); ++i)
			deck.add(environmentCards.get(arr.getInt(i)));
	}
	
	public EnvironmentCard drawCard()
	{
		EnvironmentCard c = deck.get(deck.size() - 1);
		deck.remove(deck.size() - 1);
		return c;
	}
	public void placeCard(EnvironmentCard c)
	{
		if (c == null || c.getCardIdx() < 0) return; 
		deck.add(c);
	}
	public void placeDeck(EnvironmentDeck c)
	{
		while (c.count() > 0) deck.add(c.drawCard());
	}
	
	public void shuffle()
	{
		Random r = new Random();
		for (int i = 0; i < deck.size(); i++)
		{
			int swap = r.nextInt(deck.size());
			EnvironmentCard temp = deck.get(i);
			deck.set(i, deck.get(swap));
			deck.set(swap, temp);
		}
	}
	
	public int count(){ return deck.size(); }
	
	public static void Initialize(Context ctx) throws NotFoundException, IOException
	{	// Carnivore, Herbivore, Insectivore, Nectarivore, Flying, 
		// Cold, Hot, Dry, Wet, Aquatic, Rocky
		Resources res = ctx.getResources();
		emptyCard = new EnvironmentCard(-1, "Empty", 
				((BitmapDrawable)res.getDrawable(R.drawable.emptycard)).getBitmap(), 
				null, CardGroup.BodyPart);
		
		environmentCards = new ArrayList<EnvironmentCard>();
		addFeatureCard(res, "Mountains", R.drawable.card1, new CardTag[]{ CardTag.Rocky });
		addFeatureCard(res, "Lakes", R.drawable.card1, new CardTag[]{ CardTag.Aquatic });
		addFeatureCard(res, "Lush Plants", R.drawable.card1, new CardTag[]{ CardTag.Herbivore });
		addFeatureCard(res, "Weak Prey", R.drawable.card1, new CardTag[]{ CardTag.Carnivore });
		
	}
	private static void addFeatureCard(Resources res, String title, int resId, CardTag[] tags) throws NotFoundException, IOException
	{
		environmentCards.add(new EnvironmentCard(environmentCards.size(), title, 
				((BitmapDrawable)res.getDrawable(resId)).getBitmap(), 
				tags, CardGroup.Feature));
	}
	
	public static EnvironmentCard getCardInstance(int idx)
	{
		if (idx < 0)
			return emptyCard;
		return environmentCards.get(idx);
	}
	
	public JSONArray getJSONArray() {
		JSONArray arr = new JSONArray();
		
		for (EnvironmentCard c : deck) arr.put(c.getCardIdx());
		
		return arr;
	}
}
