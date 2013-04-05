package com.potato.evolutiongame;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.potato.evolutiongame.game.CommunicationException;
import com.potato.evolutiongame.game.GameEntry;
import com.potato.evolutiongame.game.GameState;

public class Communicator {
	private static URL serverURL;
	private static HttpURLConnection connection;
	private static final String SERVER_URL = "http://fatedgame.cloudapp.net/Share/index.php";
	public static void Initialize() throws CommunicationException 
	{
		try {
			serverURL = new URL(SERVER_URL);
		} catch (MalformedURLException e) {
			throw new CommunicationException();
		}

		openConnection();
		receiveData();
		closeConnection();
	}
	
	public static long authenticate(String username, String password) throws CommunicationException
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
	public static ArrayList<GameEntry> getGames(long id)  throws CommunicationException
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
	public static GameState getGameState(long gId) throws CommunicationException, Exception
	{
		openConnection();
		
		String data = "op=2" +
				"&gid=" + gId;
		sendData(data);
		
		String res = receiveData();
		closeConnection();
		
		return GameState.fromJSONString(res);
	}
	
	private static void openConnection() throws CommunicationException 
	{
		try {
			connection = (HttpURLConnection)serverURL.openConnection();
			connection.setRequestMethod("POST");
		} catch (IOException e) {
			throw new CommunicationException(e.getMessage());
		}
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setChunkedStreamingMode(0);
	}
	private static void closeConnection()
	{
		connection.disconnect();
	}
	private static void sendData(String data) throws CommunicationException 
	{
		try{
			OutputStream out = new BufferedOutputStream(connection.getOutputStream());
			byte[] buffer = data.getBytes();
			out.write(buffer);
			out.close();
		} catch (IOException e) {
			throw new CommunicationException(e.getMessage());
		}
	}
	private static String receiveData() throws CommunicationException 
	{
		byte[] output = new byte[1];
		try{
			InputStream inp = new BufferedInputStream(connection.getInputStream());
			
			output = new byte[inp.available() + 1];
			inp.read(output);
			inp.close();
		} catch (IOException e) {
			throw new CommunicationException(e.getMessage());
		}
		
		String out = new String(output);
		return out.trim();
	}

	public static GameState startGame(String oUsername, int goal)  throws CommunicationException, Exception{
		openConnection();
		
		String data = "op=3" +
				"&username=" + Cookies.get("username") +
				"&password=" + Cookies.get("password") +
				"&oUsername=" + oUsername +
				"&goal=" + goal;
		sendData(data);
		
		String res = receiveData();
		closeConnection();
		
		try
		{
			int errorno = Integer.parseInt(res);
			throw new CommunicationException(String.valueOf(errorno));
		}
		catch(NumberFormatException e)
		{
			return GameState.fromJSONString(res);
		}
	}

	public static int playBodyCard(long gid, int toPlay, int toReplace)  throws CommunicationException{
		openConnection();
		
		String data = "op=4" +
				"&username=" + Cookies.get("username") +
				"&password=" + Cookies.get("password") +
				"&id=" + gid +
				"&move={\"cardidx\":" + toPlay +
				",\"creatureidx\":" + toReplace +"}";
		sendData(data);
		
		String res = receiveData();
		closeConnection();
		
		int result = Integer.parseInt(res);
		if (result < 0) throw new CommunicationException("Error #" + result);
		return result;
	}
	public static int discardCard(long gid, int idx) throws CommunicationException {
		openConnection();
		
		String data = "op=5" +
				"&username=" + Cookies.get("username") +
				"&password=" + Cookies.get("password") +
				"&id=" + gid +
				"&cardidx=" + idx;
		sendData(data);
		
		String res = receiveData();
		closeConnection();
		
		int result = Integer.parseInt(res);
		if (result < 0) throw new CommunicationException("Error #" + result);
		return result;
	}
}
