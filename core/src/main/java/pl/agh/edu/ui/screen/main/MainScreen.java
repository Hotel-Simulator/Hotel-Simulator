package pl.agh.edu.ui.screen.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.Engine;
import pl.agh.edu.ui.component.background.InfinityBackground;
import pl.agh.edu.ui.component.button.OptionButton;
import pl.agh.edu.ui.component.modal.ModalManager;
import pl.agh.edu.ui.component.navbar.NavbarBottom;
import pl.agh.edu.ui.component.navbar.NavbarTop;
import pl.agh.edu.ui.frame.FrameStack;
import pl.agh.edu.ui.shader.BlurShader;

public class MainScreen {

	public static Engine engine = null;
	public final FrameStack frameStack = new FrameStack();
	private final Stage mainStage = new Stage(GraphicConfig.getViewport());
	private final Stage middleStage = new Stage(GraphicConfig.getViewport());
	private final Stage topStage = new Stage(GraphicConfig.getViewport());
	private final BlurShader blurShader = new BlurShader(mainStage, middleStage);
	private final MainScreenInputAdapter inputMultiplexer = new MainScreenInputAdapter(mainStage);
	private final ModalManager modalManager = ModalManager.initialize(ModalManager.ModalPreferences.of(inputMultiplexer, blurShader, mainStage, topStage));
	private final InfinityBackground infinityBackground = new InfinityBackground("night-city");

	public MainScreen() {
		setupUI();
	}

	private void setupUI() {
		Table table = new Table();

		table.setFillParent(true);
		table.add().uniform();
		table.add(new NavbarTop("default")).growX();
		table.add(new OptionButton()).uniform();
		table.row();
		table.add();
		table.add(frameStack).grow().center();
		table.add();
		table.row();
		table.add();
		table.add(new NavbarBottom("default", this)).growX();
		table.add();

		mainStage.addActor(stack);
		middleStage.addActor(blurShader);
		topStage.addActor(modalManager);

		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void show() {}

	@Override
	public void render(float delta) {
		mainStage.act();
		mainStage.draw();
		if (blurShader.isActive()) {
			blurShader.render();
		}
		if (modalManager.isModalOpen()) {
			topStage.act();
			topStage.draw();
		}

		engine.time.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) {
		mainStage.getViewport().update(width, height, true);
		middleStage.getViewport().update(width, height, true);
		topStage.getViewport().update(width, height, true);
		blurShader.resize();
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {}

}
