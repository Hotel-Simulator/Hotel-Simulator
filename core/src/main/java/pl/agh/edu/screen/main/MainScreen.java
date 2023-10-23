package pl.agh.edu.screen.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.GdxGame;
import pl.agh.edu.actor.component.background.InfinityBackground;
import pl.agh.edu.actor.component.button.OptionButton;
import pl.agh.edu.actor.component.modal.event.EventWrapper;
import pl.agh.edu.actor.component.modal.options.OptionsWrapper;
import pl.agh.edu.actor.component.navbar.NavbarBottom;
import pl.agh.edu.actor.component.navbar.NavbarTop;
import pl.agh.edu.actor.frame.FrameStack;
import pl.agh.edu.actor.shader.BlurShader;
import pl.agh.edu.actor.utils.resolution.ResolutionChangeListener;
import pl.agh.edu.actor.utils.resolution.ResolutionManager;
import pl.agh.edu.config.GraphicConfig;

public class MainScreen implements Screen, ResolutionChangeListener {
	private final GdxGame game;
	private final Stage mainStage = new Stage(GraphicConfig.getViewport());
	private final Stage middleStage = new Stage(GraphicConfig.getViewport());
	private final Stage topStage = new Stage(GraphicConfig.getViewport());
	public final FrameStack frameStack = new FrameStack();
	private final BlurShader blurShader = new BlurShader(mainStage, middleStage);
	private final MainScreenInputAdapter inputMultiplexer = new MainScreenInputAdapter(mainStage);
	public final OptionsWrapper optionsWrapper = new OptionsWrapper(inputMultiplexer, blurShader, mainStage, topStage);
	private final InfinityBackground infinityBackground = new InfinityBackground("night-city");
	private final EventWrapper eventWrapper = new EventWrapper(inputMultiplexer, blurShader, mainStage, topStage);

	public MainScreen(GdxGame game) {
		this.game = game;
		game.engine.eventHandler.setEventHandlerFunction(eventWrapper::showEvent);
		inputMultiplexer.setOpenOptionsAction(optionsWrapper.getOptionHandler());
		setupUI();
	}

	private void setupUI() {
		Stack stack = new Stack();
		Table table = new Table();

		stack.setFillParent(true);
		stack.addActor(infinityBackground);

		table.setFillParent(true);
		table.add().uniform();
		table.add(new NavbarTop("default")).growX();
		table.add(new OptionButton(optionsWrapper.getOptionHandler())).uniform();
		table.row();
		table.add();
		table.add(frameStack).grow().center();
		table.add();
		table.row();
		table.add();
		table.add(new NavbarBottom("default", this)).growX();
		table.add();
		stack.add(table);

		mainStage.addActor(stack);
		middleStage.addActor(blurShader);
		topStage.addActor(eventWrapper);
		topStage.addActor(optionsWrapper);

		ResolutionManager.addListener(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void show() {}

	@Override
	public void render(float delta) {
		mainStage.act();
		mainStage.draw();
		blurShader.render();
		if (eventWrapper.isEventOpen() || optionsWrapper.isModalOpen()) {
			topStage.act();
			topStage.draw();
		}
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

	@Override
	public void onResolutionChange() {
		if (GraphicConfig.isFullscreen())
			this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		else
			this.resize(GraphicConfig.getResolution().WIDTH, GraphicConfig.getResolution().HEIGHT);
	}
}
