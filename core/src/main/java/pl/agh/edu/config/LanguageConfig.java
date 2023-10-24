package pl.agh.edu.config;

import static pl.agh.edu.ui.language.Language.Polish;

import pl.agh.edu.ui.language.Language;
import pl.agh.edu.ui.language.LanguageManager;

public class LanguageConfig {

	private static Language language = Polish;

	public static Language getLanguage() {
		return language;
	}

	public static void setLanguage(Language language) {
		LanguageConfig.language = language;
		LanguageManager.updateLanguage();
	}

}
