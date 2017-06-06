package com.mygdx.game.client.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.model.GameClient;
import com.mygdx.game.client.view.renderer.CustomTiledMapRenderer;
import com.mygdx.game.shared.model.DialogueLine;


public class GameScreen extends DebuggableScreen {
	private final GameClient game;
	private CustomTiledMapRenderer renderer;
	/** Game camera. Controls the part of the game map being shown via position and zoom */
	private OrthographicCamera camera;

	private Image dialogueFrameTopBottom;
	private long timeStartedDialogueDisplay; // when the dialogue line began to be displayed
	private long timeToDisplayDialogue; // how long in milliseconds the current dialogue should be displayed before it is removed from the screen.
	private Label dialogueLabel; // used to display the dialogue text;

	public GameScreen(Viewport gameViewport, Viewport uiViewport, Batch batch) {
		super(uiViewport, batch);
		this.camera = (OrthographicCamera) gameViewport.getCamera();
		game = GameClient.getInstance();
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		// TEST: Just an example of positioning the camera; completely arbitrary
		camera.position.x = 120;
		camera.position.y = 120;
		camera.zoom = 1.2f;

		dialogueFrameTopBottom = new Image(new Texture(Gdx.files.internal("dialogueFrameTopBottom.png")));
		dialogueFrameTopBottom.setPosition(0, 0);

		dialogueLabel = new Label("Placeholder", skin);
		dialogueLabel.setFontScale(1.3f);
		dialogueLabel.setPosition(90, dialogueFrameTopBottom.getHeight() / 2 + dialogueFrameTopBottom.getY());
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		// Update camera projection matrix
		camera.update();

		// Render based on game camera position and zoom
		renderer.setView(camera);
		renderer.render();
		
		// Draw UI elements (HUD)
		stage.draw();

		if (TimeUtils.timeSinceMillis(timeStartedDialogueDisplay) > timeToDisplayDialogue) {
			removeDialogueStuff();
		}
	}

	/**
	 * Used to get rid of dialogue screen when dialogue line no longer needs displayed.
	 */
	private void removeDialogueStuff() {
		stage.getActors().removeValue(dialogueFrameTopBottom, true);
		stage.getActors().removeValue(dialogueLabel, true);
	}

	@Override
	public void toggleDebug() {
		renderer.toggleDebug();
	}

	@Override
	public void updateUI() {
		// TODO Auto-generated method stub
	}

	/**
	 * @param renderer used to draw the world
	 */
	public void setRenderer(CustomTiledMapRenderer renderer) {
		this.renderer = renderer;
	}

	/**
	 * Display a dialogue line on the bottom of the screen for a period of time.
	 * @param line contains the msg to display and how long to display it for.
	 */
	public void displayDialogue(DialogueLine line) {
		stage.addActor(dialogueFrameTopBottom);
		timeStartedDialogueDisplay = TimeUtils.millis();
		timeToDisplayDialogue = line.displayTime;
		dialogueLabel.setText(line.msg);
		stage.addActor(dialogueLabel);
	}
}
