package pl.agh.edu.json.data;

import java.math.BigDecimal;

public record BankData (
    String name,
    int creditInterestRate,
    int depositInterestRate,
    BigDecimal bankAccountFee

){}
