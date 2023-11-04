package pl.agh.edu.ui.panel;

import static pl.agh.edu.ui.resolution.Size.LARGE;
import static pl.agh.edu.ui.utils.SkinColor.ALERT;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._500;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.button.ScenarioButton;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.utils.LanguageString;

public class ScenarioPanel extends WrapperContainer<Table> {
	public final GameSkin skin = GameSkin.getInstance();
	public final Table frame = new Table();
	public final List<ScenarioButton> buttonList = new ArrayList<>();
	public final ButtonGroup<Button> buttonGroup = new ButtonGroup<>();
	private LanguageLabel titleLabel;
	private TextButton nextButton;
	private float largePaddingMultiplier = 1;

	public ScenarioPanel() {
		super(new LanguageString("scenario.next.button"));
		setActor(frame);
		getSize();
		createDifficultyButtons();
		createTitleLabel();
		createNextButton();

		createFrame();
		setResolutionChangeHandler(this::updatedSizes);
		setLanguageChangeHandler(this::setButtonText);
	}

	public void createFrame() {
		frame.clearChildren();
		frame.top();
		frame.setFillParent(true);
		frame.background(skin.getDrawable("hotel-room"));
		frame.add(titleLabel).padTop(largePaddingMultiplier * frame.getHeight() / 16).expandX().row();
		addScenarioButtonsToFrame();
		frame.add(nextButton).right().padRight(frame.getWidth() / 24).padTop(largePaddingMultiplier * frame.getHeight() / 30);
		frame.debug();
	}

	private void addScenarioButtonsToFrame() {
		Table scenarioButtonsTable = new Table();
		buttonList.forEach(button -> scenarioButtonsTable.add(button).padLeft(frame.getWidth() / 48).padRight(frame.getWidth() / 48));
		frame.add(scenarioButtonsTable).padTop(largePaddingMultiplier * frame.getHeight() / 24).row();
	}

	public void getSize() {
		frame.setWidth(GraphicConfig.getResolution().WIDTH);
		frame.setHeight(GraphicConfig.getResolution().HEIGHT);
		if (GraphicConfig.getResolution().SIZE.equals(LARGE)) {
			largePaddingMultiplier = frame.getHeight() / 1000f * 0.85f;
		}
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
		titleLabel = new LanguageLabel(new LanguageString("scenario.title"), getTitleLabelStyle().font.toString());
		titleLabel.setStyle(getTitleLabelStyle());
	}

	public void createNextButton() {
		nextButton = new TextButton(getNextButtonText(), getNextButtonStyle());
	}

	public String getNextButtonText() {
		return LanguageManager.get(new LanguageString("scenario.next.button"));
	}

	public void setButtonText(String text) {
		nextButton.setText(text);
	}

	public void updateLabels() {
		titleLabel.setStyle(getTitleLabelStyle());
		nextButton.setStyle(getNextButtonStyle());
	}

	public Label.LabelStyle getTitleLabelStyle() {
		return switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> skin.get("scenario-title-panel-small", Label.LabelStyle.class);
			case MEDIUM -> skin.get("scenario-title-panel-medium", Label.LabelStyle.class);
			case LARGE -> skin.get("scenario-title-panel-large", Label.LabelStyle.class);
		};
	}

	public TextButton.TextButtonStyle getNextButtonStyle() {
		return switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> skin.get("difficulty-play-back-small", TextButton.TextButtonStyle.class);
			case MEDIUM -> skin.get("difficulty-play-back-medium", TextButton.TextButtonStyle.class);
			case LARGE -> skin.get("difficulty-play-back-large", TextButton.TextButtonStyle.class);
		};
	}

	public void updatedSizes() {
		getSize();
		updateLabels();
		createFrame();
	}

}
