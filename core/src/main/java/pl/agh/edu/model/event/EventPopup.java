package pl.agh.edu.model.event;

import pl.agh.edu.utils.LanguageString;

public record EventPopup(
        LanguageString title,
        LanguageString description,
        String imagePath
) {
}
