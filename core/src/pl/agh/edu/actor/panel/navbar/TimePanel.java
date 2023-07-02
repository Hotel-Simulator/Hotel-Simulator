package pl.agh.edu.actor.panel.navbar;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import pl.agh.edu.actor.HotelSkin;

public class TimePanel extends Table {
    private Label timeLabel;
    private Skin skin;

    public TimePanel() {
        skin = HotelSkin.getInstance();
        Label.LabelStyle labelStyle = skin.get("navbar", Label.LabelStyle.class);
        timeLabel = new Label("20.05.2023\n19:00", labelStyle);
        this.pad(0, 0, 0, 0);
        timeLabel.setSize(300,80);
        timeLabel.setAlignment(Align.center);
        add(timeLabel).growX().expandX().top();
    }
    @Override
    public void layout() {
        super.layout();
        this.setSize(300, 80);
    }
    public void setTime(String time) {
        timeLabel.setText(time);
    }
}
