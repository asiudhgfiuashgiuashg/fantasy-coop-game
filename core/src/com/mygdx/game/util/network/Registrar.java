package com.mygdx.game.util.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.mygdx.game.util.network.messages.Serializeable;
import org.reflections.Reflections;


/**
 * Called by client and server to register classes with Kryo as per this link:
 *  https://github.com/EsotericSoftware/kryonet#registering-classes
 * Every class that you want to send over the network must implement Serializeable.
 * Serializeables will be automatically registered with Kryo.
 * Created by elimonent on 8/27/16.
 */
public class Registrar {
	/**
	 * register all serializeable classes to this endpoint (an endpoint is a client or server)
	 * @param endPoint
	 */
	public void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		//Every network message (Serializeable) class is automatically registered below.
		Reflections reflections = new Reflections("com.mygdx.game.util.network.messages");


		for (Class<? extends Serializeable> aClass: reflections.getSubTypesOf(Serializeable.class)) {
			kryo.register(aClass);
		}
	}
}
