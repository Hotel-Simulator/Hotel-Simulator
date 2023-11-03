package pl.agh.edu.ui.panel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pl.agh.edu.ui.utils.SkinFont.H1;
import static pl.agh.edu.ui.utils.SkinFont.H2;
import static pl.agh.edu.ui.utils.SkinFont.H3;
import static pl.agh.edu.ui.resolution.Size.LARGE;
import static pl.agh.edu.ui.utils.SkinColor.ALERT;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._500;

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
import pl.agh.edu.ui.resolution.Size;
import pl.agh.edu.ui.utils.SkinColor;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.utils.LanguageString;

public class ScenarioPanel extends WrapperContainer<Table> {
	public final GameSkin skin = GameSkin.getInstance();
	public final Table frame = new Table();
	public final List<ScenarioButton> buttonList = new ArrayList<>();
	public final ButtonGroup<Button> buttonGroup = new ButtonGroup<>();
	private Table titleLabelTable;
	private LanguageLabel titleLabel;
	private TextButton nextButton;
	private int width;
	private int height;
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
		frame.add(titleLabelTable).padTop(largePaddingMultiplier * height / 12).expandX().row();
		addScenarioButtonsToFrame();
		frame.add(nextButton).right().padRight(width / 24).padTop(largePaddingMultiplier * height / 30);
		frame.debug();
	}

	private void addScenarioButtonsToFrame() {
		Table scenarioButtonsTable = new Table();
		buttonList.forEach(button -> scenarioButtonsTable.add(button).padLeft(width / 48).padRight(width / 48));
		frame.add(scenarioButtonsTable).padTop(largePaddingMultiplier * height / 24).row();
	}

	public void getSize() {
		width = GraphicConfig.getResolution().WIDTH;
		height = GraphicConfig.getResolution().HEIGHT;
		if (GraphicConfig.getResolution().SIZE.equals(LARGE)) {
			largePaddingMultiplier = height / 1000f * 0.85f;
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
		Label.LabelStyle titleLabelStyle = skin.get(getTitleFont(), Label.LabelStyle.class);
		titleLabelStyle.fontColor = ALERT.getColor(_500);
		titleLabel = new LanguageLabel(new LanguageString("scenario.title"), getTitleFont());
		titleLabel.setStyle(titleLabelStyle);
		titleLabelTable = new Table();
		titleLabelTable.setBackground(getTitleLabelBackground());
		titleLabelTable.add(titleLabel);
		titleLabelTable.pad(20f, 40f, 20f, 40f);
	}

	private NinePatchDrawable getTitleLabelBackground() {
		return new NinePatchDrawable(skin.getPatch("scenario-button-up"));
	}

	public void createNextButton() {
		nextButton = new TextButton(getNextButtonText(), getNextButtonStyle());
	}

	public String getNextButtonText() {
		return LanguageManager.get(new LanguageString("scenario.next.button"));
	}

	public void setButtonText(String text){
		nextButton.setText(text);
	}

	public void updateLabels() {
		Label.LabelStyle titleLabelStyle = titleLabel.getStyle();
		titleLabelStyle.font = skin.getFont(getTitleFont());
		titleLabel.setStyle(titleLabelStyle);

		nextButton.setStyle(getNextButtonStyle());
	}

	public String getTitleFont() {
		return switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> H3.getWhiteVariantName();
			case MEDIUM -> H2.getWhiteVariantName();
			case LARGE -> H1.getWhiteVariantName();
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
