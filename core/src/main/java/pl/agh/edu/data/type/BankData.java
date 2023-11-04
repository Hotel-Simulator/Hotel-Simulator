package pl.agh.edu.data.type;

import pl.agh.edu.engine.bank.BankAccountDetails;
import pl.agh.edu.utils.LanguageString;


public record BankData(
        String id,
        LanguageString name,
        BankAccountDetails accountDetails
) {
}
