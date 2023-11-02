package pl.agh.edu.data.type;

import pl.agh.edu.engine.bank.BankAccountDetails;


public record BankData(
        String name,
        BankAccountDetails accountDetails
) {
}
