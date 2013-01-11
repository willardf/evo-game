package com.potato.evolutiongame.views;

import com.potato.evolutiongame.game.GameState;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

public class GameStateDisplayView extends View {
	Paint bgPaint;
	GameState gS;
	Rect wholeRect;
	
	public GameStateDisplayView(Context ctx, AttributeSet attr)
	{
		super(ctx, attr);
		bgPaint = new Paint();
		bgPaint.setColor(Color.BLUE);
		bgPaint.setStyle(Paint.Style.FILL);
		//bgHeight = textPaint.getTextSize();
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
	}
	
	public void setGameState(GameState g)
	{
		gS = g;
	}
}
