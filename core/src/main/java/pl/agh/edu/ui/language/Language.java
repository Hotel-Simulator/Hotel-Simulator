package pl.agh.edu.ui.language;

import static java.util.Locale.ENGLISH;

import java.util.Locale;

public enum Language {
	Polish("language.pl", new Locale("pl", "PL")),
	English("language.en", ENGLISH);

	public final String languagePath;
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
