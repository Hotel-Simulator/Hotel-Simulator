package pl.agh.edu.ui.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.I18NBundle;

import pl.agh.edu.config.LanguageConfig;
import pl.agh.edu.utils.LanguageString;
import pl.agh.edu.utils.Pair;

public class LanguageManager {
	private static final List<LanguageChangeListener> listeners = new ArrayList<>();
	private static I18NBundle bundle;

	private static void loadBundle(Locale locale) {
		FileHandle baseFileHandle = Gdx.files.internal("i18n/language");
		bundle = I18NBundle.createBundle(baseFileHandle, locale);
	}

	public static void updateLanguage() {
		loadBundle(LanguageConfig.getLanguage().locale);
		notifyListeners();
	}

	private static String get(String key) {
		return bundle.get(key);
	}

	public static String get(LanguageString languageString) {
		if (languageString.replacementsList.isEmpty())
			return get(languageString.path);
		String result = get(languageString.path);
		for (Pair<String, String> replacement : languageString.replacementsList) {
			result = result.replace("{{" + replacement.first() + "}}", replacement.second());
		}
		return result;
	}

	private static void notifyListeners() {
		listeners.forEach(elem -> {
			Actor actor = elem.onLanguageChange();
			if (actor == null)
				listeners.remove(elem);
		});
	}

	public static void addListener(LanguageChangeListener listener) {
		listeners.add(listener);
	}
}
