package pl.agh.edu.engine.event;

import pl.agh.edu.utils.LanguageString;

public record EventModalData(
        LanguageString title,
        LanguageString description,
        String imagePath
) {
}