package pl.agh.edu.actor.panel.navbar;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import pl.agh.edu.actor.HotelSkin;

public class AccelerationPanel extends Table{
    private Label accelerationLabel;
    private Button increaseButton;
    private Button decreaseButton;
    private Button playButton;

    private Skin skin;

    public AccelerationPanel() {
        skin = HotelSkin.getInstance();
        Label.LabelStyle labelStyle = skin.get("h4_label", Label.LabelStyle.class);
        accelerationLabel = new Label("1x", labelStyle);
        increaseButton = new Button(skin, "navbar-plus");
        decreaseButton = new Button(skin, "navbar-minus");
        playButton = new Button(skin, "navbar-play");

        this.setBackground(skin.getDrawable("pane-background-lime"));
        this.pad(0, 0, 0, 0);
        this.setSize(215, 60);

        add(decreaseButton);
        add(accelerationLabel);
        add(increaseButton);
        add(playButton);
    }
    @Override
    public void layout() {
        super.layout();
        this.setSize(215, 60);
    }

    public void setAcceleration(String acceleration) {
        accelerationLabel.setText(acceleration);
    }
}

