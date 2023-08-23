package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.agh.edu.actor.HotelSkin;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		this.add(new Label(name, HotelSkin.getInstance()));
	}
}
