package com.potato.evolutiongame.views;

import java.util.ArrayList;

import com.potato.evolutiongame.R;

import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

public class GameDisplayView extends View {
	ArrayList<SlideOverListener> slideListeners;
	
	Bitmap background;
	Rect wholeRect;
	
	int maxOffsetX;
	int offsetX;
	int screen;
	
	GestureDetector detector;
	public int startScroll;
	Scroller scroll;
	
	public GameDisplayView(Context ctx, AttributeSet attrs)
	{
		super(ctx, attrs);
		slideListeners = new ArrayList<SlideOverListener>();
		background = ((BitmapDrawable)getResources().getDrawable(R.drawable.background)).getBitmap();
		detector = new GestureDetector(ctx, new gestureListen());
		scroll = new Scroller(ctx);
		screen = 0;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldW, int oldH)
	{
		maxOffsetX = w;
		wholeRect = new Rect(0, 0, w, h);
	}
	
	@Override
	protected void onDraw(Canvas c)
	{
		if (!scroll.isFinished()) 
		{
			postInvalidate();
			scroll.computeScrollOffset();
			offsetX = scroll.getCurrX();
		}
		
		wholeRect.left = -offsetX;
		wholeRect.right = -offsetX + getWidth();
		c.drawBitmap(background, null, wholeRect, null);
		
		wholeRect.left += getWidth();
		wholeRect.right += getWidth();
		c.drawBitmap(background, null, wholeRect, null);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent evt)
	{
		detector.onTouchEvent(evt);
		super.onTouchEvent(evt);
		if (evt.getActionMasked() == MotionEvent.ACTION_UP)
		{
			if (startScroll != -1)
			{
				if (startScroll == 1)
					scroll.startScroll(offsetX, 0, maxOffsetX - offsetX, 0);
				else if (startScroll == 0)
					scroll.startScroll(offsetX, 0, -offsetX, 0);
				if (screen != startScroll)
				{
					screen = startScroll;
					for (SlideOverListener s : slideListeners) s.onSlideOver(screen);
				}
				postInvalidate();
			}
		}
		return true;
	}
	
	class gestureListen implements GestureDetector.OnGestureListener
	{
		@Override
		public boolean onDown(MotionEvent arg0) {
			startScroll = -1;
			return false;
		}

		@Override
		public boolean onFling(MotionEvent arg0, MotionEvent arg1, float vX,
				float vY) {
			scroll.fling(offsetX, 0, (int)vX, 0, 0, maxOffsetX, 0, 0);
			postInvalidate();
			return true;
		}

		@Override
		public void onLongPress(MotionEvent arg0) {
		}

		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float dX,
				float dY) {
			offsetX += dX;
			if (offsetX < 0) 
				offsetX = 0;
			if (offsetX > maxOffsetX) 
				offsetX = maxOffsetX;
			if (offsetX <= maxOffsetX / 2)
			{
				startScroll = 0;
			}		
			else
			{
				startScroll = 1;
			}
			postInvalidate();
			return true;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			return true;
		}
	}
	public void setOnSlideOverListener(SlideOverListener s)
	{
		slideListeners.add(s);
	}
}
