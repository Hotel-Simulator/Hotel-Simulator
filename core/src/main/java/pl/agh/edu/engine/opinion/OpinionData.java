package pl.agh.edu.engine.opinion;

import java.time.LocalDate;
import java.util.List;

import pl.agh.edu.utils.LanguageString;

public record OpinionData(
        String guest,
        LocalDate date,
        OpinionStars stars,
        List<LanguageString> comments
) {
}
