package pl.agh.edu.utils;

import java.util.List;

import pl.agh.edu.language.LanguageManager;

public class LanguageString {
	private final String property;
	private final List<Pair<String, String>> stringsWithReplacements;

	public LanguageString(String property, List<Pair<String, String>> stringsWithReplacements) {
		this.property = property;
		this.stringsWithReplacements = stringsWithReplacements;
	}

	public LanguageString(String property) {
		this.property = property;
		this.stringsWithReplacements = List.of();
	}

	public String get() {
		String result = LanguageManager.get(property);
		for (Pair<String, String> stringsWithReplacement : stringsWithReplacements)
			result = result.replace(stringsWithReplacement.first(), stringsWithReplacement.second());
		return result;
	}
}
