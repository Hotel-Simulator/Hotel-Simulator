package pl.agh.edu.ui.component.textField;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import static com.badlogic.gdx.utils.Align.center;
import pl.agh.edu.GdxGame;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.language.LanguageChangeListener;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.utils.SkinFont;
import pl.agh.edu.utils.LanguageString;

public class HotelNameTextField extends TextField {
    public final GdxGame game = (GdxGame) Gdx.app.getApplicationListener();

    public HotelNameTextField(Skin skin, String style) {
        super("", skin, style);
        String hotelName = game.engine.hotelHandler.hotel.getHotelName();
        setText(hotelName);

        setMaxLength();
        setAlignment(center);
        saveTextOnChange();
    }

    public void setMaxLength(){
        switch (GraphicConfig.getResolution().SIZE){
            case SMALL -> this.setMaxLength(16);
            case MEDIUM, LARGE -> this.setMaxLength(14);
        }
    }

    public void saveTextOnChange(){
        this.setTextFieldListener((textField, c) -> game.engine.hotelHandler.hotel.setHotelName(textField.getText()));
    }

}
