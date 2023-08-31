package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.select_menu.SelectMenu;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		this.add(new Label(name, HotelSkin.getInstance()));
		this.add(new SelectMenu());
	}
}
