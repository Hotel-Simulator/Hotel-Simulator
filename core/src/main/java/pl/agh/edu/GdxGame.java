package pl.agh.edu;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.config.LanguageConfig;
import pl.agh.edu.language.LanguageManager;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.screen.ScenariosScreen;

public class GdxGame extends ApplicationAdapter {

	private Screen currentScreen;
	private Screen previousScreen;
	private final Time time = Time.getInstance();

	@Override
	public void create() {
		LanguageManager.updateLanguage();

		currentScreen = new ScenariosScreen(this);
		setScreen(currentScreen);

		LanguageConfig.setLanguage(LanguageConfig.getLanguage());
		GraphicConfig.setFullscreenMode(GraphicConfig.isFullscreen());
		GraphicConfig.changeResolution(GraphicConfig.getResolution());
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (currentScreen != null) {
			currentScreen.render(Gdx.graphics.getDeltaTime());
		}

		time.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) {
		changeScreen(currentScreen);
		if (currentScreen != null) {
			currentScreen.resize(width, height);
		}
	}

	@Override
	public void dispose() {
		if (currentScreen != null) {
			currentScreen.dispose();
		}
	}

	public void setScreen(Screen screen) {
		if (currentScreen != null) {
			currentScreen.hide();
			currentScreen.dispose();
		}
		currentScreen = screen;
		currentScreen.show();
	}

	public void changeScreen(Screen screen) {
		previousScreen = currentScreen;
		currentScreen = screen;
		setScreen(screen);
	}

	public void changeScreenBack() {
		Screen temp = currentScreen;
		currentScreen = previousScreen;
		previousScreen = temp;
		setScreen(currentScreen);
	}
}
