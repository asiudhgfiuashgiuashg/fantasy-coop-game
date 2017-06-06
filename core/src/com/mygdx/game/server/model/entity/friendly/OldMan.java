package com.mygdx.game.server.model.entity.friendly;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.server.model.GameServer;
import com.mygdx.game.server.model.entity.player.Player;
import com.mygdx.game.shared.model.DialogueLine;
import com.mygdx.game.shared.model.EntityLight;
import com.mygdx.game.shared.model.CollideablePolygon;
import com.mygdx.game.shared.network.GameMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * AI for highwayman (from prototype plot in google drive)
 * Far from complete right now.
 */
public class OldMan extends Friendly {

	private float[] hitbox = {13, -3, 3, -3, 3, 5, 13, 5};
	private final static float MASS = 1000f; // affects movement

	private boolean setInitialAnimation = false;

	private long timeSinceAnimationChange = 0;
	private long directionDuration = 0; // how long to look in a direction

	private Random rand = new Random();
	private boolean beginSpeakingSequence = false;
	private GameMessage.SayMsg sayMsg; // fill this with dialogue and send to clients
	private List<DialogueLine> dialogueLines; // the lines of dialogue which the old man will say to the players sequentially
	private int nextLineIndex = 0; // the index of the next line to say to the players
	private long timeLineWasSaid; // when the current dialogue line was first displayed
	private long lineSayTime; // how long the current line should be displayed

	public OldMan(String uid, Vector2 position, int visLayer, boolean solid) {
		super(uid, position, visLayer, solid);
		setVertices(hitbox);
		setMass(MASS);
		frameDuration = 1f;
		sayMsg = new GameMessage.SayMsg();
		sayMsg.entityUID = this.getUid();
		// instantiate and populate the dialogue lines
		dialogueLines = new ArrayList<DialogueLine>();
		dialogueLines.add(new DialogueLine("Hello Travelers. Can you help me fix my broken cart?", 5000));
		dialogueLines.add(new DialogueLine("The handle broke off so I can't move it anymore.", 5000));
		dialogueLines.add(new DialogueLine("I don't want to be stuck here in the woods overnight if I can help it.", 5000));
		dialogueLines.add(new DialogueLine("Just kidding! Prepare to die!", 4000));
	}

	/**
	 * look in a random direction every once in a while
	 *
	 * @param elapsedTime
	 */
	@Override
	public void act(long elapsedTime) {
		super.act(elapsedTime);
		if (!setInitialAnimation) { // send the initial animation of the old man
			this.animationName = "down_facing";
			sendAnimation();
			setInitialAnimation = true;
			directionDuration = pickDirectionDuration();
		}
		timeSinceAnimationChange += elapsedTime;
		if (timeSinceAnimationChange >= directionDuration) {
			this.animationName = chooseRandomDirectionAnimation();
			sendAnimation();
			timeSinceAnimationChange = 0;
			directionDuration = pickDirectionDuration();
		}
		if (beginSpeakingSequence) {
			if (nextLineIndex < dialogueLines.size() && TimeUtils.timeSinceMillis(timeLineWasSaid) > lineSayTime) {
				DialogueLine line = dialogueLines.get(nextLineIndex);
				sayMsg.dialogueLine = line;
				GameServer.getInstance().sendToAll(sayMsg);
				timeLineWasSaid = TimeUtils.millis();
				lineSayTime = line.displayTime;
				nextLineIndex++;
			}
		}
	}

	/**
	 * choose a random direction animation for the old man to look in (1 of 4 directions possible)
	 *
	 * @return
	 */
	private String chooseRandomDirectionAnimation() {
		int max = 3;
		int min = 0;
		int randomNum = rand.nextInt((max - min) + 1) + min;
		if (0 == randomNum) {
			return "down_facing";
		} else if (1 == randomNum) {
			return "up_facing";
		} else if (2 == randomNum) {
			return "right_facing";
		} else {
			return "left_facing";
		}
	}

	/**
	 * choose a random amount of time to look in the next direction (milliseconds)
	 *
	 * @return
	 */
	private long pickDirectionDuration() {
		long minTime = 3000;
		long maxTime = 10000;

		return (long) (rand.nextDouble() * (maxTime - minTime) + minTime);
	}

	@Override
	public void onBumpInto(CollideablePolygon other) {

	}

	/**
	 * Trigger start talking to the players
	 * After the talking finishes the highwaymen should attack
	 * @param player the player who is interacting with this dynamic entity.
	 */
	@Override
	public void onInteractionFrom(Player player) {
		beginSpeakingSequence = true;
	}
}
