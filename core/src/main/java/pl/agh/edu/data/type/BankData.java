package pl.agh.edu.data.type;

import pl.agh.edu.engine.bank.BankAccountDetails;
import pl.agh.edu.serialization.KryoConfig;


public record BankData(
        String name,
        BankAccountDetails accountDetails
) {

    static {
        KryoConfig.kryo.register(BankData.class);
    }
}
