package com.potato.evolutiongame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class CreatureDisplayView extends View {
	private Paint bgPaint;
	//private Paint whitePaint;
	private Rect wholeRect;

	public CreatureDisplayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		bgPaint = new Paint();
		bgPaint.setColor(Color.BLACK);
		bgPaint.setStyle(Paint.Style.FILL);
	}
	
	protected void onSizeChanged(int w, int h, int oldW, int oldH){
		wholeRect = new Rect(0, 0, w, h);
	}
	@Override
	protected void onDraw(Canvas c) {
		c.clipRect(wholeRect, Region.Op.REPLACE);
		c.drawRect(wholeRect, bgPaint);
	}
}
