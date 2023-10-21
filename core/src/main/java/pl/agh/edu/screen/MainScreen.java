package pl.agh.edu.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import pl.agh.edu.GdxGame;
import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.button.OptionButton;
import pl.agh.edu.actor.component.navbar.NavbarBottom;
import pl.agh.edu.actor.component.navbar.NavbarTop;
import pl.agh.edu.actor.frame.BaseFrame;
import pl.agh.edu.actor.frame.OptionFrame;
import pl.agh.edu.actor.frame.TestFrame;
import pl.agh.edu.actor.shader.BlurShader;
import pl.agh.edu.actor.utils.ResolutionChangeListener;
import pl.agh.edu.actor.utils.ResolutionManager;
import pl.agh.edu.config.GraphicConfig;

public class MainScreen implements Screen, ResolutionChangeListener {
	private Cell<?> currentFrame;
	private final Skin skin = HotelSkin.getInstance();
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

	public MainScreen(GdxGame game) {
		setupUI();
	}

	private void setupUI() {
		Image background = new Image(skin.getDrawable("night-city"));
		background.setScaling(Scaling.stretch);
		background.setAlign(Align.center);

		stack.setFillParent(true);
		stack.add(background);

		table.setFillParent(true);
		table.add().uniform();
		table.add(new NavbarTop("default")).growX();
		table.add(new OptionButton(this::optionButtonHandler)).uniform();
		table.row();
		table.add();
		currentFrame = table.add();
		table.add();
		table.row();
		table.add();
		table.add(new NavbarBottom("default", this)).growX();
		table.add();

		stack.add(table);

		mainStage.addActor(stack);
		middleStage.addActor(blurShader);
		topStage.addActor(optionFrameContainer);

		ResolutionManager.addListener(this);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	public void changeFrame(BaseFrame frame) {
		currentFrame.setActor(frame).grow();
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
		inputMultiplexer.setProcessors(topStage);
		if (isOptionsOpen)
			return;
		blurShader.startBlur();
		isOptionsOpen = true;
		optionFrameContainer.setActor(optionFrame);
	}

	private void closeOptions() {
		inputMultiplexer.setProcessors(mainStage);
		if (!isOptionsOpen)
			return;
		blurShader.stopBlur();
		isOptionsOpen = false;
		optionFrameContainer.removeActor(optionFrame);
	}

	@Override
	public void show() {
		changeFrame(new TestFrame("test"));
	}

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
