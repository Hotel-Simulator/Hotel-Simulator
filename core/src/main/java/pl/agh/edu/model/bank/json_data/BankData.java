package pl.agh.edu.model.bank.json_data;

import java.math.BigDecimal;
import java.math.BigInteger;

public record BankData (
    String name,
    int creditInterestRate,
    int depositInterestRate,
    BigDecimal bankAccountFee

){}
