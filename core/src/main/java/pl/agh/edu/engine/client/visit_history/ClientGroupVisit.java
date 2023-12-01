package pl.agh.edu.engine.client.visit_history;

import java.time.LocalDateTime;

import pl.agh.edu.engine.room.RoomRank;
import pl.agh.edu.engine.room.RoomSize;
import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.LanguageString;

public record ClientGroupVisit(
        LocalDateTime time,
        RoomRank roomRank,
        RoomSize roomSize,
        LanguageString result
) {

    public static void kryoRegister() {
        KryoConfig.kryo.register(ClientGroupVisit.class);
    }

}
