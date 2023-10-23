package pl.agh.edu.engine.client.report.data;

import java.math.BigDecimal;

public record ClientGroupReportData<T>(
        T timeUnit,
        Integer clientGroupNumber,
        BigDecimal yearOnYearChangePercentage,
        Integer yearOnYearChangeChange
) {
}
