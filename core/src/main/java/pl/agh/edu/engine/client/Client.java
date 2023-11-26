package pl.agh.edu.engine.client;

import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.serialization.KryoConfig;

public record Client(String name, int age, Gender sex, HotelVisitPurpose hotelVisitPurpose){
    public static void kryoRegister() {
        KryoConfig.kryo.register(Client.class);
    }
}
