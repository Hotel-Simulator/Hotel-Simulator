package pl.agh.edu.config;

import pl.agh.edu.language.Language;
import pl.agh.edu.language.LanguageManager;

public class LanguageConfig {

	private static Language language = Language.Polish;

	public static void setLanguage(Language language) {
		LanguageConfig.language = language;
		LanguageManager.updateLanguage();
	}

	public static Language getLanguage() {
		return language;
	}

}
