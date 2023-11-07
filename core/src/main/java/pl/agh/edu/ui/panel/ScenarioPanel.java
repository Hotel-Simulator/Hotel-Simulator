package pl.agh.edu.ui.panel;

import static pl.agh.edu.ui.resolution.Size.LARGE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.button.ScenarioButton;
import pl.agh.edu.ui.component.button.ScenarioLabeledButton;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.utils.LanguageString;

public class ScenarioPanel extends WrapperContainer<Table> {
	public final Table frame = new Table();
	public final ScenarioPanelSizes sizes = new ScenarioPanelSizes();
	public final List<ScenarioButton> buttonList = new ArrayList<>();
	public final ButtonGroup<Button> buttonGroup = new ButtonGroup<>();
	public final Table topTable = new Table();
	public final Table middleTable = new Table();
	public final Table bottomTable = new Table();
	private LanguageLabel titleLabel;
	private ScenarioLabeledButton nextButton;

	public ScenarioPanel() {
		super(new LanguageString("scenario.next.button"));
		setActor(frame);
		getSize();
		createDifficultyButtons();
		createTitleLabel();
		createNextButton();

		createFrame();
		setResolutionChangeHandler(this::updatedSizes);
	}

	public void createFrame() {
		topTable.clearChildren();
		middleTable.clearChildren();
		bottomTable.clearChildren();

		frame.clearChildren();
		frame.setFillParent(true);
		frame.background(skin.getDrawable("hotel-room"));

		addTitleLabelToFrame();
		addScenarioButtonsToFrame();
		addNextButtonToFrame();

		frame.add(topTable).height(sizes.getTopAndBottomTableHeight()).growX().row();
		frame.add(middleTable).height(sizes.getMiddleTableHeight()).growX().row();
		frame.add(bottomTable).height(sizes.getTopAndBottomTableHeight()).growX().row();
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

	public void createNextButton() {
		nextButton = new ScenarioLabeledButton(getNextButtonText());
	}

	public void addNextButtonToFrame() {
		bottomTable.add(nextButton).growX().right().padRight(sizes.getPaddingHorizontal());
	}

	public LanguageString getNextButtonText() {
		return new LanguageString("scenario.next.button");
	}

	public void updateLabels() {
		titleLabel.setStyle(ScenarioPanelStyles.getTitleLabelStyle());
	}

	public void updatedSizes() {
		getSize();
		updateLabels();
		createFrame();
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
