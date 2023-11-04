package pl.agh.edu.ui.language;

import static java.util.Locale.ENGLISH;

import java.util.Locale;

import pl.agh.edu.utils.LanguageString;

public enum Language {
	Polish("language.pl", new Locale("pl", "PL")),
	English("language.en", ENGLISH);

	public final LanguageString languageString;
	public final Locale locale;

	Language(String languagePath, Locale locale) {
		this.languageString = new LanguageString(languagePath);
		this.locale = locale;
	}

	@Override
	public String toString() {
		return LanguageManager.get(languageString);
	}
}
