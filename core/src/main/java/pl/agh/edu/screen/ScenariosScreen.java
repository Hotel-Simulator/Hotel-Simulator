package pl.agh.edu.screen;

import java.util.ArrayList;
import java.util.Optional;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import pl.agh.edu.GdxGame;
import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.component.button.DifficultyButton;
import pl.agh.edu.actor.component.button.MyInputAdapter;
import pl.agh.edu.actor.component.button.ScenarioButton;
import pl.agh.edu.actor.utils.ScenariosSettings;
import pl.agh.edu.actor.utils.resolution.ResolutionChangeListener;
import pl.agh.edu.actor.utils.resolution.ResolutionManager;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.enums.DifficultyLevel;
import pl.agh.edu.enums.HotelType;
import pl.agh.edu.language.LanguageChangeListener;
import pl.agh.edu.language.LanguageManager;

public class ScenariosScreen implements Screen, ResolutionChangeListener, LanguageChangeListener {
	private Stage stage = new Stage(GraphicConfig.getViewport());
	public final Skin skin = GameSkin.getInstance();

	private Table mainTable = new Table();
	private Table difficultyTable;
	private Table scenariosTable;
	private final GdxGame game;
	private final ScenarioScreenTexts texts = new ScenarioScreenTexts();

	public final NinePatchDrawable upButton = new NinePatchDrawable(skin.getPatch("button"));
	public final NinePatchDrawable downButton = new NinePatchDrawable(skin.getPatch("button_selected"));
	private Label.LabelStyle titleLabelStyle;
	private boolean isLastScreenScenarios = true;

	// for difficulty
	private ArrayList<DifficultyButton> difficultyButtons = new ArrayList<>();
	private int width = GraphicConfig.getResolution().WIDTH;
	private int height = GraphicConfig.getResolution().HEIGHT;
	public Optional<DifficultyButton> selectedDifficultyButton = Optional.empty();
	public final ScenariosSettings scenariosSettings = new ScenariosSettings();

	// for scenarios
	private ArrayList<ScenarioButton> scenarioButtons = new ArrayList<>();
	public Optional<ScenarioButton> selectedScenarioButton = Optional.empty();
	private Label errorLabel;

	public ScenariosScreen(GdxGame game) {
		this.game = game;

		onLanguageChange();
		createFrames();

		ResolutionManager.addListener(this);
		Gdx.input.setInputProcessor(new MyInputAdapter(stage));
	}

	public void createFrames() {
		stage.getViewport().update(width, height, true);

		scenariosSettings.setParams();
		mainTable = new Table();
		mainTable.setFillParent(true);
		mainTable.background(skin.getDrawable("hotel-room"));
		difficultyTable = new Table();
		scenariosTable = new Table();
		createDifficultyFrame();
		createScenariosFrame();

		stage.addActor(mainTable);
		if (isLastScreenScenarios) {
			mainTable.add(scenariosTable).growX();
		} else {
			mainTable.add(difficultyTable).growX();
		}
	}

	public void createDifficultyFrame() {
		difficultyTable.left();
		difficultyTable.setFillParent(true);
		titleLabelStyle = createTitleLabel();

		Label label1 = new Label(texts.difficultyTitle, titleLabelStyle);

		Table titleTable = new Table();
		titleTable.setBackground(upButton);
		titleTable.add(label1);
		titleTable.pad(label1.getHeight() / 4, label1.getWidth() / 12, label1.getHeight() / 4, label1.getWidth() / 12);

		difficultyTable.add(titleTable).left().padLeft((int) (width / 12)).padTop((int) (scenariosSettings.getLargePaddingMultiplier() * height / 12)).expandX();

		createDifficultyButtons();
		addDifficultyButtonsListeners();
		difficultyTable.row();
		difficultyTable.row();

		Button back = new TextButton(texts.difficultyBackButton, scenariosSettings.getPlayBackButtonStyle());
		back.pad(back.getHeight() / 4, back.getWidth() / 6, back.getHeight() / 4, back.getWidth() / 6);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				mainTable.clearChildren();
				isLastScreenScenarios = true;
				mainTable.add(scenariosTable).growX();
			}
		});

		Table playBack = new Table();
		playBack.add(back).padRight(3 * width / 5);

		Button play = new TextButton(texts.difficultyPlayButton, scenariosSettings.getPlayBackButtonStyle());
		play.pad(play.getHeight() / 4, play.getWidth() / 6, play.getHeight() / 4, play.getWidth() / 6);
		play.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				if (selectedScenarioButton.isPresent() && selectedDifficultyButton.isPresent()) {
					game.changeScreen(new MainScreen(game));
				} else {
					difficultyTable.add(errorLabel);
				}

			}
		});

		playBack.add(play);
		difficultyTable.add(playBack).padTop((float) (scenariosSettings.getLargePaddingMultiplier() * height / 30));
		difficultyTable.row().padTop((float) (scenariosSettings.getLargePaddingMultiplier() * 10));

	}

	private Label.LabelStyle createTitleLabel() {
		Label.LabelStyle titleLabel = GameSkin.getInstance().get(scenariosSettings.getTitleFont().toString(), Label.LabelStyle.class);
		titleLabel.fontColor = Color.YELLOW;
		return titleLabel;
	}

	public void createDifficultyButtons() {
		String[] names = {texts.difficultyEasy, texts.difficultyMedium, texts.difficultyHard, texts.difficultyBrutal};
		DifficultyLevel[] names2 = DifficultyLevel.values();

		for (int i = 0; i < 4; i++) {
			difficultyTable.row();
			DifficultyButton myButton = new DifficultyButton(names[i], new TextButton.TextButtonStyle(scenariosSettings.getDifficultyButtonStyle()), names2[i], scenariosSettings);
			this.difficultyButtons.add(myButton);
			myButton.pad((scenariosSettings.getDiffHeight() - myButton.getHeight()) / 2, (scenariosSettings.getDiffWidth() - myButton.getWidth()) / 2, (scenariosSettings
					.getDiffHeight() - myButton.getHeight()) / 2, (scenariosSettings.getDiffWidth() - myButton.getWidth())
							/ 2);
			difficultyTable.add(myButton).left().padTop((int) (scenariosSettings.getLargePaddingMultiplier() * height / 24)).padLeft((int) (width / 6 + i * width / 24));
		}
	}

	public Label createErrorLabel() {
		Label.LabelStyle style = GameSkin.getInstance().get("white-h4", Label.LabelStyle.class);
		style.fontColor = Color.RED;

		return new Label(texts.difficultyErrorLabel, style);
	}

	public void addDifficultyButtonsListeners() {
		for (DifficultyButton button : difficultyButtons) {
			button.addListener(
					new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {

							selectedDifficultyButton.ifPresent(difficultyButton -> difficultyButton.setChecked(false));

							selectedDifficultyButton = Optional.of(button);
							selectedDifficultyButton.get().setChecked(true);

							if (selectedScenarioButton.isPresent() && selectedDifficultyButton.isPresent()) {
								difficultyTable.removeActor(errorLabel);
								difficultyTable.row().clearActor();
							}
						}
					});
		}
	}

	public void createScenariosFrame() {
		scenariosTable.top();
		scenariosTable.setFillParent(true);
		errorLabel = createErrorLabel();

		Label label1 = new Label(texts.scenarioTitle, titleLabelStyle);

		Table titleTable = new Table();
		titleTable.setBackground(upButton);
		titleTable.add(label1);
		titleTable.pad(label1.getHeight() / 4, label1.getWidth() / 12, label1.getHeight() / 4, label1.getWidth() / 12);

		scenariosTable.add(titleTable).center().padTop((int) (scenariosSettings.getLargePaddingMultiplier() * height / 10)).padBottom((int) (height / 26)).expandX();

		scenariosTable.row();

		Table buttons = new Table();
		buttons.add(addScenariosButton(texts.scenarioResortTitle, "water", texts.scenarioResortDescription, texts.scenarioResortPopularity, HotelType.RESORT));
		buttons.add(addScenariosButton(texts.scenarioSanatoriumTitle, "hospital", texts.scenarioSanatoriumDescription, texts.scenarioSanatoriumPopularity, HotelType.SANATORIUM))
				.padLeft((int) (width / 12));
		buttons.add(addScenariosButton(texts.scenarioHotelTitle, "hotel", texts.scenarioHotelDescription, texts.scenarioHotelPopularity, HotelType.HOTEL)).padLeft((int) (width
				/ 12));
		scenariosTable.add(buttons).padTop((int) (scenariosSettings.getLargePaddingMultiplier() * height / 24));

		scenariosTable.row();

		TextButton.TextButtonStyle playStyle = new TextButton.TextButtonStyle();
		playStyle.up = upButton;
		playStyle.down = downButton;
		playStyle.font = skin.getFont("white-h1"); // Set the custom font
		playStyle.fontColor = Color.YELLOW; // Set the font color

		Button next = new TextButton(texts.scenarioNextButton, playStyle);
		next.pad(15);
		next.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (selectedScenarioButton.isPresent() && selectedDifficultyButton.isPresent()) {
					difficultyTable.removeActor(errorLabel);
				}
				mainTable.clearChildren();
				isLastScreenScenarios = false;
				mainTable.add(difficultyTable).growX();
			}
		});

		scenariosTable.add(next).right().padRight((int) (width / 12)).padTop((int) (scenariosSettings.getLargePaddingMultiplier() * height / 16));
	}

	public ScenarioButton addScenariosButton(String title, String image, String description, String season, HotelType hotelType) {
		ScenarioButton scenario = new ScenarioButton(title, image, description, season, hotelType, scenariosSettings);
		scenario.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (selectedScenarioButton.isPresent() && selectedScenarioButton.get() != scenario) {
					selectedScenarioButton.get().setChecked(false);
				}
				selectedScenarioButton = Optional.of(scenario);
				selectedScenarioButton.get().setChecked(true);
			}
		});
		scenarioButtons.add(scenario);

		return scenario;
	}

	@Override
	public void show() {
		// InputMultiplexer multiplexer = new InputMultiplexer();
		// multiplexer.addProcessor(stage);
		// Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render(float delta) {
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void onResolutionChange() {
		if (GraphicConfig.isFullscreen()) {
			this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			this.width = Gdx.graphics.getWidth();
			this.height = Gdx.graphics.getHeight();
		} else {
			this.resize(GraphicConfig.getResolution().WIDTH, GraphicConfig.getResolution().HEIGHT);
			this.width = GraphicConfig.getResolution().WIDTH;
			this.height = GraphicConfig.getResolution().HEIGHT;
		}
		scenariosSettings.setTypeAndDifficulty(selectedScenarioButton, selectedDifficultyButton);
		createFrames();
	}

	@Override
	public void onLanguageChange() {
		texts.difficultyTitle = LanguageManager.get("difficulty.title");
		texts.difficultyBackButton = LanguageManager.get("difficulty.back.button");
		texts.difficultyPlayButton = LanguageManager.get("difficulty.play.button");
		texts.difficultyEasy = LanguageManager.get("difficulty.easy");
		texts.difficultyMedium = LanguageManager.get("difficulty.medium");
		texts.difficultyHard = LanguageManager.get("difficulty.hard");
		texts.difficultyBrutal = LanguageManager.get("difficulty.brutal");
		texts.difficultyErrorLabel = LanguageManager.get("difficulty.error.label");
		texts.scenarioTitle = LanguageManager.get("scenario.title");
		texts.scenarioResortTitle = LanguageManager.get("scenario.resort.title");
		texts.scenarioResortDescription = LanguageManager.get("scenario.resort.description");
		texts.scenarioResortPopularity = LanguageManager.get("scenario.resort.popularity");
		texts.scenarioHotelTitle = LanguageManager.get("scenario.hotel.title");
		texts.scenarioHotelDescription = LanguageManager.get("scenario.hotel.description");
		texts.scenarioHotelPopularity = LanguageManager.get("scenario.hotel.popularity");
		texts.scenarioSanatoriumTitle = LanguageManager.get("scenario.sanatorium.title");
		texts.scenarioSanatoriumDescription = LanguageManager.get("scenario.sanatorium.description");
		texts.scenarioSanatoriumPopularity = LanguageManager.get("scenario.sanatorium.popularity");
		texts.scenarioNextButton = LanguageManager.get("scenario.next.button");
	}

	private static class ScenarioScreenTexts {
		public String difficultyTitle;
		public String difficultyBackButton;
		public String difficultyPlayButton;
		public String difficultyEasy;
		public String difficultyMedium;
		public String difficultyHard;
		public String difficultyBrutal;
		public String difficultyErrorLabel;
		public String scenarioTitle;
		public String scenarioResortTitle;
		public String scenarioResortDescription;
		public String scenarioResortPopularity;
		public String scenarioSanatoriumTitle;
		public String scenarioSanatoriumDescription;
		public String scenarioSanatoriumPopularity;
		public String scenarioHotelTitle;
		public String scenarioHotelDescription;
		public String scenarioHotelPopularity;
		public String scenarioNextButton;
	}
}
