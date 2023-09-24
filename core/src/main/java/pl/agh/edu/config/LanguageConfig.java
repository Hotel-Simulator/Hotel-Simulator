package pl.agh.edu.config;

import pl.agh.edu.language.Language;
import pl.agh.edu.language.LanguageManager;

import java.util.Locale;

public class LanguageConfig {

    private static Locale locale = Language.Polish.locale;

    public static void setLanguage(Locale locale) {
        LanguageConfig.locale = locale;
        LanguageManager.updateLanguage();
    }
    public static Locale getLanguage() {
        return locale;
    }

}
