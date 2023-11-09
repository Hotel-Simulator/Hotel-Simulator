package pl.agh.edu.ui.component.button;

import static com.badlogic.gdx.scenes.scene2d.Touchable.enabled;
import static pl.agh.edu.ui.audio.SoundAudio.CLICK;
import static pl.agh.edu.ui.utils.SkinColor.ALERT;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._500;
import static pl.agh.edu.ui.utils.SkinFont.H2;
import static pl.agh.edu.ui.utils.SkinFont.H3;
import static pl.agh.edu.ui.utils.SkinFont.H4;
import static pl.agh.edu.ui.utils.SkinFont.SUBTITLE1;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.utils.LanguageString;

public class ScenarioButton extends WrapperContainer<Button> {
	private final Skin skin = GameSkin.getInstance();
	private final Button scenarioButton;
	private final ScenarioButtonStyle scenarioButtonStyle;
	private LanguageLabel titleLabel;
	private Image scenarioImage;
	private LanguageLabel descriptionLabel;
	private LanguageLabel seasonLabel;
	private HotelType hotelType;

	public ScenarioButton(HotelType hotelType) {
		this.scenarioButton = new Button(skin, "scenario-difficulty-button");
		this.scenarioButtonStyle = new ScenarioButtonStyle();
		this.hotelType = hotelType;
		setActor(scenarioButton);
		setSize();

		ScenarioButtonStyle.createPad(this);
		setResolutionChangeHandler(this::updateSizes);

		createActors();
		createButton();
		addSoundEvents();
		setTouchable(enabled);
	}

	public void createActors() {
		titleLabel = createLabel(scenarioButtonStyle.getTitleLabelPath(), scenarioButtonStyle.getTitleLabelFont(), (int) (scenarioButton.getHeight() / 30));
		titleLabel.setAlignment(getAlign());

		scenarioImage = new Image(skin.getDrawable(scenarioButtonStyle.getIconPath()));

		descriptionLabel = createLabel(scenarioButtonStyle.getDescriptionLabelPath(), scenarioButtonStyle.getDescriptionLabelFont(), (int) (scenarioButton.getHeight() / 40));
		descriptionLabel.setAlignment(getAlign());

		seasonLabel = createLabel(scenarioButtonStyle.getSeasonLabelPath(), scenarioButtonStyle.getDescriptionLabelFont(), (int) (scenarioButton.getHeight() / 40));
		seasonLabel.setAlignment(getAlign());
	}

	public void createButton() {
		scenarioButton.clearChildren();
		scenarioButton.add(titleLabel).width(scenarioButtonStyle.getTitleDescriptionWidth()).height(scenarioButtonStyle.getTitleDescriptionHeight()).row();
		scenarioButton.add(scenarioImage).width(scenarioButtonStyle.getIconWidthHeight()).height(scenarioButtonStyle.getIconWidthHeight()).padTop(scenarioButtonStyle.getElementPadTop())
				.center().row();
		scenarioButton.add(descriptionLabel).width(scenarioButtonStyle.getTitleDescriptionWidth()).height(scenarioButtonStyle.getTitleDescriptionHeight()).padTop(scenarioButtonStyle
				.getElementPadTop()).row();
		scenarioButton.add(seasonLabel).width(scenarioButtonStyle.getSeasonLabelWidth()).padTop(scenarioButtonStyle.getElementPadTop()).center();
	}

	private void addSoundEvents() {
		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (!scenarioButton.isChecked()) {
					CLICK.playSound();
					return true;
				}
				return false;
			}
		});
	}

	public void setSize() {
		scenarioButton.setWidth(GraphicConfig.getResolution().WIDTH);
		scenarioButton.setHeight(GraphicConfig.getResolution().HEIGHT);
	}

	private LanguageLabel createLabel(String labelTextPath, String labelFont, int lineHeight) {
		Label.LabelStyle labelStyle = createLabelStyle(labelFont, lineHeight);
		LanguageLabel languageLabel = new LanguageLabel(new LanguageString(labelTextPath), labelFont);
		languageLabel.setStyle(labelStyle);
		languageLabel.setWrap(true);
		return languageLabel;
	}

	private Label.LabelStyle createLabelStyle(String labelFont, int lineHeight) {
		Label.LabelStyle labelStyle = new Label.LabelStyle(skin.get(labelFont, Label.LabelStyle.class));
		labelStyle.font.getData().setLineHeight(lineHeight);
		labelStyle.fontColor = ALERT.getColor(_500);
		return labelStyle;
	}

	public void updateSizes() {
		setSize();
		createButton();
		updateLabelStyles();
	}

	private void updateLabelStyles() {
		Label.LabelStyle titleStyle = titleLabel.getStyle();
		titleStyle.font = skin.getFont(scenarioButtonStyle.getTitleLabelFont());
		titleLabel.setStyle(titleStyle);

		Label.LabelStyle descriptionStyle = descriptionLabel.getStyle();
		descriptionStyle.font = skin.getFont(scenarioButtonStyle.getDescriptionLabelFont());
		descriptionLabel.setStyle(descriptionStyle);
		seasonLabel.setStyle(descriptionStyle);
	}

	public class ScenarioButtonStyle {
		private static final int PAD_VERTICAL = 30;
		private static final int PAD_HORIZONTAL = 20;

		public static void createPad(ScenarioButton button) {
			button.pad(PAD_VERTICAL, PAD_HORIZONTAL, PAD_VERTICAL, PAD_HORIZONTAL);
		}

		public String getTitleLabelPath() {
			return scenarioKey("title");
		}

		public String getIconPath() {
			return switch (hotelType) {
				case RESORT -> "scenario-icon-water";
				case CITY -> "scenario-icon-hotel";
				case SANATORIUM -> "scenario-icon-hospital";
			};
		}

		public String getDescriptionLabelPath() {
			return scenarioKey("description");
		}

		public String getSeasonLabelPath() {
			return scenarioKey("popularity");
		}

		public String getTitleLabelFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> H4.getWhiteVariantName();
				case MEDIUM -> H3.getWhiteVariantName();
				case LARGE -> H2.getWhiteVariantName();
			};
		}

		public String getDescriptionLabelFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> SUBTITLE1.getWhiteVariantName();
				case MEDIUM -> H4.getWhiteVariantName();
				case LARGE -> H3.getWhiteVariantName();
			};
		}

		private String scenarioKey(String propertyName) {
			return "scenario." + hotelType.name().toLowerCase() + "." + propertyName;
		}

		public float getTitleDescriptionWidth() {
			return 5 * scenarioButton.getWidth() / 24;
		}

		public float getTitleDescriptionHeight() {
			return scenarioButton.getHeight() / 12;
		}

		public float getElementPadTop() {
			return scenarioButton.getHeight() / 50;
		}

		public float getSeasonLabelWidth() {
			return 3 * scenarioButton.getWidth() / 24;
		}

		public float getIconWidthHeight() {
			return scenarioButton.getHeight() / 8;
		}
	}
}
