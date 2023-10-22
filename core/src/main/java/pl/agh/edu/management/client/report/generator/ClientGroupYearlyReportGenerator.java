package pl.agh.edu.management.client.report.generator;

import java.time.Year;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;

import pl.agh.edu.management.client.report.ClientGroupNumberType;
import pl.agh.edu.management.client.report.collector.ClientGroupReportDataCollector;
import pl.agh.edu.management.client.report.data.ClientGroupReportData;

public class ClientGroupYearlyReportGenerator extends ClientGroupReportGenerator<Year> {
	@Override
	public List<ClientGroupReportData<Year>> getReport(ClientGroupNumberType clientGroupNumberType, Year start, Year end) {

		SortedMap<Year, Integer> map = ClientGroupReportDataCollector.getAppropriateMap(clientGroupNumberType).getYearlyData();

		// only full-year data
		map.remove(Year.from(time.getTime()));

		SortedMap<Year, Integer> subMap = map.subMap(start, end);

		return subMap.entrySet().stream()
				.map(entry -> generateClientGroupReportData(
						entry.getKey(),
						entry.getValue(),
						map.get(entry.getKey().minusYears(1))))
				.collect(Collectors.toList());
	}
}
