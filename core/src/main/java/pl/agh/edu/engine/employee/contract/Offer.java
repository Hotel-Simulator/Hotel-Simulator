package pl.agh.edu.engine.employee.contract;

import java.math.BigDecimal;

import pl.agh.edu.engine.employee.Shift;
import pl.agh.edu.serialization.KryoConfig;

public record Offer(Shift shift, BigDecimal offeredWage, TypeOfContract typeOfContract) {
    public static void kryoRegister() {
        KryoConfig.kryo.register(Offer.class);
    }
}
