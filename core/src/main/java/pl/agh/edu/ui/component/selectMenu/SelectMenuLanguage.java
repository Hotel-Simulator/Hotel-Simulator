package pl.agh.edu.ui.component.selectMenu;

import com.badlogic.gdx.utils.Array;

import pl.agh.edu.ui.language.Language;

public class SelectMenuLanguage extends SelectMenuItem {

	public final Language value;

	public SelectMenuLanguage(Language value) {
		super(value.languageString.path, value::toString);
		this.value = value;
	}

	public static Array<SelectMenuItem> getArray() {
		Array<SelectMenuItem> itemArray = new Array<>();

		for (Language language : Language.values()) {
			itemArray.add(new SelectMenuLanguage(language));
		}

		return itemArray;
	}
}
