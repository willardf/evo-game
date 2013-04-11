package com.potato.evolutiongame.game.cards;

import android.graphics.Bitmap;

public class EnvironmentCard extends Card {

	public EnvironmentCard(int idx, String title, Bitmap bitmap,
			CardTag[] t, CardGroup grp) {
		cardIdx = idx;
		tags = t;
		image = bitmap;
		name = title;
		group = grp;
	}

}
