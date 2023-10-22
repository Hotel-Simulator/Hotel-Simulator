package pl.agh.edu.screen.main;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MainScreenInputAdapter extends InputMultiplexer {

    Runnable openOptionsAction;

    public MainScreenInputAdapter(Stage stage) {
        super(stage);
    }

    public void setOpenOptionsAction(Runnable openOptionsAction) {
        this.openOptionsAction = openOptionsAction;
    }
    @Override
    public boolean keyDown(int keycode) {
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE && openOptionsAction!=null) {
            openOptionsAction.run();
            return true;
        }
        return false;
    }
}
