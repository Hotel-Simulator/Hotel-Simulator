package pl.agh.edu;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import pl.agh.edu.model.console.CommandExecutor;
import pl.agh.edu.screen.ConsoleScreen;
import pl.agh.edu.screen.MainScreen;
import pl.agh.edu.model.Time;

public class GdxGame extends ApplicationAdapter {

	private Screen currentScreen;
	private Screen previousScreen;
	private ConsoleScreen consoleScreen;
	private Time time;
	private CommandExecutor commandExecutor;

	@Override
	public void create() {
		time = Time.getInstance();
		commandExecutor=CommandExecutor.getInstance();
		currentScreen = new MainScreen(this);
		setScreen(currentScreen);
		consoleScreen = new ConsoleScreen(this);
		previousScreen = consoleScreen;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Delegate render to the current screen
		if (currentScreen != null) {
			currentScreen.render(Gdx.graphics.getDeltaTime());
		}

		time.update(Gdx.graphics.getDeltaTime());
		commandExecutor.executeCommands();
	}

	@Override
	public void resize(int width, int height) {
		if (currentScreen != null) {
			currentScreen.resize(width, height);
		}
	}

	@Override
	public void dispose () {
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
		currentScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void changeScreen(Screen screen){
		previousScreen = currentScreen;
		currentScreen = screen;
		setScreen(screen);
	}

	public void changeScreenBack(){
		Screen temp = currentScreen;
		currentScreen = previousScreen;
		previousScreen = temp;
		setScreen(currentScreen);
	}

	public void changeScreenToConsole(){
		previousScreen = currentScreen;
		currentScreen = consoleScreen;
		setScreen(consoleScreen);
	}

}