package pl.agh.edu.model.employee;

import pl.agh.edu.enums.TypeOfContract;

import java.math.BigDecimal;

public record EmploymentPreferences(Shift desiredShift,
                                    BigDecimal acceptableWage,
                                    BigDecimal desiredWage,
                                    TypeOfContract desiredTypeOfContract) {
}
