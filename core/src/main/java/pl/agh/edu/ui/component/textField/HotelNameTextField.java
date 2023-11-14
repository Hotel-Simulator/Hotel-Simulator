package pl.agh.edu.ui.component.textField;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.language.LanguageChangeListener;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.utils.SkinFont;
import pl.agh.edu.utils.LanguageString;

public class HotelNameTextField extends TextField {
    private boolean wasModified = false;
    private final LanguageLabel label;

    public HotelNameTextField(String text, Skin skin, String style) {
        super("", skin, style);
        this.label = new LanguageLabel(new LanguageString(text), SkinFont.BODY1.getName());
        setText(label.getText().toString());
        setTextFieldListener((textField, c) -> {
            wasModified = true;
        });
//        LanguageManager.
    }
}
