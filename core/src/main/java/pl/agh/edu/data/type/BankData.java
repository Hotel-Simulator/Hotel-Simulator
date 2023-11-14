package pl.agh.edu.data.type;

import pl.agh.edu.engine.bank.BankAccountDetails;
import pl.agh.edu.utils.LanguageString;
import pl.agh.edu.serialization.KryoConfig;



public record BankData(
        Integer id,
        LanguageString name,
        BankAccountDetails accountDetails
) {

    static {
        KryoConfig.kryo.register(BankData.class);
    }
}
