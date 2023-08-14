package pl.agh.edu.actor.panel.navbar;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.model.time.Time;

public class AccelerationPanel extends Table {
	private final Label accelerationLabel;

	public AccelerationPanel() {
		Skin skin = HotelSkin.getInstance();
		Label.LabelStyle labelStyle = skin.get("h4_label", Label.LabelStyle.class);
		accelerationLabel = new Label("1x", labelStyle);
		Button increaseButton = new Button(skin, "navbar-plus");
		Button decreaseButton = new Button(skin, "navbar-minus");
		Button playButton = new Button(skin, "navbar-play");

		this.setBackground(skin.getDrawable("pane-background-lime"));
		this.pad(0, 0, 0, 0);

		increaseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Time.getInstance().increaseAcceleration();
			}
		});

		decreaseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Time.getInstance().decreaseAcceleration();
			}
		});

		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Time.getInstance().stop();
			}
		});
		Table insideTable = new Table();
		insideTable.add(decreaseButton);
		insideTable.add(accelerationLabel);
		insideTable.add(increaseButton);
		insideTable.add(playButton);

		add(insideTable).size(220, 60).right();
	}

	public void setAcceleration(String acceleration) {
		accelerationLabel.setText(acceleration);
	}

}
