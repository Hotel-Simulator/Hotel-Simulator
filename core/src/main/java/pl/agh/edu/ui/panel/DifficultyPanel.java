package pl.agh.edu.ui.panel;

import static pl.agh.edu.ui.utils.SkinFont.H1;
import static pl.agh.edu.ui.utils.SkinFont.H2;
import static pl.agh.edu.ui.utils.SkinFont.H3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
	public final List<DifficultyButton> buttonsList = new ArrayList<>();
	public final ButtonGroup<TextButton> buttonGroup = new ButtonGroup<>();
	private LanguageLabel titleLabel;
	private ScenarioLabeledButton backButton;
	private ScenarioLabeledButton playButton;
	private float largePaddingMultiplier = 1;

	public DifficultyPanel() {
		setActor(frame);
		getSize();
		createDifficultyButtons();
		createTitleLabel();
		createPlayButton();
		createBackButton();
		createFrame();
		setResolutionChangeHandler(this::updateSizes);
	}

	public void createFrame() {
		frame.clearChildren();
		frame.left().top();
		frame.setFillParent(true);
		frame.background(skin.getDrawable("hotel-room"));
		frame.add(createTitleLabelTable()).left().padLeft(frame.getWidth() / 12).padTop(largePaddingMultiplier * frame.getHeight() / 16).expandX().row();
		addDifficultyButtonsToFrame();
		addPlayBackButtonsToFrame();
	}

	private Table createTitleLabelTable() {
		Table titleLabelTable = new Table();
		titleLabelTable.setBackground(getTitleLabelBackground());
		titleLabelTable.add(titleLabel).pad(10f, 40f, 10f, 40f);
		return titleLabelTable;
	}

	private void addPlayBackButtonsToFrame() {
		Table playBack = new Table();
		playBack.add(backButton).padRight(frame.getWidth() / 2);
		playBack.add(playButton);
		frame.add(playBack).padTop(largePaddingMultiplier * frame.getHeight() / 15);
	}

	private void addDifficultyButtonsToFrame() {
		buttonsList.forEach(button -> frame.add(button).left().padTop((int) (largePaddingMultiplier * frame.getHeight() / 24)).padLeft(frame.getWidth() / 6 + buttonsList.indexOf(
				button) * frame.getWidth() / 24).row());
	}

	public void createDifficultyButtons() {
		Arrays.stream(DifficultyLevel.values()).forEach(level -> buttonsList.add(new DifficultyButton(level)));
		createButtonGroup();
	}

	public void createButtonGroup() {
		buttonGroup.setMinCheckCount(1);
		buttonGroup.setMaxCheckCount(1);
		buttonsList.forEach(difficultyButton -> buttonGroup.add(difficultyButton.getActor()));
	}

	public void getSize() {
		frame.setWidth(GraphicConfig.getResolution().WIDTH);
		frame.setHeight(GraphicConfig.getResolution().HEIGHT);
		if (GraphicConfig.getResolution().SIZE.equals(Size.LARGE)) {
			largePaddingMultiplier = frame.getHeight() / 1000f * 0.85f;
		} else {
			largePaddingMultiplier = 1;
		}
	}

	public void createTitleLabel() {
		titleLabel = new LanguageLabel(new LanguageString("difficulty.title"), getTitleFont());
		titleLabel.setStyle(getTitleLabelStyle());
	}

	private NinePatchDrawable getTitleLabelBackground() {
		return new NinePatchDrawable(skin.getPatch("scenario-button-up"));
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

	public String getTitleFont() {
		return switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> H3.getWhiteVariantName();
			case MEDIUM -> H2.getWhiteVariantName();
			case LARGE -> H1.getWhiteVariantName();
		};
	}

	private Label.LabelStyle getTitleLabelStyle() {
		Label.LabelStyle titleLabelStyle = skin.get(getTitleFont(), Label.LabelStyle.class);
		titleLabelStyle.fontColor = SkinColor.ALERT.getColor(SkinColor.ColorLevel._500);
		return titleLabelStyle;
	}

	public void updateSizes() {
		getSize();
		createTitleLabel();
		createFrame();
	}
}
