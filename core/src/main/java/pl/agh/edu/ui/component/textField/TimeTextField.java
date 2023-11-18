package pl.agh.edu.ui.component.textField;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import pl.agh.edu.GdxGame;

import java.util.Objects;

public class TimeTextField extends TextField {
    public final GdxGame game = (GdxGame) Gdx.app.getApplicationListener();
    public String lastText;

    public TimeTextField(String text, Skin skin, String style) {
        super("", skin, style);
        setText(getTimeFromEngine(text));
        setMaxLength(5);
        setTextFieldFilter(new TimeTextFieldFilter());

        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                lastText = getText();
//                System.out.println(lastText);
                setText("");
                setCursorPosition(0);
            }
        });

//        this.addListener(new InputListener() {
//            @Override
//            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
////                super.exit(event, x, y, pointer, toActor);
//                setText(lastText);
//                System.out.println(getText());
//                setCursorPosition(lastText.length());
//            }
//        });

//        meld - 10-18
//                wymeld - 6-12
    }

    private String getTimeFromEngine(String text){
        if(Objects.equals(text, "in")){
            return game.engine.hotelHandler.hotel.getCheckInTime().toString();
        }
        else if(Objects.equals(text, "out")){
            return game.engine.hotelHandler.hotel.getCheckOutTime().toString();
        }
        return "";
    }

    private class TimeTextFieldFilter implements TextFieldFilter {
        @Override
        public boolean acceptChar(TextField textField, char c) {
            // Allow only numbers and colons
            if (Character.isDigit(c) || c == ':') {
                // Check the current text and the position of the cursor
                String currentText = textField.getText();
                int cursorPosition = textField.getCursorPosition();
                if(cursorPosition == 2 && c ==':'){
                    return true;
                }

                if(cursorPosition == 0 && (c == '1' || c == '2' || c == '0')){
                    return true;
                }
                if(cursorPosition == 1){
                    return true;
                }

                if(cursorPosition == 2){
                    text += ":";
                    setCursorPosition(3);
                    return true;
                }

                if(cursorPosition == 3 &&  (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5')){
                    return true;
                }
                if(cursorPosition == 4){
                    return true;
                }
            }

            return false;
        }
    }
}
