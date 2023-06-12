package pl.agh.edu.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder;
import pl.agh.edu.GdxGame;
import pl.agh.edu.model.console.CommandExecutor;
import pl.agh.edu.model.console.CommandHistory;
import pl.agh.edu.model.console.LogHistory;
import pl.agh.edu.model.console.LogLevel;
public class ConsoleScreen implements Screen {

    private final GdxGame game;
    private final Stage stage;
    private Skin skin;
    private TextField consoleTextArea;
    private Table consoleHistory;
    private LogHistory logHistory;
    private CommandExecutor commandExecutor;
    private CommandHistory commandHistory;
    private ScrollPane consoleHistoryScrollPane;


    public ConsoleScreen(GdxGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
    }
    @Override
    public void show() {
        logHistory = LogHistory.getInstance();
        commandHistory = CommandHistory.getInstance();
        commandExecutor = CommandExecutor.getInstance();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.GRAVE) {
                    game.changeScreenBack();
                }
                return super.keyDown(keycode);
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
        skin = new Skin(Gdx.files.internal("skin/skin.json"));
        SceneComposerStageBuilder builder = new SceneComposerStageBuilder();
        builder.build(stage, skin, Gdx.files.internal("view/console_view.json"));

        consoleTextArea = stage.getRoot().findActor("console_input");
        consoleHistory = stage.getRoot().findActor("console_history_table");
        consoleHistoryScrollPane = stage.getRoot().findActor("console_history_scrollPane");

        consoleHistory.clear();
        consoleTextArea.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    commandExecutor.addCommand(consoleTextArea.getText());
                    consoleTextArea.setText("");
                    renderLogHistory();
                    //TODO repair auto scroll to bottom not 2 rows above
                    consoleHistoryScrollPane.setScrollPercentY(1);
                    return true;
                } else if (keycode == Input.Keys.UP) {
                    consoleTextArea.setText(commandHistory.getPreviousCommand());
                    consoleTextArea.setCursorPosition(consoleTextArea.getText().length());
                    return true;
                } else if (keycode == Input.Keys.DOWN) {
                    consoleTextArea.setText(commandHistory.getNextCommand());
                    consoleTextArea.setCursorPosition(consoleTextArea.getText().length());
                    return true;
                }
                return super.keyDown(event, keycode);
            }
        });
    }

    @Override
    public void render(float delta) {
        renderLogHistory();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void renderLogHistory(){
        consoleHistory.clear();
        logHistory.getLogEntries().forEach(logEntry -> {
            Label label = new Label(logEntry.toString(), skin, "h4_label");
            label.setAlignment(Align.bottomLeft);
            label.setWrap(true);
            if (logEntry.getLogLevel().equals(LogLevel.DEFAULT)) {
                label.setColor(skin.getColor("Gray_500"));
            } else if (logEntry.getLogLevel().equals(LogLevel.ERROR)){
                label.setColor(skin.getColor("Warning_700"));
            } else if (logEntry.getLogLevel().equals(LogLevel.SUCCESS)){
                label.setColor(skin.getColor("Success_500"));
            } else if (logEntry.getLogLevel().equals(LogLevel.WARNING)){
                label.setColor(skin.getColor("Alert_700"));
            }
            consoleHistory.row();
            consoleHistory.add(label).padLeft(10.0f).padRight(10.0f).growX().align(Align.bottomLeft);
        });
    }
}

