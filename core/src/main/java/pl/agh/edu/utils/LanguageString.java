package pl.agh.edu.utils;

import java.util.List;

public class LanguageString {
	public final String property;
	public final List<Pair<String, String>> stringsWithReplacements;

	public LanguageString(String property, List<Pair<String, String>> stringsWithReplacements) {
		this.property = property;
		this.stringsWithReplacements = stringsWithReplacements;
	}

	public LanguageString(String property) {
		this.property = property;
		this.stringsWithReplacements = List.of();
	}
}
