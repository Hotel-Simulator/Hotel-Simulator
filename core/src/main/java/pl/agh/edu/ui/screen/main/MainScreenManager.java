package pl.agh.edu.ui.screen.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.engine.Engine;
import pl.agh.edu.ui.component.button.OptionButton;
import pl.agh.edu.ui.component.navbar.NavbarBottom;
import pl.agh.edu.ui.component.navbar.NavbarTop;
import pl.agh.edu.ui.frame.FrameStack;
import pl.agh.edu.ui.screen.GameScreen;
import pl.agh.edu.ui.utils.GameSkinProvider;

public class MainScreenManager implements GameSkinProvider {
	private static MainScreenManager instance;

	public final Engine engine;
	public final GameScreen gameScreen = new GameScreen("night-city", new MainScreenInputAdapter(), this::render, true);
	public final FrameStack frameStack = new FrameStack();

	private MainScreenManager(Engine engine) {
		this.engine = engine;
	}

	public static MainScreenManager initialize(Engine engine) {
		instance = new MainScreenManager(engine);
		return instance;
	}

	public static MainScreenManager getInstance() {
		return instance;
	}

	public void setupUI() {
		Table table = new Table();

		table.setFillParent(true);
		table.add().uniform();
		table.add(new NavbarTop("default")).growX();
		table.add(new OptionButton(getGameSkin(), "options")).uniform();
		table.row();
		table.add();
		table.add(frameStack).grow().center();
		table.add();
		table.row();
		table.add();
		table.add(new NavbarBottom("default", frameStack)).growX();
		table.add();

		gameScreen.setActor(table);
	}

	public void render(float delta) {
		engine.time.update(Gdx.graphics.getDeltaTime());
	}
}
