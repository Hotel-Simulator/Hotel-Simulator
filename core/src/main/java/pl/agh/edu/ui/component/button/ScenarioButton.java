package pl.agh.edu.ui.component.button;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import static pl.agh.edu.ui.utils.SkinFont.H2;
import static pl.agh.edu.ui.utils.SkinFont.H3;
import static pl.agh.edu.ui.utils.SkinFont.H4;
import static pl.agh.edu.ui.utils.SkinFont.SUBTITLE1;
import static pl.agh.edu.ui.utils.SkinColor.ALERT;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._500;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.utils.LanguageString;

public class ScenarioButton extends WrapperContainer<Button> {
	private final Skin skin = GameSkin.getInstance();
	private final Button button;
	private final ScenarioButtonStyle scenarioButtonStyle;
	private LanguageLabel titleLabel;
	private Image scenarioImage;
	private LanguageLabel descriptionLabel;
	private LanguageLabel seasonLabel;

	public ScenarioButton(HotelType hotelType) {
		this.button = new Button(skin.get("scenario-difficulty-button", Button.ButtonStyle.class));
		this.scenarioButtonStyle = new ScenarioButtonStyle(hotelType);
		setActor(button);
		setSize();

		ScenarioButtonStyle.createPad(this);
		setResolutionChangeHandler(this::updateSizes);

		createActors();
		createButton();
		setTouchable(Touchable.enabled);
	}

	public void createActors() {
		titleLabel = createLabel(scenarioButtonStyle.getTitleLabelPath(), scenarioButtonStyle.getTitleLabelFont(), (int) (button.getHeight() / 30));
		titleLabel.setAlignment(getAlign());

		scenarioImage = new Image(skin.getDrawable(scenarioButtonStyle.getIconPath()));

		descriptionLabel = createLabel(scenarioButtonStyle.getDescriptionLabelPath(), scenarioButtonStyle.getDescriptionLabelFont(), (int) (button.getHeight() / 40));
		descriptionLabel.setAlignment(getAlign());

		seasonLabel = createLabel(scenarioButtonStyle.getSeasonLabelPath(), scenarioButtonStyle.getDescriptionLabelFont(), (int) (button.getHeight() / 40));
		seasonLabel.setAlignment(getAlign());
	}

	public void createButton() {
		button.clearChildren();
		button.add(titleLabel).width(5 * button.getWidth() / 24).height(button.getHeight() / 12).row();
		button.add(scenarioImage).width(button.getWidth() / 12).height(button.getHeight() / 8).padTop(button.getHeight() / 50).center().row();
		button.add(descriptionLabel).width(5 * button.getWidth() / 24).height(button.getHeight() / 12).padTop(button.getHeight() / 50).row();
		button.add(seasonLabel).width(3 * button.getWidth() / 24).padTop(button.getHeight() / 40).center();
	}

	public void setSize() {
		button.setWidth(GraphicConfig.getResolution().WIDTH);
		button.setHeight(GraphicConfig.getResolution().HEIGHT);
	}

	private LanguageLabel createLabel(String labelTextPath, String labelFont, int lineHeight) {
		Label.LabelStyle labelStyle = skin.get(labelFont, Label.LabelStyle.class);
		labelStyle.font.getData().setLineHeight(lineHeight);
		labelStyle.fontColor = ALERT.getColor(_500);
		LanguageLabel languageLabel = new LanguageLabel(new LanguageString(labelTextPath), labelFont);
		languageLabel.setStyle(labelStyle);
		languageLabel.setWrap(true);
		return languageLabel;
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

	public record ScenarioButtonStyle(HotelType hotelType) {
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
                case HOTEL -> "scenario-icon-hotel";
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
    }
}