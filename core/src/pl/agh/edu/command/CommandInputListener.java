package pl.agh.edu.command;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class CommandInputListener extends InputListener {
    private final TextField textField;

    private final CommandHistory commandHistory;

    private final CommandExecutor commandExecutor;

    public CommandInputListener(TextField textField) {
        this.textField = textField;
        this.commandHistory = CommandHistory.getInstance();
        this.commandExecutor = CommandExecutor.getInstance();
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (keycode == Input.Keys.ENTER) {
            String commandString = textField.getText();
            commandHistory.store(commandString);
            commandExecutor.addCommand(commandString);
            textField.setText("");
            return true;
        } else if (keycode == Input.Keys.UP) {
            textField.setText(commandHistory.getPreviousCommand());
            textField.setCursorPosition(textField.getText().length());
            return true;
        } else if (keycode == Input.Keys.DOWN) {
            textField.setText(commandHistory.getNextCommand());
            textField.setCursorPosition(textField.getText().length());
            return true;
        }
        return false;
    }
}
