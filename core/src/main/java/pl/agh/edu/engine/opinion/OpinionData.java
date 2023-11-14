package pl.agh.edu.engine.opinion;

import java.time.LocalDate;
import java.util.Set;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.LanguageString;

public record OpinionData(
        String guest,
        LocalDate date,
        OpinionStars stars,
        Set<LanguageString> comments
) {

    static {
        KryoConfig.kryo.register(OpinionData.class, new Serializer<OpinionData>() {
            @Override
            public void write(Kryo kryo, Output output, OpinionData object) {
                kryo.writeObject(output, object.guest);
                kryo.writeObject(output, object.date);
                kryo.writeObject(output, object.stars);
                kryo.writeObject(output, object.comments, KryoConfig.setSerializer(LanguageString.class));
            }

            @Override
            public OpinionData read(Kryo kryo, Input input, Class<? extends OpinionData> type) {
                return new OpinionData(
                        kryo.readObject(input, String.class),
                        kryo.readObject(input, LocalDate.class),
                        kryo.readObject(input, OpinionStars.class),
                        kryo.readObject(input, Set.class, KryoConfig.setSerializer(LanguageString.class))

                );
            }
        });
    }
}
