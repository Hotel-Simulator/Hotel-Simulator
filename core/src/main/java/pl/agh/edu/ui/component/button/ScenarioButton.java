package pl.agh.edu.ui.component.button;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.resolution.ResolutionManager;
import pl.agh.edu.ui.utils.FontType;
import pl.agh.edu.ui.utils.SkinColor;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;

public class ScenarioButton extends WrapperContainer<Button> {
    public final Skin skin = GameSkin.getInstance();
    public final HotelType hotelType;
    public final Button button;
    private final ScenarioButtonStyle scenarioButtonStyle;
    private int width;
    private int height;
    private LanguageLabel titleLabel;
    private Image scenarioImage;
    private LanguageLabel descriptionLabel;
    private LanguageLabel seasonLabel;

    public ScenarioButton(HotelType hotelType) {
        this.hotelType = hotelType;
        button = new Button(GameSkin.getInstance().get("scenario-difficulty-button", Button.ButtonStyle.class));
        setActor(button);

        setSize();
        scenarioButtonStyle = new ScenarioButtonStyle(hotelType);
        ScenarioButtonStyle.createPad(this);
        ResolutionManager.addListener(this);
        LanguageManager.addListener(this);

        createActors();
        createButton();

        setTouchable(Touchable.enabled);
    }

    public void createActors(){
        titleLabel = createLabel(scenarioButtonStyle.getTitleLabelPath(), scenarioButtonStyle.getTitleLabelFont(), height / 30);
        titleLabel.setAlignment(getAlign());

        scenarioImage = new Image(skin.getDrawable(scenarioButtonStyle.getIconPath()));

        descriptionLabel = createLabel(scenarioButtonStyle.getDescriptionLabelPath(), scenarioButtonStyle.getDescriptionLabelFont(), height / 40);
        descriptionLabel.setAlignment(getAlign());

        seasonLabel = createLabel(scenarioButtonStyle.getSeasonLabelPath(), scenarioButtonStyle.getDescriptionLabelFont(), height / 40);
        seasonLabel.setAlignment(getAlign());
    }

    public void createButton(){
        button.clearChildren();
        button.add(titleLabel).width(5 * width / 24).height(height / 12).row();
        button.add(scenarioImage).width(width / 12).height(height / 8).padTop(height / 50).center().row();
        button.add(descriptionLabel).width(5 * width / 24).height(height / 12).padTop(height / 50).row();
        button.add(seasonLabel).width(3 * width / 24).padTop(height / 40).center();
    }

    public void setSize() {
        width = GraphicConfig.getResolution().WIDTH;
        height = GraphicConfig.getResolution().HEIGHT;
    }

    public LanguageLabel createLabel(String labelTextPath, String labelFont, int lineHeight) {
        Label.LabelStyle labelStyle = skin.get(labelFont, Label.LabelStyle.class);
        labelStyle.font.getData().setLineHeight(lineHeight);
        labelStyle.fontColor = SkinColor.ALERT.getColor(SkinColor.ColorLevel._500);
        LanguageLabel languageLabel = new LanguageLabel(labelTextPath, labelFont);
        languageLabel.setStyle(labelStyle);
        languageLabel.setWrap(true);
        return languageLabel;
    }

    @Override
    public void onResolutionChange() {
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
        public static final int padTopBottom = 30;
        public static final int padLeftRight = 20;

        public static void createPad(ScenarioButton button) {
            button.pad(padTopBottom, padLeftRight, padTopBottom, padLeftRight);
        }

        public String getTitleLabelPath() {
            return switch (hotelType) {
                case RESORT -> "scenario.resort.title";
                case HOTEL -> "scenario.hotel.title";
                case SANATORIUM -> "scenario.sanatorium.title";
            };
        }

        public String getIconPath() {
            return switch (hotelType) {
                case RESORT -> "scenario-icon-water";
                case HOTEL -> "scenario-icon-hotel";
                case SANATORIUM -> "scenario-icon-hospital";
            };
        }

        public String getDescriptionLabelPath() {
            return switch (hotelType) {
                case RESORT -> "scenario.resort.description";
                case HOTEL -> "scenario.hotel.description";
                case SANATORIUM -> "scenario.sanatorium.description";
            };
        }

        public String getSeasonLabelPath() {
            return switch (hotelType) {
                case RESORT -> "scenario.resort.popularity";
                case HOTEL -> "scenario.hotel.popularity";
                case SANATORIUM -> "scenario.sanatorium.popularity";
            };
        }

        public String getTitleLabelFont() {
            return switch (GraphicConfig.getResolution().SIZE) {
                case SMALL -> FontType.H4.getWhiteVariantName();
                case MEDIUM -> FontType.H3.getWhiteVariantName();
                case LARGE -> FontType.H2.getWhiteVariantName();
            };
        }

        public String getDescriptionLabelFont() {
            return switch (GraphicConfig.getResolution().SIZE) {
                case SMALL -> FontType.SUBTITLE1.getWhiteVariantName();
                case MEDIUM -> FontType.H4.getWhiteVariantName();
                case LARGE -> FontType.H3.getWhiteVariantName();
            };
        }
    }

}
