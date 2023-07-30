package pl.agh.edu.model.employee;

import pl.agh.edu.enums.TypeOfContract;

import java.math.BigDecimal;

public record JobOffer(Shift shift, BigDecimal offeredWage, TypeOfContract typeOfContract) {
}
