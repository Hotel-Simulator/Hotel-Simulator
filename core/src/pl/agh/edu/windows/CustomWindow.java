package pl.agh.edu.windows;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CustomWindow extends Window {

    protected Stage stage;

    CustomWindow( String title, Skin skin){
        super(title,skin);
        TextButton closeButton = new TextButton("x",skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
            }
        });
        getTitleTable().add(closeButton).size(18, 18).padRight(10).padTop(0);
        setClip(false);
        setTransform(true);

    }

    CustomWindow(Stage stage,String title, Skin skin){
        this(title,skin);
    }
    public Stage getStage() {
        return stage;
    }
}
