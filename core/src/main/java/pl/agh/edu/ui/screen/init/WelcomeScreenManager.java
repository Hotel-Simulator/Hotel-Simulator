package pl.agh.edu.ui.screen.init;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.ui.component.button.OptionButton;
import pl.agh.edu.ui.screen.GameScreen;
import pl.agh.edu.ui.screen.main.MainScreenInputAdapter;
import pl.agh.edu.ui.utils.GameSkinProvider;

public class WelcomeScreenManager implements GameSkinProvider {
	public final GameScreen gameScreen = new GameScreen("hotel-room", new MainScreenInputAdapter());

	public WelcomeScreenManager() {
		Table table = new Table();
		table.add(new OptionButton(getGameSkin(), "options")).top().right().expandX().uniform().row();
		table.add(new GameStartContainer()).grow().row();
		table.add().uniform();
		gameScreen.setActor(table);
	}

}
