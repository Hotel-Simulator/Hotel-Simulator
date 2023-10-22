package pl.agh.edu.model.employee;

import java.math.BigDecimal;

import pl.agh.edu.enums.TypeOfContract;

public record ContractOffer(Shift shift, BigDecimal offeredWage, TypeOfContract typeOfContract) {
}
