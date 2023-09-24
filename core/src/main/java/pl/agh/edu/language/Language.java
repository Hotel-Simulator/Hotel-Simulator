package pl.agh.edu.language;

import java.util.Locale;

public enum Language {
	Polish("language.pl", new Locale("pl", "PL")),
	English("language.en", Locale.ENGLISH);

	private final String languagePath;
	public final Locale locale;

	Language(String path, Locale locale) {
		this.languagePath = path;
		this.locale = locale;
	}

	@Override
	public String toString() {
		return LanguageManager.get(languagePath);
	}
}
