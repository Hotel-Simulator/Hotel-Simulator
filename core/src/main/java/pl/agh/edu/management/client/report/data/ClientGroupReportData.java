package pl.agh.edu.management.client.report.data;

import java.math.BigDecimal;

public record ClientGroupReportData<T>(
        T timeUnit,
        Integer clientGroupNumber,
        BigDecimal yearOnYearChangePercentage,
        Integer yearOnYearChangeChange
) {
}
