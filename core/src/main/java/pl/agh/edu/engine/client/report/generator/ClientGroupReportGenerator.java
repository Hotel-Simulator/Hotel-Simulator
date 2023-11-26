package pl.agh.edu.engine.client.report.generator;

import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;
import java.util.List;

import pl.agh.edu.engine.client.report.ClientGroupNumberType;
import pl.agh.edu.engine.client.report.collector.ClientGroupReportDataCollector;
import pl.agh.edu.engine.client.report.data.ClientGroupReportData;
import pl.agh.edu.engine.time.Time;

public abstract class ClientGroupReportGenerator<T> {
	protected final ClientGroupReportDataCollector clientGroupReportDataCollector;
	protected final Time time;

	protected ClientGroupReportGenerator(ClientGroupReportDataCollector clientGroupReportDataCollector) {
		this.time = Time.getInstance();
		this.clientGroupReportDataCollector = clientGroupReportDataCollector;
	}

	protected ClientGroupReportGenerator(Time time,
			ClientGroupReportDataCollector clientGroupReportDataCollector) {
		this.time = time;
		this.clientGroupReportDataCollector = clientGroupReportDataCollector;
	}

	private static BigDecimal getYearOnYearChangePercentage(int currentValue, int previousValue) {
		return BigDecimal.valueOf((double) currentValue / (double) previousValue)
				.subtract(ONE)
				.setScale(2, HALF_EVEN);
	}

	private static Integer getYearOnYearChange(int currentValue, int previousValue) {
		return currentValue - previousValue;
	}

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
}
