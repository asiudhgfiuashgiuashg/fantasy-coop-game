package com.mygdx.game.util;

import com.esotericsoftware.kryonet.Client;
import com.mygdx.game.client.GameClient;
import com.strongjoshua.console.GUIConsole;
import com.strongjoshua.console.LogLevel;

/**
 * Singleton version of {@link GUIConsole} created so that every class can access the on-screen console to print errors/debug info.
 * Created by elimonent on 8/21/16.
 */
public class SingletonGUIConsole extends GUIConsole {
	private static SingletonGUIConsole instance;
	private final Object logLock = new Object(); //calls to various log() functions are synchronized on this object
	private static final Object instanceLock = new Object();
	private SingletonGUIConsole() {

	}

	@Override
	public void log(String msg, LogLevel level) {
		synchronized (logLock) {
			super.log(msg, level);
		}
	}

	@Override
	public void log(String msg) {
		synchronized (logLock) {
			super.log(msg);
		}
	}


	public static SingletonGUIConsole getInstance() {
		synchronized (instanceLock) {
			if (null == instance) {
				instance = new SingletonGUIConsole();
			}
			return instance;
		}
	}
}
