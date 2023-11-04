package pl.agh.edu.ui.component.selectMenu;

import com.badlogic.gdx.utils.Array;

import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.utils.LanguageString;

public class SelectMenuBoolean extends SelectMenuItem {
	private static final String languagePath = "selectMenu.boolean.";
	public final boolean value;

	public SelectMenuBoolean(boolean value) {
		super(getLanguagePath(value).path, () -> LanguageManager.get(getLanguagePath(value)));
		this.value = value;
	}

	public static Array<SelectMenuItem> getArray() {
		Array<SelectMenuItem> itemArray = new Array<>();

		itemArray.add(new SelectMenuBoolean(true));
		itemArray.add(new SelectMenuBoolean(false));

		return itemArray;
	}

	public static LanguageString getLanguagePath(boolean value) {
		return new LanguageString(languagePath + (value ? "yes" : "no"));
	}
}
