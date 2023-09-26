package pl.agh.edu.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import pl.agh.edu.config.LanguageConfig;

public class LanguageManager {
	private static final List<LanguageChangeListener> listeners = new ArrayList<>();
	private static I18NBundle bundle;

	private static void loadBundle(Locale locale) {
		FileHandle baseFileHandle = Gdx.files.internal("i18n/language");
		bundle = I18NBundle.createBundle(baseFileHandle, locale);
	}

	public static void updateLanguage() {
		System.out.println(LanguageConfig.getLanguage());
		loadBundle(LanguageConfig.getLanguage());
		notifyListeners();
	}

	public static String get(String key) {
		return bundle.get(key);
	}

	private static void notifyListeners() {
		listeners.forEach(LanguageChangeListener::onLanguageChange);
	}

	public static void addListener(LanguageChangeListener listener) {
		listeners.add(listener);
	}
}
