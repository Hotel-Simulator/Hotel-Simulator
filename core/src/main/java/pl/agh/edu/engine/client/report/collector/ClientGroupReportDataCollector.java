package pl.agh.edu.engine.client.report.collector;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.client.report.ClientGroupNumberType;
import pl.agh.edu.engine.client.report.util.DateTrie;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;

public class ClientGroupReportDataCollector {
	private final Time time;
	private final TimeCommandExecutor timeCommandExecutor;
	private final DateTrie allClientGroupNumber;
	private final DateTrie clientGroupWithRoomNumber;
	private final DateTrie clientGroupWithoutRoomNumber;
	private int clientGroupWithRoomCounter;

	static {
		KryoConfig.kryo.register(ClientGroupReportDataCollector.class, new Serializer<ClientGroupReportDataCollector>() {
			@Override
			public void write(Kryo kryo, Output output, ClientGroupReportDataCollector object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.timeCommandExecutor);
				kryo.writeObject(output, object.allClientGroupNumber);
				kryo.writeObject(output, object.clientGroupWithRoomNumber);
				kryo.writeObject(output, object.clientGroupWithoutRoomNumber);
				kryo.writeObject(output, object.clientGroupWithRoomCounter);
			}

			@Override
			public ClientGroupReportDataCollector read(Kryo kryo, Input input, Class<? extends ClientGroupReportDataCollector> type) {
				return new ClientGroupReportDataCollector(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, TimeCommandExecutor.class),
						kryo.readObject(input, DateTrie.class),
						kryo.readObject(input, DateTrie.class),
						kryo.readObject(input, DateTrie.class),
						kryo.readObject(input, Integer.class));
			}
		});
	}

	public ClientGroupReportDataCollector() {
		this.time = Time.getInstance();
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.allClientGroupNumber = new DateTrie();
		this.clientGroupWithRoomNumber = new DateTrie();
		this.clientGroupWithoutRoomNumber = new DateTrie();
		this.clientGroupWithRoomCounter = 0;
	}

	private ClientGroupReportDataCollector(Time time,
			TimeCommandExecutor timeCommandExecutor,
			DateTrie allClientGroupNumber,
			DateTrie clientGroupWithRoomNumber,
			DateTrie clientGroupWithoutRoomNumber,
			int clientGroupWithRoomCounter) {
		this.time = time;
		this.timeCommandExecutor = timeCommandExecutor;
		this.allClientGroupNumber = allClientGroupNumber;
		this.clientGroupWithRoomNumber = clientGroupWithRoomNumber;
		this.clientGroupWithoutRoomNumber = clientGroupWithoutRoomNumber;
		this.clientGroupWithRoomCounter = clientGroupWithRoomCounter;
	}

	public void increaseClientGroupWithRoomCounter() {
		clientGroupWithRoomCounter++;
	}

	public void collectData(EnumMap<HotelVisitPurpose, Integer> numberOfClientGroups) {
		int allClientGroupNumberForToday = numberOfClientGroups.values().stream().mapToInt(Integer::intValue).sum();
		LocalDate date = time.getTime().toLocalDate();
		timeCommandExecutor.addCommand(
				new TimeCommand(
						() -> {
							allClientGroupNumber.insert(date, allClientGroupNumberForToday);
							clientGroupWithRoomNumber.insert(date, clientGroupWithRoomCounter);
							clientGroupWithoutRoomNumber.insert(date, allClientGroupNumberForToday - clientGroupWithRoomCounter);
							clientGroupWithRoomCounter = 0;
						},
						time.getTime().truncatedTo(ChronoUnit.DAYS).plusDays(1)));
	}

	public DateTrie getAppropriateMap(ClientGroupNumberType clientGroupNumberType) {
		return switch (clientGroupNumberType) {
			case ALL -> allClientGroupNumber;
			case WITH_ROOM -> clientGroupWithRoomNumber;
			case WITHOUT_ROOM -> clientGroupWithoutRoomNumber;
		};
	}
}
