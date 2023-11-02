package pl.agh.edu.ui.window.scenarios;

import java.util.ArrayList;
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
import pl.agh.edu.ui.resolution.Size;
import pl.agh.edu.ui.utils.FontType;
import pl.agh.edu.ui.utils.SkinColor;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;

public class ScenarioWindow extends WrapperContainer<Table> {
	public final GameSkin skin = GameSkin.getInstance();
	public final Table frame = new Table();
	public final List<ScenarioButton> buttonsList = new ArrayList<>();
	public final ButtonGroup<Button> buttonGroup = new ButtonGroup<>();
	private Table titleLabelTable;
	private LanguageLabel titleLabel;
	private TextButton nextButton;
	private int width;
	private int height;
	private float largePaddingMultiplier = 1;

	public ScenarioWindow() {
		setActor(frame);
		getSize();
		createDifficultyButtons();
		createTitleLabel();
		createNextButton();

		createFrame();
		LanguageManager.addListener(this);
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
		for (int i = 0; i < buttonsList.size(); i++) {
			scenarioButtonsTable.add(buttonsList.get(i)).padLeft(width / 48).padRight(width / 48);
		}
		frame.add(scenarioButtonsTable).padTop(largePaddingMultiplier * height / 24).row();
	}

	public void getSize() {
		width = GraphicConfig.getResolution().WIDTH;
		height = GraphicConfig.getResolution().HEIGHT;
		if (GraphicConfig.getResolution().SIZE.equals(Size.LARGE)) {
			largePaddingMultiplier = height / 1000f * 0.85f;
		}
	}

	public void createDifficultyButtons() {
		for (HotelType hotelType : HotelType.values()) {
			buttonsList.add(new ScenarioButton(hotelType));
		}
		createButtonGroup();
	}

	public void createButtonGroup() {
		buttonGroup.setMinCheckCount(1);
		buttonGroup.setMaxCheckCount(1);
		for (ScenarioButton scenarioButton : buttonsList) {
			buttonGroup.add(scenarioButton.getActor());
		}
	}

	public void createTitleLabel() {
		Label.LabelStyle titleLabelStyle = skin.get(getTitleFont(), Label.LabelStyle.class);
		titleLabelStyle.fontColor = SkinColor.ALERT.getColor(SkinColor.ColorLevel._500);
		titleLabel = new LanguageLabel("scenario.title", getTitleFont());
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
		return LanguageManager.get("scenario.next.button");
	}

	public void updateLabels() {
		Label.LabelStyle titleLabelStyle = titleLabel.getStyle();
		titleLabelStyle.font = skin.getFont(getTitleFont());
		titleLabel.setStyle(titleLabelStyle);

		nextButton.setStyle(getNextButtonStyle());
	}

	public String getTitleFont() {
		return switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> FontType.H3.getWhiteVariantName();
			case MEDIUM -> FontType.H2.getWhiteVariantName();
			case LARGE -> FontType.H1.getWhiteVariantName();
		};
	}

	public TextButton.TextButtonStyle getNextButtonStyle() {
		return switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> skin.get("difficulty-play-back-small", TextButton.TextButtonStyle.class);
			case MEDIUM -> skin.get("difficulty-play-back-medium", TextButton.TextButtonStyle.class);
			case LARGE -> skin.get("difficulty-play-back-large", TextButton.TextButtonStyle.class);
		};
	}

	@Override
	public void onLanguageChange() {
		nextButton.setText(getNextButtonText());
	}

	@Override
	public void onResolutionChange() {
		getSize();
		updateLabels();
		createFrame();
	}

}
