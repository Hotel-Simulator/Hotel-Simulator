package pl.agh.edu.engine.employee.contract;

import java.math.BigDecimal;

import pl.agh.edu.engine.employee.Shift;
import pl.agh.edu.serialization.KryoConfig;

public record Offer(Shift shift, BigDecimal offeredWage, TypeOfContract typeOfContract) {
    static {
        KryoConfig.kryo.register(Offer.class);
    }
}
