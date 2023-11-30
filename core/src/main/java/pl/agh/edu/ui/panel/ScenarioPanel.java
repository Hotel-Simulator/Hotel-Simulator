package pl.agh.edu.ui.panel;

import static pl.agh.edu.ui.audio.SoundAudio.CLICK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.button.ScenarioButton;
import pl.agh.edu.ui.component.button.ScenarioLabeledButton;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.resolution.ResolutionChangeListener;
import pl.agh.edu.ui.resolution.ResolutionManager;
import pl.agh.edu.utils.LanguageString;

public class ScenarioPanel implements ResolutionChangeListener {
	public final Table frame = new Table();
	protected Skin skin = GameSkin.getInstance();
	public final ScenarioPanelSizes sizes = new ScenarioPanelSizes();
	public final List<ScenarioButton> buttonList = new ArrayList<>();
	public final ButtonGroup<Button> buttonGroup = new ButtonGroup<>();
	public final Table topTable = new Table();
	public final Table middleTable = new Table();
	public final Table bottomTable = new Table();
	private LanguageLabel titleLabel;
	private ScenarioLabeledButton backButton;
	private ScenarioLabeledButton nextButton;
	private final Runnable goToDifficultyPanel;

	public ScenarioPanel(Runnable goToDifficultyPanel) {
		this.goToDifficultyPanel = goToDifficultyPanel;
		getSize();
		createDifficultyButtons();
		createTitleLabel();
		createNextButton();
		createBackButton();

		setEventListeners();

		ResolutionManager.addListener(this);
	}

	public void createFrame() {
		topTable.clearChildren();
		middleTable.clearChildren();
		bottomTable.clearChildren();

		frame.clearChildren();
		frame.setFillParent(true);
		frame.left();

		addTitleLabelToFrame();
		addScenarioButtonsToFrame();
		addNextBackButtonsToFrame();

		frame.add(topTable).height(sizes.getTopAndBottomTableHeight()).growX().row();
		frame.add(middleTable).height(sizes.getMiddleTableHeight()).growX().row();
		frame.add(bottomTable).height(sizes.getTopAndBottomTableHeight()).growX().row();
	}

	private void setEventListeners() {
		nextButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				CLICK.playSound();
				goToDifficultyPanel.run();
			}
		});
	}

	public Optional<HotelType> getSelectedScenario() {
		Button selectedButton = buttonGroup.getChecked();

		return buttonList.stream()
				.filter(button -> selectedButton.equals(button.getActor()))
				.map(button -> button.hotelType)
				.findFirst();
	}

	private void addScenarioButtonsToFrame() {
		Table scenarioButtonsTable = new Table();
		buttonList.forEach(button -> scenarioButtonsTable.add(button).padLeft(sizes.getPaddingHorizontal()).padRight(sizes.getPaddingHorizontal()));
		middleTable.add(scenarioButtonsTable).left();
	}

	public void getSize() {
		frame.setWidth(GraphicConfig.getResolution().WIDTH);
		frame.setHeight(GraphicConfig.getResolution().HEIGHT);
	}

	public void createDifficultyButtons() {
		Arrays.stream(HotelType.values()).forEach(hotelType -> buttonList.add(new ScenarioButton(hotelType)));
		createButtonGroup();
	}

	public void createButtonGroup() {
		buttonGroup.setMinCheckCount(1);
		buttonGroup.setMaxCheckCount(1);
		buttonList.forEach(scenarioButton -> buttonGroup.add(scenarioButton.getActor()));
	}

	public void createTitleLabel() {
		titleLabel = new LanguageLabel(new LanguageString("scenario.title"), ScenarioPanelStyles.getTitleLabelStyle().font.toString());
		titleLabel.setStyle(ScenarioPanelStyles.getTitleLabelStyle());
	}

	public void addTitleLabelToFrame() {
		topTable.add(titleLabel);
	}

	private void addNextBackButtonsToFrame() {
		Table playBack = new Table();
		playBack.add(backButton).padRight(frame.getWidth() / 2);
		playBack.add(nextButton);
		bottomTable.add(playBack);
	}

	public void createNextButton() {
		nextButton = new ScenarioLabeledButton(getNextButtonText());
	}

	public void createBackButton() {
		backButton = new ScenarioLabeledButton(getBackButtonText());
	}

	public LanguageString getNextButtonText() {
		return new LanguageString("scenario.next.button");
	}

	public LanguageString getBackButtonText() {
		return new LanguageString("init.back.button");
	}

	public void updateLabels() {
		titleLabel.setStyle(ScenarioPanelStyles.getTitleLabelStyle());
	}

	public void updateSizes() {
		getSize();
		updateLabels();
		createFrame();
	}

	@Override
	public Actor onResolutionChange() {
		updateSizes();
		return frame;
	}

	public static class ScenarioPanelStyles {
		public static final GameSkin skin = GameSkin.getInstance();

		public static Label.LabelStyle getTitleLabelStyle() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> skin.get("scenario-title-panel-small", Label.LabelStyle.class);
				case MEDIUM -> skin.get("scenario-title-panel-medium", Label.LabelStyle.class);
				case LARGE -> skin.get("scenario-title-panel-large", Label.LabelStyle.class);
			};
		}

	}

	public class ScenarioPanelSizes {

		public float getTopAndBottomTableHeight() {
			return 2 * frame.getHeight() / 9;
		}

		public float getMiddleTableHeight() {
			return 5 * frame.getHeight() / 9;
		}

		public float getPaddingHorizontal() {
			return frame.getWidth() / 50;
		}

	}
}
