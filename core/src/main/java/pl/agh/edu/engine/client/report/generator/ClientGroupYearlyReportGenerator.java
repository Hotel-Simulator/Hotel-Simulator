package pl.agh.edu.engine.client.report.generator;

import java.time.Year;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.client.report.ClientGroupNumberType;
import pl.agh.edu.engine.client.report.collector.ClientGroupReportDataCollector;
import pl.agh.edu.engine.client.report.data.ClientGroupReportData;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.serialization.KryoConfig;

public class ClientGroupYearlyReportGenerator extends ClientGroupReportGenerator<Year> {

	static {
		KryoConfig.kryo.register(ClientGroupYearlyReportGenerator.class, new Serializer<ClientGroupYearlyReportGenerator>() {
			@Override
			public void write(Kryo kryo, Output output, ClientGroupYearlyReportGenerator object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.clientGroupReportDataCollector);
			}

			@Override
			public ClientGroupYearlyReportGenerator read(Kryo kryo, Input input, Class<? extends ClientGroupYearlyReportGenerator> type) {
				return new ClientGroupYearlyReportGenerator(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, ClientGroupReportDataCollector.class));
			}
		});
	}

	public ClientGroupYearlyReportGenerator(ClientGroupReportDataCollector clientGroupReportDataCollector) {
		super(clientGroupReportDataCollector);
	}

	private ClientGroupYearlyReportGenerator(Time time, ClientGroupReportDataCollector clientGroupReportDataCollector) {
		super(time, clientGroupReportDataCollector);
	}

	@Override
	public List<ClientGroupReportData<Year>> getReport(ClientGroupNumberType clientGroupNumberType, Year start, Year end) {

		SortedMap<Year, Integer> map = clientGroupReportDataCollector.getAppropriateMap(clientGroupNumberType).getYearlyData();

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
