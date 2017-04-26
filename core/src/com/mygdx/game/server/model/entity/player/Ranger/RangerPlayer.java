package com.mygdx.game.server.model.entity.player.Ranger;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.server.model.GameServer;
import com.mygdx.game.server.model.entity.player.Player;
import com.mygdx.game.shared.model.UniqueIDAssigner;

public class RangerPlayer extends Player {
	public RangerPlayer(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
	}

	@Override
	public void attack(Vector2 destination) {
		String uid = UniqueIDAssigner.generateDynamicEntityUID(RangerArrow.class.getSimpleName());
		RangerArrow arrow = new RangerArrow(uid, getPosition(), 0, true, destination);
		GameServer.getInstance().getMap().addFriendly(arrow);
		GameServer.getInstance().sendToAll(GameServer.getDynamicEntityInitMsg(arrow));
	}
}
