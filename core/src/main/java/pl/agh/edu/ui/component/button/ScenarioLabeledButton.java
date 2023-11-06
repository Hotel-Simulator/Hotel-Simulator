package pl.agh.edu.ui.component.button;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.resolution.Size;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.utils.LanguageString;

public class ScenarioLabeledButton extends WrapperContainer<TextButton> {
    private final TextButton button;

    public ScenarioLabeledButton(LanguageString languageString) {
        super(languageString);
        this.button = new TextButton("", ScenarioLabeledButtonStyle.getButtonStyle());
        this.button.setFillParent(true);
        this.button.getLabel().setWrap(true);

        this.setActor(button);

        this.setLanguageChangeHandler(this::updateLabel);
        this.setResolutionChangeHandler(this::updateSize);
        this.initChangeHandlers();
    }

    private void updateSize() {
        this.button.setStyle(ScenarioLabeledButtonStyle.getButtonStyle());
        this.size(ScenarioLabeledButtonStyle.getWidth(), ScenarioLabeledButtonStyle.getHeight());
    }

    private void updateLabel(String text) {
        button.setText(text);
    }

    private static class ScenarioLabeledButtonStyle {
        private static final GameSkin skin = GameSkin.getInstance();

        public static float getWidth() {
            return switch (GraphicConfig.getResolution().SIZE) {
                case SMALL -> 300f;
                case MEDIUM -> 400f;
                case LARGE -> 500f;
            };
        }

        public static float getHeight() {
            return switch (GraphicConfig.getResolution().SIZE) {
                case SMALL -> 100f;
                case MEDIUM -> 130f;
                case LARGE -> 180f;
            };
        }

        public static TextButton.TextButtonStyle getButtonStyle() {
            return switch (GraphicConfig.getResolution().SIZE) {
                case SMALL -> skin.get("difficulty-play-back-small", TextButton.TextButtonStyle.class);
                case MEDIUM -> skin.get("difficulty-play-back-medium", TextButton.TextButtonStyle.class);
                case LARGE -> skin.get("difficulty-play-back-large", TextButton.TextButtonStyle.class);
            };
        }
    }
}
