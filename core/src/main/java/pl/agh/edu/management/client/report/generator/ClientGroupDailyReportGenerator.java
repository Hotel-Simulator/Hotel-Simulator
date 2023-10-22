package pl.agh.edu.management.client.report.generator;

import java.time.LocalDate;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;

import pl.agh.edu.management.client.report.ClientGroupNumberType;
import pl.agh.edu.management.client.report.collector.ClientGroupReportDataCollector;
import pl.agh.edu.management.client.report.data.ClientGroupReportData;

public class ClientGroupDailyReportGenerator extends ClientGroupReportGenerator<LocalDate> {

	@Override
	public List<ClientGroupReportData<LocalDate>> getReport(ClientGroupNumberType clientGroupNumberType, LocalDate start, LocalDate end) {

		SortedMap<LocalDate, Integer> map = ClientGroupReportDataCollector.getAppropriateMap(clientGroupNumberType).getDailyData();
		SortedMap<LocalDate, Integer> subMap = map.subMap(start, end);

		return subMap.entrySet().stream()
				.map(entry -> generateClientGroupReportData(
						entry.getKey(),
						entry.getValue(),
						map.get(entry.getKey().minusYears(1))))
				.collect(Collectors.toList());
	}
}
