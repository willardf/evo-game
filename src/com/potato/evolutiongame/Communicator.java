package com.potato.evolutiongame;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.potato.evolutiongame.game.GameEntry;
import com.potato.evolutiongame.game.GameState;


public class Communicator {
	private static URL serverURL;
	private static HttpURLConnection connection;
	private static final String SERVER_URL = "http://fatedgame.cloudapp.net/Share/";
	public static void Initialize() throws IOException
	{
		serverURL = new URL(SERVER_URL);
	}
	
	public static long authenticate(String username, String password) throws IOException
	{
		long output = -1L;
		openConnection();
		String data = "op=0" +
				"&username=" + username +
				"&password=" + password;
		
		sendData(data);
		String response = receiveData();
		closeConnection();
		try{
			output = Long.parseLong(response);
		}catch(Exception e)
		{
		}
		return output;
	}
	public static ArrayList<GameEntry> getGames(long id) throws IOException
	{
		ArrayList<GameEntry> output = new ArrayList<GameEntry>();
		openConnection();
		
		String data = "op=1" +
				"&id=" + id;
		sendData(data);
		
		String res = receiveData();
		String[] split = res.split(";");
		for (String s : split)
		{
			try{
				GameEntry e = GameEntry.parseGameEntry(s);
				output.add(e);
			} catch(Exception e){}
		}
		closeConnection();
		
		return output;
	}
	public static GameState getGameState(long gId) throws Exception
	{
		ArrayList<GameEntry> output = new ArrayList<GameEntry>();
		openConnection();
		
		String data = "op=2" +
				"&gid=" + gId;
		sendData(data);
		
		String res = receiveData();
		closeConnection();
		return GameState.parseGameState(res);
	}
	
	private static void openConnection() throws IOException
	{
		connection = (HttpURLConnection)serverURL.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setChunkedStreamingMode(0);
	}
	private static void closeConnection()
	{
		connection.disconnect();
	}
	private static void sendData(String data) throws IOException
	{
		OutputStream out = new BufferedOutputStream(connection.getOutputStream());
		byte[] buffer = data.getBytes();
		out.write(buffer);
		out.close();
	}
	private static String receiveData() throws IOException
	{
		InputStream inp = new BufferedInputStream(connection.getInputStream());
		
		byte[] output = new byte[inp.available() + 1];
		inp.read(output);
		inp.close();
		
		String out = new String(output);
		return out.trim();
	}
}
