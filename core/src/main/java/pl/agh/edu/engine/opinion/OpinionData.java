package pl.agh.edu.engine.opinion;

import java.time.LocalDate;
import java.util.List;

import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.utils.LanguageString;

public record OpinionData(
        String guest,
        LocalDate date,
        OpinionStars stars,
        List<LanguageString> comments
) {
    @Override
    public String toString() {
        return "OpinionData{" +
                "date=" + date +
                ", stars=" + stars +
                ", comments=" + comments.stream().map(c -> LanguageManager.get(c.property)).toList() +
                '}';
    }
}
