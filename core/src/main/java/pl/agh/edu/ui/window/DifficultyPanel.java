package pl.agh.edu.ui.window;

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
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.resolution.Size;
import pl.agh.edu.ui.utils.FontType;
import pl.agh.edu.ui.utils.SkinColor;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;

public class DifficultyPanel extends WrapperContainer<Table> {
	public final GameSkin skin = GameSkin.getInstance();
	public final Table frame = new Table();
	public final List<DifficultyButton> buttonsList = new ArrayList<>();
	public final ButtonGroup<TextButton> buttonGroup = new ButtonGroup<>();
	private LanguageLabel titleLabel;
	private TextButton backButton;
	private TextButton playButton;
	private float largePaddingMultiplier = 1;

	public DifficultyPanel() {
		setActor(frame);
		getSize();
		createDifficultyButtons();
		createTitleLabel();
		createPlayButton();
		createBackButton();
		createFrame();
		LanguageManager.addListener(this);
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
		playBack.add(backButton).padRight(3 * frame.getWidth() / 5);
		playBack.add(playButton);
		frame.add(playBack).padTop(largePaddingMultiplier * frame.getHeight() / 15);
	}

	private void addDifficultyButtonsToFrame() {
		for (DifficultyButton button : buttonsList) {
			frame.add(button).left().padTop((int) (largePaddingMultiplier * frame.getHeight() / 24)).padLeft(frame.getWidth() / 6 + buttonsList.indexOf(button) * frame.getWidth() / 24).row();
		}
	}

	public void createDifficultyButtons() {
		Arrays.stream(DifficultyLevel.values()).forEach(level -> buttonsList.add(new DifficultyButton(level)));
		createButtonGroup();
	}

	public void createButtonGroup() {
		buttonGroup.setMinCheckCount(1);
		buttonGroup.setMaxCheckCount(1);
		for (DifficultyButton difficultyButton : buttonsList) {
			buttonGroup.add(difficultyButton.getActor());
		}
	}

	public void getSize() {
		frame.setWidth(GraphicConfig.getResolution().WIDTH);
		frame.setHeight(GraphicConfig.getResolution().HEIGHT);
		if (GraphicConfig.getResolution().SIZE.equals(Size.LARGE)) {
			largePaddingMultiplier = frame.getHeight() / 1000f * 0.85f;
		}
		else{
			largePaddingMultiplier = 1;
		}
	}

	public void createTitleLabel() {
		titleLabel = new LanguageLabel("difficulty.title", getTitleFont());
		titleLabel.setStyle(getTitleLabelStyle());
	}

	private NinePatchDrawable getTitleLabelBackground() {
		return new NinePatchDrawable(skin.getPatch("scenario-button-up"));
	}

	public void createPlayButton() {
		playButton = new TextButton(getPlayButtonText(), getPlayBackButtonStyle());
	}

	public void createBackButton() {
		backButton = new TextButton(getBackButtonText(), getPlayBackButtonStyle());
	}

	public String getPlayButtonText() {
		return LanguageManager.get("difficulty.play.button");
	}

	public String getBackButtonText() {
		return LanguageManager.get("difficulty.back.button");
	}

	public String getTitleFont() {
		return switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> FontType.H3.getWhiteVariantName();
			case MEDIUM -> FontType.H2.getWhiteVariantName();
			case LARGE -> FontType.H1.getWhiteVariantName();
		};
	}

	public TextButton.TextButtonStyle getPlayBackButtonStyle() {
		return switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> skin.get("difficulty-play-back-small", TextButton.TextButtonStyle.class);
			case MEDIUM -> skin.get("difficulty-play-back-medium", TextButton.TextButtonStyle.class);
			case LARGE -> skin.get("difficulty-play-back-large", TextButton.TextButtonStyle.class);
		};
	}

	private Label.LabelStyle getTitleLabelStyle() {
		Label.LabelStyle titleLabelStyle = skin.get(getTitleFont(), Label.LabelStyle.class);
		titleLabelStyle.fontColor = SkinColor.ALERT.getColor(SkinColor.ColorLevel._500);
		return titleLabelStyle;
	}

	@Override
	public void onLanguageChange() {
		playButton.setText(getPlayButtonText());
		backButton.setText(getBackButtonText());
	}

	@Override
	public void onResolutionChange() {
		getSize();
		createTitleLabel();
		createPlayButton();
		createBackButton();
		createFrame();
	}
}