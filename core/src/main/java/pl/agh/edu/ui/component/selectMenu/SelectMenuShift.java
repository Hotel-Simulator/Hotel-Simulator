package pl.agh.edu.ui.component.selectMenu;

import com.badlogic.gdx.utils.Array;

import pl.agh.edu.engine.employee.Shift;

public class SelectMenuShift extends SelectMenuItem {
	public Shift shift;

	public SelectMenuShift(Shift shift) {
		super(shift.name(), shift::toString);
		this.shift = shift;
	}

	public static Array<SelectMenuItem> getArray() {
		Array<SelectMenuItem> itemArray = new Array<>();

		for (Shift shift : Shift.values()) {
			itemArray.add(new SelectMenuShift(shift));
		}

		return itemArray;
	}
}
