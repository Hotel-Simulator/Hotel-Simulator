package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.selectMenu.SelectMenu;
import pl.agh.edu.actor.utils.Size;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		this.add(new Label(name, HotelSkin.getInstance()));

		Array<String> items = new Array<>();
		items.add("Option 1");
		items.add("Option 2");
		items.add("Option 3");

		SelectMenu<String> selectMenu = new SelectMenu<>(
				Size.LARGE,
				"Example",
				items,
				selectedOption -> {
					System.out.println("Selected: " + selectedOption);
					return null;
				});

		this.add(selectMenu);
	}
}
