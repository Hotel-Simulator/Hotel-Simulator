package pl.agh.edu.ui.component.button;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;
import pl.agh.edu.ui.language.LanguageChangeListener;
import pl.agh.edu.ui.language.LanguageManager;

public class DifficultyButton extends TextButton implements LanguageChangeListener {
    public final DifficultyLevel difficulty;
    public final Skin skin;

    public DifficultyButton(DifficultyLevel difficulty, Skin skin, Boolean isChecked) {
        super("", skin);
        this.difficulty = difficulty;
        this.skin = skin;

        setButtonText();
        setChecked(isChecked);
    }

    public DifficultyButton(DifficultyLevel difficulty, Skin skin) {
        super("", skin);
        this.difficulty = difficulty;
        this.skin = skin;

        setButtonText();
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public void setButtonText(){
        String path = "difficulty." + difficulty.toString();
        setText(LanguageManager.get(path));
    }

    public TextButtonStyle getStyle(){
        return switch(GraphicConfig.getResolution().SIZE){
            case SMALL -> skin.get("difficulty-button-subtitle1", TextButtonStyle.class);
            case MEDIUM -> skin.get("difficulty-button-h4", TextButtonStyle.class);
            case LARGE -> skin.get("difficulty-button-h3", TextButtonStyle.class);
        };
    }

    @Override
    public void onLanguageChange() {
        setButtonText();
    }
}
