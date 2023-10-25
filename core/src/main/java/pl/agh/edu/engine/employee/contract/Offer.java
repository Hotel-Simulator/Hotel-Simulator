package pl.agh.edu.engine.employee.contract;

import java.math.BigDecimal;

import pl.agh.edu.engine.employee.Shift;

public record Offer(Shift shift, BigDecimal offeredWage, TypeOfContract typeOfContract) {
}
