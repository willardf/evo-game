package com.potato.evolutiongame;

import java.util.HashMap;

public class Cookies {
	private static HashMap<String, String> data = new HashMap<String, String>();
	public static void set(String key, String value)
	{
		data.put(key, value);
	}
	public static String get(String key)
	{
		return data.get(key);
	}
	public static void clear(String key)
	{
		data.remove(key);
	}
}
