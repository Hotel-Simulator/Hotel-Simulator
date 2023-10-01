package pl.agh.edu.actor.component.selectMenu;

import com.badlogic.gdx.utils.Array;

import pl.agh.edu.language.LanguageManager;

public class SelectMenuBoolean extends SelectMenuItem {
	public final boolean value;
	private static final String languagePath = "selectMenu.boolean.";

	public SelectMenuBoolean(boolean value) {
		super(getLanguagePath(value), () -> LanguageManager.get(getLanguagePath(value)));
		this.value = value;
	}

	public static Array<SelectMenuItem> getArray() {
		Array<SelectMenuItem> itemArray = new Array<>();

		itemArray.add(new SelectMenuBoolean(true));
		itemArray.add(new SelectMenuBoolean(false));

		return itemArray;
	}

	public static String getLanguagePath(boolean value) {
		return "selectMenu.boolean." + (value ? "yes" : "no");
	}
}
