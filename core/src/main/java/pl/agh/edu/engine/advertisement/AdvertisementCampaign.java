package pl.agh.edu.engine.advertisement;

import java.time.LocalDate;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONAdvertisementDataLoader;
import pl.agh.edu.data.type.AdvertisementData;
import pl.agh.edu.serialization.KryoConfig;

public record AdvertisementCampaign(AdvertisementData advertisementData,
                                    LocalDate startDate,
                                    LocalDate endDate) implements Comparable<AdvertisementCampaign> {

    @Override
    public int compareTo(AdvertisementCampaign o) {
        return this.startDate.compareTo(o.startDate);
    }

    static {
        KryoConfig.kryo.register(AdvertisementCampaign.class, new Serializer<AdvertisementCampaign>() {
            @Override
            public void write(Kryo kryo, Output output, AdvertisementCampaign object) {
                kryo.writeObject(output, object.advertisementData().type());
                kryo.writeObject(output, object.startDate());
                kryo.writeObject(output, object.endDate());
            }

            @Override
            public AdvertisementCampaign read(Kryo kryo, Input input, Class<? extends AdvertisementCampaign> type) {
                return new AdvertisementCampaign(
                        JSONAdvertisementDataLoader.advertisementData.get(kryo.readObject(input, AdvertisementType.class)),
                        kryo.readObject(input, LocalDate.class),
                        kryo.readObject(input, LocalDate.class));
            }
        });
    }
}
