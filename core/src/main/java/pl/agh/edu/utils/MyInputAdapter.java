package pl.agh.edu.utils;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.config.LanguageConfig;
import pl.agh.edu.ui.language.Language;
import pl.agh.edu.ui.resolution.Resolution;

public class MyInputAdapter extends InputMultiplexer {

    public MyInputAdapter(Stage stage) {
        super(stage);
    }

    @Override
    public boolean keyDown(int keycode) {
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.NUM_1) {
            GraphicConfig.changeResolution(Resolution._1366x768);
        }
        if (keycode == Input.Keys.NUM_2) {
            GraphicConfig.changeResolution(Resolution._1920x1080);
        }
        if (keycode == Input.Keys.NUM_3) {
            GraphicConfig.changeResolution(Resolution._3440x1440);
        }
        if (keycode == Input.Keys.NUM_4) {
            GraphicConfig.setFullscreenMode(true);
        }
        if (keycode == Input.Keys.NUM_5) {
            GraphicConfig.setFullscreenMode(false);
        }
        if (keycode == Input.Keys.NUM_6) {
            LanguageConfig.setLanguage(Language.Polish);
        }
        if (keycode == Input.Keys.NUM_7) {
            LanguageConfig.setLanguage(Language.English);
        }
        return false;
    }
}
