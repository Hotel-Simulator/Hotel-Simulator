package pl.agh.edu.screen.application_listener;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import pl.agh.edu.model.time.Time;

public class TopNavbarListener implements ApplicationListener {

    private final Stage stage;

    public TopNavbarListener(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void create() {
        Button accelerationDecreaseButton = stage.getRoot().findActor("acceleration_decrease_button");
        Button accelerationIncreaseButton = stage.getRoot().findActor("acceleration_increase_button");
        Button accelerationToggleButton = stage.getRoot().findActor("time_toggle_button");
        accelerationDecreaseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Time.getInstance().decreaseAcceleration();
            }
        });

        accelerationIncreaseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Time.getInstance().increaseAcceleration();
            }
        });

        accelerationToggleButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Time.getInstance().toggle();
            }
        });
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Time time = Time.getInstance();
        Label timeLabel = stage.getRoot().findActor("time_navbar_label");
        timeLabel.setText(time.getStringTime());
        Label accelerationLabel = stage.getRoot().findActor("acceleration_label");
        accelerationLabel.setText(time.getStringAcceleration());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
