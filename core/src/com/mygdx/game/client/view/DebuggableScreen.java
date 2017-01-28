package com.mygdx.game.client.view;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * A screen that can render debug information
 * All screens shoudl extend this.
 */
public class DebuggableScreen extends ScreenAdapter
{
	/**
	 * gui stuff for each screen is drawn on that screen's stage
	 */
	protected Stage stage;


	/**
	 * upon construction, add the stage as an input processor to the multiplexer
	 * upon disposal, remove the stage from the input multiplexer - since the next screen will add their own stage
	 */
	protected InputMultiplexer inputMultiplexer;

	public void toggleDebug() {
		
	}
	
	public void updateUI() {
		
	}

	/**
	 * dispose of this screen's stage (gui) and remove the stage as an input processor.
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose()
	{
		inputMultiplexer.removeProcessor(stage);
		stage.dispose();
	}

}
