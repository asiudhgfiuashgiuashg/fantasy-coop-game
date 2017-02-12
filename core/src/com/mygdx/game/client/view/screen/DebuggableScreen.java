package com.mygdx.game.client.view.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A screen that can render debug information
 * All screens should extend this.
 */
public abstract class DebuggableScreen extends ScreenAdapter
{
	/**
	 * GUI skin
	 */
	protected Skin skin;
	
	protected Stage stage;

	public DebuggableScreen(Viewport viewport, Batch batch) {
		this.stage = new Stage(viewport, batch);
	}
	
	
	public abstract void toggleDebug();
	
	/**
	 * This method updates all the UI elements within a screen. Do not use this method
	 * in the create method of a screen. It can potentially cause NullPointExceptions
	 */
	public abstract void updateUI();
	
	/**
	 * dispose of this screen's stage (gui) and remove the stage as an input processor.
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		stage.dispose();
	}


	public Stage getStage() {
		return stage;
	}
	
}
