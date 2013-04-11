package com.potato.evolutiongame.views;

import com.potato.evolutiongame.game.GameState;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class GameStateDisplayView extends View {
	private Paint bgPaint;
	private GameState gameState;
	private Rect wholeRect;
	private Paint whitePaint;
	private int textHeight;
	
	private String sizeText;
	private String populationText;
	private static final float padding = 2.5f;
	
	public GameStateDisplayView(Context ctx, AttributeSet attr)
	{
		super(ctx, attr);
		bgPaint = new Paint();
		bgPaint.setColor(Color.BLUE);
		bgPaint.setStyle(Paint.Style.FILL);
		
		whitePaint = new Paint();
		whitePaint.setColor(Color.WHITE);
		whitePaint.setStyle(Paint.Style.FILL);
		whitePaint.setTextSize(14);
		whitePaint.setAntiAlias(true);
		whitePaint.setTypeface(Typeface.DEFAULT_BOLD);
		textHeight = (int)(whitePaint.getTextSize() + .5);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldW, int oldH)
	{
		wholeRect = new Rect(0, 0, w, h);
	}
	
	@Override
	protected void onDraw(Canvas c)
	{
		c.drawRect(wholeRect, bgPaint);
		if (gameState == null) return;
		
		float bottom = textHeight + padding;
		c.drawText(sizeText, padding, bottom, whitePaint);
		bottom += textHeight + padding;
		c.drawText(populationText, padding, bottom, whitePaint);
	}
	
	public void setGameState(GameState g)
	{
		gameState = g;
		int size = gameState.getCurrentPlayerCreature().getSize();
		sizeText = "Size: " + size + " (" + gameState.getSizeText(size) + ")";
		populationText = "Population: " + gameState.getCurrentPlayer().getPopulation() +
				"/" + gameState.getGoalPopulation();
	}
}
