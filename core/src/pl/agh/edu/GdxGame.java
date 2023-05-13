package pl.agh.edu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import pl.agh.edu.command.CommandExecutor;
import pl.agh.edu.time.Time;

public class GdxGame extends ApplicationAdapter {

	private Time time;
	private CommandExecutor commandExecutor;

	@Override
	public void create() {
		time = Time.getInstance();
		commandExecutor=CommandExecutor.getInstance();
	}

	@Override
	public void render() {
		time.update(Gdx.graphics.getDeltaTime());
		commandExecutor.executeCommands();
	}

//	@Override
//	public void dispose() {
//	}
}