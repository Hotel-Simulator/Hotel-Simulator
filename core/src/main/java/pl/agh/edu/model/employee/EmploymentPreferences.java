package pl.agh.edu.model.employee;

import java.math.BigDecimal;

import pl.agh.edu.enums.TypeOfContract;

public record EmploymentPreferences(Shift desiredShift,
                                    BigDecimal acceptableWage,
                                    BigDecimal desiredWage,
                                    TypeOfContract desiredTypeOfContract) {
}
