package com.potato.evolutiongame.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GroupedButtonList extends View {
	private Paint textPaint;
	private float textHeight;
	
	private ArrayList<ArrayList<String>> buttons;
	
	public GroupedButtonList(Context c, AttributeSet attrs)
	{
		super(c, attrs);
		buttons = new ArrayList<ArrayList<String>>();
		
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(Color.BLUE);
		textPaint.setStyle(Paint.Style.FILL);
		textHeight = textPaint.getTextSize();
	}
	
	public void addGroup(ArrayList<String> s)
	{
		buttons.add(s);
	}
	public void addToGroup(int group, String s)
	{
		buttons.get(group).add(s);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		
	}
	
	@Override
	protected void onDraw(Canvas c)
	{
		for (ArrayList<String> group : buttons)
		{
			if (group.size() == 1)
			{
				// Just a button
			}
			else
			{
				// Border
				//for (String)
			}
		}
		int l = getLeft();
		int t = getTop();
		int w = getWidth();
		int h = getHeight();
		c.drawRect(l, t, w, h, textPaint);
	}
}
