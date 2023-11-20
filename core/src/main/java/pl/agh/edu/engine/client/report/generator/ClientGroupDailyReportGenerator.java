package pl.agh.edu.engine.client.report.generator;

import java.time.LocalDate;
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

public class ClientGroupDailyReportGenerator extends ClientGroupReportGenerator<LocalDate> {

	public static void kryoRegister() {
		KryoConfig.kryo.register(ClientGroupDailyReportGenerator.class, new Serializer<ClientGroupDailyReportGenerator>() {
			@Override
			public void write(Kryo kryo, Output output, ClientGroupDailyReportGenerator object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.clientGroupReportDataCollector);
			}

			@Override
			public ClientGroupDailyReportGenerator read(Kryo kryo, Input input, Class<? extends ClientGroupDailyReportGenerator> type) {
				return new ClientGroupDailyReportGenerator(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, ClientGroupReportDataCollector.class));
			}
		});
	}

	public ClientGroupDailyReportGenerator(ClientGroupReportDataCollector clientGroupReportDataCollector) {
		super(clientGroupReportDataCollector);
	}

	private ClientGroupDailyReportGenerator(Time time, ClientGroupReportDataCollector clientGroupReportDataCollector) {
		super(time, clientGroupReportDataCollector);
	}

	@Override
	public List<ClientGroupReportData<LocalDate>> getReport(ClientGroupNumberType clientGroupNumberType, LocalDate start, LocalDate end) {

		SortedMap<LocalDate, Integer> map = clientGroupReportDataCollector.getAppropriateMap(clientGroupNumberType).getDailyData();
		SortedMap<LocalDate, Integer> subMap = map.subMap(start, end);

		return subMap.entrySet().stream()
				.map(entry -> generateClientGroupReportData(
						entry.getKey(),
						entry.getValue(),
						map.get(entry.getKey().minusYears(1))))
				.collect(Collectors.toList());
	}
}
