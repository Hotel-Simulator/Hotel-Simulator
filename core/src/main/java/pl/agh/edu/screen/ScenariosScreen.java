package pl.agh.edu.screen;

import java.util.ArrayList;

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

public class ScenariosScreen implements Screen, ResolutionChangeListener {
	private Stage stage = new Stage(GraphicConfig.getViewport());
	public final Skin skin = GameSkin.getInstance();

	private Table mainTable = new Table();
	private Table difficultyTable;
	private Table scenariosTable;
	private final GdxGame game;

	public final NinePatchDrawable upButton = new NinePatchDrawable(skin.getPatch("button"));
	public final NinePatchDrawable downButton = new NinePatchDrawable(skin.getPatch("button_selected"));
	private Label.LabelStyle titleLabelStyle;

	// for difficulty
	private ArrayList<DifficultyButton> difficultyButtons = new ArrayList<>();
	private int width = GraphicConfig.getResolution().WIDTH;
	private int height = GraphicConfig.getResolution().HEIGHT;
	public DifficultyButton selectedDifficultyButton = null;
	public final ScenariosSettings scenariosSettings = new ScenariosSettings();

	// for scenarios
	private ArrayList<ScenarioButton> scenarioButtons = new ArrayList<>();
	public ScenarioButton selectedScenarioButton = null;
	private Label errorLabel;

	public ScenariosScreen(GdxGame game) {
		this.game = game;

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
		mainTable.add(scenariosTable).growX();
	}

	public void createDifficultyFrame() {
		difficultyTable.left();
		difficultyTable.setFillParent(true);
		titleLabelStyle = createTitleLabel();

		Label label1 = new Label("Choose difficulty", titleLabelStyle);

		Table titleTable = new Table();
		titleTable.setBackground(upButton);
		titleTable.add(label1);
		titleTable.pad(label1.getHeight() / 4, label1.getWidth() / 12, label1.getHeight() / 4, label1.getWidth() / 12);

		difficultyTable.add(titleTable).left().padLeft((int) (width / 12)).padTop((int) (scenariosSettings.getLargePaddingMultiplier() * height / 12)).expandX();

		createDifficultyButtons(upButton, downButton);
		addDifficultyButtonsListeners();
		difficultyTable.row();
		difficultyTable.row();

		Button back = new TextButton("Back", scenariosSettings.getPlayBackButtonStyle());
		back.pad(back.getHeight() / 4, back.getWidth() / 6, back.getHeight() / 4, back.getWidth() / 6);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				mainTable.clearChildren();
				mainTable.add(scenariosTable).growX();
			}
		});

		Table playBack = new Table();
		playBack.add(back).padRight(3 * width / 5);

		Button play = new TextButton("Play", scenariosSettings.getPlayBackButtonStyle());
		play.pad(play.getHeight() / 4, play.getWidth() / 6, play.getHeight() / 4, play.getWidth() / 6);
		play.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				if (selectedScenarioButton != null && selectedDifficultyButton != null) {
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
		Label.LabelStyle titleLabel = GameSkin.getInstance().get(scenariosSettings.getTitleFont(), Label.LabelStyle.class);
		titleLabel.fontColor = Color.YELLOW;
		return titleLabel;
	}

	public void createDifficultyButtons(NinePatchDrawable buttonBackground, NinePatchDrawable onSelect) {
		String[] names = {"EASY", "MEDIUM", "HARD", "BRUTAL"};
		DifficultyLevel[] names2 = DifficultyLevel.values();

		for (int i = 0; i < 4; i++) {
			difficultyTable.row();
			DifficultyButton myButton = new DifficultyButton(names[i], new TextButton.TextButtonStyle(scenariosSettings.getDifficultyButtonStyle()), names2[i]);
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

		return new Label("Select scenario and difficulty", style);
	}

	public void addDifficultyButtonsListeners() {
		for (DifficultyButton button : difficultyButtons) {
			button.addListener(
					new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {

							if (selectedDifficultyButton != null) {
								selectedDifficultyButton.setChecked(false);
							}
							selectedDifficultyButton = button;
							selectedDifficultyButton.setChecked(true);

							if (selectedScenarioButton != null && selectedDifficultyButton != null) {
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

		Label label1 = new Label("Choose scenario", titleLabelStyle);

		Table titleTable = new Table();
		titleTable.setBackground(upButton);
		titleTable.add(label1);
		titleTable.pad(label1.getHeight() / 4, label1.getWidth() / 12, label1.getHeight() / 4, label1.getWidth() / 12);

		scenariosTable.add(titleTable).center().padTop((int) (scenariosSettings.getLargePaddingMultiplier() * height / 10)).padBottom((int) (height / 26)).expandX();

		scenariosTable.row();

		Table buttons = new Table();
		buttons.add(addScenariosButton("Seaside Resort", "water", "Most of the clients are people who are on vacation", "Summer", HotelType.RESORT, scenariosSettings
				.getScenarioTitleFont(),
				scenariosSettings.getDifficultyButtonStyle().font.toString()));
		buttons.add(addScenariosButton("Sanatorium Ciechocinek", "hospital", "Clients are mostly patients but not only", "Autumn", HotelType.SANATORIUM, scenariosSettings
				.getScenarioTitleFont(),
				scenariosSettings.getDifficultyButtonStyle().font.toString())).padLeft((int) (width / 12));
		buttons.add(addScenariosButton("City center Hotel", "hotel", "Hotel guests are mostly businessman", "Spring", HotelType.HOTEL, scenariosSettings.getScenarioTitleFont(),
				scenariosSettings.getDifficultyButtonStyle().font.toString()))
				.padLeft((int) (width / 12));
		scenariosTable.add(buttons).padTop((int) (scenariosSettings.getLargePaddingMultiplier() * height / 24));

		scenariosTable.row();

		TextButton.TextButtonStyle playStyle = new TextButton.TextButtonStyle();
		playStyle.up = upButton;
		playStyle.down = downButton;
		playStyle.font = skin.getFont("white-h1"); // Set the custom font
		playStyle.fontColor = Color.YELLOW; // Set the font color

		Button next = new TextButton("Next", playStyle);
		next.pad(15);
		next.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (selectedScenarioButton != null && selectedDifficultyButton != null) {
					difficultyTable.removeActor(errorLabel);
				}
				mainTable.clearChildren();
				mainTable.add(difficultyTable).growX();
			}
		});

		scenariosTable.add(next).right().padRight((int) (width / 12)).padTop((int) (scenariosSettings.getLargePaddingMultiplier() * height / 16));
	}

	public ScenarioButton addScenariosButton(String title, String image, String description, String season, HotelType hotelType, String scenarioTitleFont,
			String scenarioTextFont) {
		ScenarioButton scenario = new ScenarioButton(title, image, description, season, hotelType, scenarioTitleFont, scenarioTextFont);
		scenario.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (selectedScenarioButton != null && selectedScenarioButton != scenario) {
					selectedScenarioButton.setChecked(false);
				}
				selectedScenarioButton = scenario;
				selectedScenarioButton.setChecked(true);
			}
		});
		scenarioButtons.add(scenario);

		return scenario;
	}

	@Override
	public void show() {
//		InputMultiplexer multiplexer = new InputMultiplexer();
//		multiplexer.addProcessor(stage);
//		Gdx.input.setInputProcessor(multiplexer);
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
		createFrames();
	}
}
