package com.mygdx.game.util;

import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.LogLevel;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Public functions in this class are accessible via the developer console.
 * Arguments must be primitive or Strings.
 * No two methods of the same name can have the same number of parameters.
 * Created by elimonent on 8/21/16.
 */
public class ConcreteCommandExecutor extends CommandExecutor {
	public void connect(String ip, int port) {
		console.log("Attempting to connect to" + ip + ":" + port);
		try {
			InetAddress iNetIP = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			console.log(e.getMessage(), LogLevel.ERROR);
		}
		//TODO
	}

	/**
	 * host a server on this computer
	 * @param port
	 */
	public void startServer(int port) {
		console.log("Starting server on port " + port + " ...");
	}
}
