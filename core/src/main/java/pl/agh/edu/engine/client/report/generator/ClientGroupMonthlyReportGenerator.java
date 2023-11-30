package pl.agh.edu.engine.client.report.generator;

import java.time.YearMonth;
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

public class ClientGroupMonthlyReportGenerator extends ClientGroupReportGenerator<YearMonth> {

	public static void kryoRegister() {
		KryoConfig.kryo.register(ClientGroupMonthlyReportGenerator.class, new Serializer<ClientGroupMonthlyReportGenerator>() {
			@Override
			public void write(Kryo kryo, Output output, ClientGroupMonthlyReportGenerator object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.clientGroupReportDataCollector);
			}

			@Override
			public ClientGroupMonthlyReportGenerator read(Kryo kryo, Input input, Class<? extends ClientGroupMonthlyReportGenerator> type) {
				return new ClientGroupMonthlyReportGenerator(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, ClientGroupReportDataCollector.class));
			}
		});
	}

	public ClientGroupMonthlyReportGenerator(ClientGroupReportDataCollector clientGroupReportDataCollector) {
		super(clientGroupReportDataCollector);
	}

	private ClientGroupMonthlyReportGenerator(Time time, ClientGroupReportDataCollector clientGroupReportDataCollector) {
		super(time, clientGroupReportDataCollector);
	}

	@Override
	public List<ClientGroupReportData<YearMonth>> getReport(ClientGroupNumberType clientGroupNumberType, YearMonth start, YearMonth end) {

		SortedMap<YearMonth, Integer> map = clientGroupReportDataCollector.getAppropriateMap(clientGroupNumberType).getMonthlyData();

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
