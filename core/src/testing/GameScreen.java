package testing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

/**
 * (Insert a comment that briefly describes the purpose of this class definition.)
 *
 * <p/> Bugs: (List any known issues or unimplemented features here)
 * 
 * @author (Bhavishya Shah)
 *
 */
public class GameScreen implements Screen
{
	final Test game;
	
	Texture oldMan;
	Music friend;
	
	OrthographicCamera cam;
	
	GameScreen(final Test game) {
		this.game = game;
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 800, 480);
		cam.zoom = 2.5f; 
		
		oldMan = new Texture(Gdx.files.internal("old-man.jpg"));
		friend = Gdx.audio.newMusic(Gdx.files.internal("friend in me.mp3"));
	
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show()
	{
		friend.play();
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        cam.zoom -= .001f;
        if (cam.zoom <= 0) {
        	cam.rotate(.5f, 0, 0, 10);
        	cam.translate(0, .3f);
        }
        cam.update();
        
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(oldMan, 400 - oldMan.getWidth()/2, 240 - oldMan.getHeight()/2);
        game.batch.end();
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause()
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume()
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide()
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose()
	{
		oldMan.dispose();
		friend.dispose();
	}
	
	
}
