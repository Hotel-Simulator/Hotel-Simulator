package pl.agh.edu.ui.component.textField;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import pl.agh.edu.GdxGame;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.language.LanguageChangeListener;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.utils.SkinFont;
import pl.agh.edu.utils.LanguageString;

public class HotelNameTextField extends TextField {
    public final GdxGame game = (GdxGame) Gdx.app.getApplicationListener();
    private boolean wasModified = false;
    private final Label label;

    public HotelNameTextField(Skin skin, String style) {
        super("", skin, style);
        String hotelName = game.engine.hotelHandler.hotel.getHotelName();
        this.label = new Label(hotelName, GameSkin.getInstance(), SkinFont.BODY1.getName());
        setText(label.getText().toString());
        setTextFieldListener((textField, c) -> {
            wasModified = true;
        });
//        LanguageManager.
    }
}
