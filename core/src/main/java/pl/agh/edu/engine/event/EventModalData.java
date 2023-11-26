package pl.agh.edu.engine.event;

import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.LanguageString;

public record EventModalData(
        LanguageString title,
        LanguageString description,
        String imagePath
) {
    public static void kryoRegister() {
        KryoConfig.kryo.register(EventModalData.class);
    }
}
