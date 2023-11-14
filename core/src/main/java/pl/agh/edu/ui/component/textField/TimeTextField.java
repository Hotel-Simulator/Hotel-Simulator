package pl.agh.edu.ui.component.textField;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class TimeTextField extends TextField {

    public TimeTextField(String text, Skin skin) {
        super(text, skin);
        setMaxLength(5);  // Limit the maximum length to 5 characters
        setTextFieldFilter(new TimeTextFieldFilter());
    }

    private class TimeTextFieldFilter implements TextFieldFilter {
        @Override
        public boolean acceptChar(TextField textField, char c) {
            // Allow only numbers and colons
            if (Character.isDigit(c) || c == ':') {
                // Check the current text and the position of the cursor
                String currentText = textField.getText();
                int cursorPosition = textField.getCursorPosition();

                // Allow only numbers at positions 0, 1, 3, and 4 (2 digits : 2 digits)
                if ((cursorPosition == 0 || cursorPosition == 1 || cursorPosition == 3 || cursorPosition == 4) && Character.isDigit(c)) {
                    return true;
                }

                // Allow only colons at position 2
                if (cursorPosition == 2 && c == ':') {
                    return true;
                }

                // Allow colons only if there are no colons already and cursor is at positions 2 or 5
                if (currentText.indexOf(':') == -1 && (cursorPosition == 2 || cursorPosition == 5) && c == ':') {
                    return true;
                }
            }

            return false;
        }
    }
}
