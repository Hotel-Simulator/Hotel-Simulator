package pl.agh.edu.management.client.report.generator;

import java.time.YearMonth;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;

import pl.agh.edu.management.client.report.ClientGroupNumberType;
import pl.agh.edu.management.client.report.collector.ClientGroupReportDataCollector;
import pl.agh.edu.management.client.report.data.ClientGroupReportData;

public class ClientGroupMonthlyReportGenerator extends ClientGroupReportGenerator<YearMonth> {

	@Override
	public List<ClientGroupReportData<YearMonth>> getReport(ClientGroupNumberType clientGroupNumberType, YearMonth start, YearMonth end) {

		SortedMap<YearMonth, Integer> map = ClientGroupReportDataCollector.getAppropriateMap(clientGroupNumberType).getMonthlyData();

		// only full-month data
		map.remove(time.getYearMonth());

		SortedMap<YearMonth, Integer> subMap = map.subMap(start, end);

		return subMap.entrySet().stream()
				.map(entry -> generateClientGroupReportData(
						entry.getKey(),
						entry.getValue(),
						map.get(entry.getKey().minusYears(1))))
				.collect(Collectors.toList());
	}
}
