package pl.agh.edu.management.client.report.generator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import pl.agh.edu.management.client.report.ClientGroupNumberType;
import pl.agh.edu.management.client.report.data.ClientGroupReportData;
import pl.agh.edu.model.time.Time;

public abstract class ClientGroupReportGenerator<T> {

	protected static final Time time = Time.getInstance();

	public abstract List<ClientGroupReportData<T>> getReport(ClientGroupNumberType clientGroupNumberType, T start, T end);

	protected ClientGroupReportData<T> generateClientGroupReportData(T timeUnit, Integer currentClientNumber, Integer previousClientNumber) {
		BigDecimal yearOnYearChangePercentage = null;
		Integer yearOnYearChange = null;

		if (previousClientNumber != null) {
			yearOnYearChangePercentage = getYearOnYearChangePercentage(currentClientNumber, previousClientNumber);
			yearOnYearChange = getYearOnYearChange(currentClientNumber, previousClientNumber);
		}
		return new ClientGroupReportData<>(
				timeUnit,
				currentClientNumber,
				yearOnYearChangePercentage,
				yearOnYearChange);
	}

	private static BigDecimal getYearOnYearChangePercentage(int currentValue, int previousValue) {
		return BigDecimal.valueOf((double) currentValue / (double) previousValue)
				.subtract(BigDecimal.ONE)
				.setScale(2, RoundingMode.HALF_EVEN);
	}

	private static Integer getYearOnYearChange(int currentValue, int previousValue) {
		return currentValue - previousValue;
	}
}
