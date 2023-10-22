package pl.agh.edu.model.event;

import pl.agh.edu.utils.LanguageString;

public record EventData(
        LanguageString title,
        LanguageString description,
        String imagePath
) {
}
