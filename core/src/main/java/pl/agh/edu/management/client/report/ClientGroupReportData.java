package pl.agh.edu.management.client.report;

import java.time.YearMonth;

public record ClientGroupReportData(YearMonth yearMonth,
                                    Integer clientGroupNumber,
                                    String yearOnYearChangePercentage,
                                    String yearOnYearChangeChange) {
}
