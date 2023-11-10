package pl.agh.edu.ui.panel;

import static pl.agh.edu.ui.audio.SoundAudio.CLICK;
import static pl.agh.edu.ui.utils.SkinFont.H1;
import static pl.agh.edu.ui.utils.SkinFont.H2;
import static pl.agh.edu.ui.utils.SkinFont.H3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.button.DifficultyButton;
import pl.agh.edu.ui.component.button.ScenarioLabeledButton;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.resolution.Size;
import pl.agh.edu.ui.utils.SkinColor;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.utils.LanguageString;

public class DifficultyPanel extends WrapperContainer<Table> {
	public final GameSkin skin = GameSkin.getInstance();
	public final Table frame = new Table();
	public final DifficultyPanelSizes sizes = new DifficultyPanelSizes(frame);
	public final List<DifficultyButton> buttonList = new ArrayList<>();
	public final ButtonGroup<TextButton> buttonGroup = new ButtonGroup<>();
	public final Table topTable = new Table();
	public final Table middleTable = new Table();
	public final Table bottomTable = new Table();
	private LanguageLabel titleLabel;
	private ScenarioLabeledButton backButton;
	private ScenarioLabeledButton playButton;
	private Runnable goToScenarioPanel;
	private Runnable startGame;

	public DifficultyPanel(Runnable goToScenarioPanel, Runnable startGame) {
		setActor(frame);
		setSize();

		this.goToScenarioPanel = goToScenarioPanel;
		this.startGame = startGame;

		createDifficultyButtons();
		createTitleLabel();
		createPlayButton();
		createBackButton();
		createFrame();

		setResolutionChangeHandler(this::updateSizes);
		addListeners();
	}

	public void createFrame() {
		topTable.clearChildren();
		middleTable.clearChildren();
		bottomTable.clearChildren();

		frame.clearChildren();
		frame.setFillParent(true);
		frame.background(skin.getDrawable("hotel-room"));

		createTitleLabelTable();
		addDifficultyButtonsToFrame();
		addPlayBackButtonsToFrame();

		frame.add(topTable).left().height(sizes.getTopTableHeight()).expandX().row();
		frame.add(middleTable).left().height(sizes.getMiddleTableHeight()).expandX().row();
		frame.add(bottomTable).height(sizes.getBottomTableHeight()).expandX().row();
	}

	private void addListeners() {
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				CLICK.playSound();
				goToScenarioPanel.run();
			}
		});

		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				CLICK.playSound();
				startGame.run();
			}
		});
	}

	public Optional<DifficultyLevel> getSelectedDifficulty() {
		TextButton selectedButton = buttonGroup.getChecked();

		return buttonList.stream()
				.filter(button -> selectedButton.equals(button.getActor()))
				.map(button -> button.difficulty)
				.findFirst();
	}

	private void createTitleLabelTable() {
		Table titleLabelTable = new Table();
		titleLabelTable.setBackground(getTitleLabelBackground());
		titleLabelTable.add(titleLabel).pad(DifficultyPanelStyle.PAD_VERTICAL, DifficultyPanelStyle.PAD_HORIZONTAL, DifficultyPanelStyle.PAD_VERTICAL,
				DifficultyPanelStyle.PAD_HORIZONTAL);
		topTable.add(titleLabelTable).bottom().padLeft(sizes.getLabelTableWidth()).growX();
	}

	private void addPlayBackButtonsToFrame() {
		Table playBack = new Table();
		playBack.add(backButton).padRight(frame.getWidth() / 2);
		playBack.add(playButton);
		bottomTable.add(playBack);
	}

	private void addDifficultyButtonsToFrame() {
		buttonList.forEach(button -> middleTable.add(button).left().padBottom(sizes.getButtonPadBottom()).padLeft(sizes.getButtonPadLeft(buttonList.indexOf(
				button))).row());
	}

	public void createDifficultyButtons() {
		Arrays.stream(DifficultyLevel.values()).forEach(level -> buttonList.add(new DifficultyButton(level)));
		createButtonGroup();
	}

	public void createButtonGroup() {
		buttonGroup.setMinCheckCount(1);
		buttonGroup.setMaxCheckCount(1);
		buttonList.forEach(difficultyButton -> buttonGroup.add(difficultyButton.getActor()));
	}

	public void setSize() {
		frame.setWidth(GraphicConfig.getResolution().WIDTH);
		frame.setHeight(GraphicConfig.getResolution().HEIGHT);
		DifficultyPanelStyle.updatePaddingMultiplier(frame);
	}

	public void createTitleLabel() {
		titleLabel = new LanguageLabel(new LanguageString("difficulty.title"), DifficultyPanelStyle.getTitleFont());
		titleLabel.setStyle(DifficultyPanelStyle.getTitleLabelStyle());
	}

	private NinePatchDrawable getTitleLabelBackground() {
		return new NinePatchDrawable(skin.getPatch("scenario-title"));
	}

	public void createPlayButton() {
		playButton = new ScenarioLabeledButton(getPlayButtonText());
	}

	public void createBackButton() {
		backButton = new ScenarioLabeledButton(getBackButtonText());
	}

	public LanguageString getPlayButtonText() {
		return new LanguageString("difficulty.play.button");
	}

	public LanguageString getBackButtonText() {
		return new LanguageString("difficulty.back.button");
	}

	public void updateSizes() {
		setSize();
		createTitleLabel();
		createFrame();
	}

	private static class DifficultyPanelStyle {
		public static GameSkin skin = GameSkin.getInstance();
		public static float largePaddingMultiplier = 1;
		public static final float PAD_VERTICAL = 10f;
		public static final float PAD_HORIZONTAL = 40f;

		public static void updatePaddingMultiplier(Table frame) {
			if (GraphicConfig.getResolution().SIZE.equals(Size.LARGE)) {
				largePaddingMultiplier = frame.getHeight() / 1000f * 0.85f;
			} else {
				largePaddingMultiplier = 1;
			}
		}

		private static Label.LabelStyle getTitleLabelStyle() {
			Label.LabelStyle titleLabelStyle = new Label.LabelStyle(skin.get(getTitleFont(), Label.LabelStyle.class));
			titleLabelStyle.fontColor = SkinColor.ALERT.getColor(SkinColor.ColorLevel._500);
			return titleLabelStyle;
		}

		public static String getTitleFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> H3.getWhiteVariantName();
				case MEDIUM -> H2.getWhiteVariantName();
				case LARGE -> H1.getWhiteVariantName();
			};
		}
	}

	record DifficultyPanelSizes(Table frame){

		public float getTopTableHeight(){
			return 3*frame.getHeight()/9;
		}

		public float getMiddleTableHeight(){
			return 4*frame.getHeight()/9;
		}

		public float getBottomTableHeight(){
			return 2*frame.getHeight()/9;
		}

		public float getLabelTableWidth(){
			return frame.getWidth() / 12;
		}

		public float getButtonPadLeft(int multiplier){
			return frame.getWidth() / 6 + multiplier * frame.getWidth() / 12;
		}

		public float getButtonPadBottom(){
			return DifficultyPanelStyle.largePaddingMultiplier * frame.getHeight() / 24;
		}
	}
}
