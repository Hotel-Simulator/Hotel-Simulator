package pl.agh.edu.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.GdxGame;
import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.component.background.InfinityBackground;
import pl.agh.edu.actor.component.button.OptionButton;
import pl.agh.edu.actor.component.navbar.NavbarBottom;
import pl.agh.edu.actor.component.navbar.NavbarTop;
import pl.agh.edu.actor.frame.BaseFrame;
import pl.agh.edu.actor.frame.OptionFrame;
import pl.agh.edu.actor.frame.TestFrame;
import pl.agh.edu.actor.shader.BlurShader;
import pl.agh.edu.actor.utils.resolution.ResolutionChangeListener;
import pl.agh.edu.actor.utils.resolution.ResolutionManager;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.model.time.Time;

public class MainScreen implements Screen, ResolutionChangeListener {
	private final Stack currentFrameStack = new Stack();
	private final Skin skin = GameSkin.getInstance();
	private final Stack stack = new Stack();
	private final Container<OptionFrame> optionFrameContainer = new Container<>();
	private final Table table = new Table();
	private boolean isOptionsOpen = false;
	private final OptionFrame optionFrame = new OptionFrame(this::closeOptions);
	private final Stage mainStage = new Stage(GraphicConfig.getViewport());
	private final Stage middleStage = new Stage(GraphicConfig.getViewport());
	private final Stage topStage = new Stage(GraphicConfig.getViewport());
	private final BlurShader blurShader = new BlurShader(mainStage);
	private final InputMultiplexer inputMultiplexer = new InputMultiplexer(mainStage);
	private final InfinityBackground infinityBackground = new InfinityBackground("night-city");

	public MainScreen(GdxGame game) {
		setupUI();
	}

	private void setupUI() {
		stack.setFillParent(true);
		stack.addActor(infinityBackground);

		table.setFillParent(true);
		table.add().uniform();
		table.add(new NavbarTop("default")).growX();
		table.add(new OptionButton(this::optionButtonHandler)).uniform();
		table.row();
		table.add();
		table.add(currentFrameStack).grow();
		table.add();
		table.row();
		table.add();
		table.add(new NavbarBottom("default", this)).growX();
		table.add();

		stack.add(table);

		mainStage.addActor(stack);
		middleStage.addActor(blurShader);
		topStage.addActor(optionFrameContainer);

		currentFrameStack.addActor(new TestFrame("Test"));

		ResolutionManager.addListener(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	public void changeFrame(BaseFrame newFrame) {
		BaseFrame oldFrame = (BaseFrame) currentFrameStack.getChild(0);
		currentFrameStack.addActor(newFrame);
		if (oldFrame != null)
			oldFrame.runHorizontalTrainOutAnimation();
		newFrame.runHorizontalTrainInAnimation();
	}

	private void updateBlurShaderState() {
		switch (blurShader.getStateOfTransition()) {
			case OPENING, CLOSING -> {
				actAndDrawAdditionalStages();
				blurShader.render();
			}
			case OPEN -> actAndDrawAdditionalStages();
		}
	}

	private void actAndDrawAdditionalStages() {
		middleStage.act();
		middleStage.draw();
		topStage.act();
		topStage.draw();
	}

	private void actAndDrawStages() {
		mainStage.act();
		mainStage.draw();
	}

	private void optionButtonHandler() {
		if (isOptionsOpen)
			closeOptions();
		else
			openOptions();
	}

	private void openOptions() {
		if (isOptionsOpen)
			return;
		Time.getInstance().stop();
		inputMultiplexer.setProcessors(topStage);
		isOptionsOpen = true;
		optionFrameContainer.setActor(optionFrame);
		blurShader.startBlur();
		optionFrame.runVerticalFadeInAnimation();
	}

	private void closeOptions() {
		if (!isOptionsOpen)
			return;
		inputMultiplexer.setProcessors(mainStage);
		isOptionsOpen = false;
		blurShader.stopBlur();
		optionFrame.runVerticalFadeOutAnimation();
	}

	@Override
	public void show() {}

	@Override
	public void render(float delta) {
		actAndDrawStages();
		updateBlurShaderState();
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
