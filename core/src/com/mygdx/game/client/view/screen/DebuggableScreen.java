package com.mygdx.game.client.view.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A screen that can render debug information All screens should extend this.
 */
public abstract class DebuggableScreen extends ScreenAdapter {

	/** GUI skin */
	protected Skin skin;
	/** Stage for UI elements */
	protected Stage stage;

	/**
	 * Initializes stage with viewport and batch
	 * 
	 * @param viewport
	 * @param batch
	 */
	public DebuggableScreen(Viewport viewport, Batch batch) {
		this.stage = new Stage(viewport, batch);
	}
	
	/** Toggles debug info on the screen */
	public abstract void toggleDebug();

	/**
	 * This method updates all the UI elements within a screen. Do not use this
	 * method in the create method of a screen. It can potentially cause
	 * NullPointExceptions
	 */
	public abstract void updateUI();

	/**
	 * Gets the stage that holds UI elements
	 * 
	 * @return stage
	 */
	public Stage getStage() {
		return stage;
	}
	
	/**
	 * Dispose of this screen's stage
	 * 
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		stage.dispose();
	}

}
