package pl.agh.edu.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import pl.agh.edu.config.LanguageConfig;
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

	public static String get(String key) {
		return bundle.get(key);
	}

	public static String get(String key,List<Pair<String, String>> replaceList){
		String result = LanguageManager.get(key);
		for (Pair<String, String> stringsWithReplacement : replaceList) {
			result = result.replace(stringsWithReplacement.first(), stringsWithReplacement.second());
		}
		return result;
	}
	private static void notifyListeners() {
		listeners.forEach(LanguageChangeListener::onLanguageChange);
	}

	public static void addListener(LanguageChangeListener listener) {
		listeners.add(listener);
	}
}
