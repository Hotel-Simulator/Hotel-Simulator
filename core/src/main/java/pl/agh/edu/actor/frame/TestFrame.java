package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.selectMenu.SelectMenu;
import pl.agh.edu.actor.component.selectMenu.SelectMenuItem;
import pl.agh.edu.actor.component.selectMenu.SelectedMenuStringItem;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		this.add(new Label(name, HotelSkin.getInstance()));

		Array<SelectMenuItem> items = new Array<>();
		items.add(new SelectedMenuStringItem("Option 1 Example Example Example"));
		items.add(new SelectedMenuStringItem("Option 2"));
		items.add(new SelectedMenuStringItem("Option 3"));
		items.add(new SelectedMenuStringItem("Option 4"));
		items.add(new SelectedMenuStringItem("Option 5"));
		items.add(new SelectedMenuStringItem("Option 6"));
		items.add(new SelectedMenuStringItem("Option 7"));
		items.add(new SelectedMenuStringItem("Option 8"));
		items.add(new SelectedMenuStringItem("Option 9"));

		SelectMenu selectMenu = new SelectMenu(
				"Example",
				items,
				selectedOption -> {
					System.out.println("Selected: " + selectedOption);
					return null;
				});

		this.add(selectMenu);
	}
}
