package pl.agh.edu.utils;

import java.util.List;

public class LanguageString {
	public final String path;
	public final List<Pair<String, String>> replacementsList;

	public LanguageString(String path, List<Pair<String, String>> replacementsList) {
		this.path = path;
		this.replacementsList = replacementsList;
	}

	public LanguageString() {
		this("error.error");
	}

	public LanguageString(String path) {
		this(path, List.of());
	}
}
