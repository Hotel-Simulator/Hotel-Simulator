package pl.agh.edu.actor.component.selectMenu;

import com.badlogic.gdx.utils.Array;

public class SelectMenuBoolean extends SelectMenuItem {
	public final boolean value;

	public SelectMenuBoolean(boolean value) {
		super(value ? "Yes" : "No");
		this.value = value;
	}

	public static Array<SelectMenuItem> getArray() {
		Array<SelectMenuItem> itemArray = new Array<>();

		itemArray.add(new SelectMenuBoolean(true));
		itemArray.add(new SelectMenuBoolean(false));

		return itemArray;
	}
}
