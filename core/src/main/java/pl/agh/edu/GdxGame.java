package pl.agh.edu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.config.LanguageConfig;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.screen.GameScreenManager;

public class GdxGame extends ApplicationAdapter {
	public final GameScreenManager gameScreenManager = new GameScreenManager();

	@Override
	public void create() {
		LanguageManager.updateLanguage();

		LanguageConfig.setLanguage(LanguageConfig.getLanguage());
		GraphicConfig.setFullscreenMode(GraphicConfig.isFullscreen());
		GraphicConfig.changeResolution(GraphicConfig.getResolution());

		gameScreenManager.showWelcomeScreen();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gameScreenManager.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) {
		gameScreenManager.resize(width, height);
	}

	@Override
	public void dispose() {
		gameScreenManager.dispose();
	}

}
