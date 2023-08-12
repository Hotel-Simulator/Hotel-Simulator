package pl.agh.edu.model.employee;

import java.math.BigDecimal;

import pl.agh.edu.enums.TypeOfContract;

public record JobOffer(Shift shift, BigDecimal offeredWage, TypeOfContract typeOfContract) {
}
