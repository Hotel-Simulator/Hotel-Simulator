package pl.agh.edu.management.client.report.collector;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.management.client.report.ClientGroupNumberType;
import pl.agh.edu.management.client.report.util.DateTrie;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class ClientGroupReportDataCollector {
	private static int clientGroupWithRoomCounter = 0;

	private static final DateTrie allClientGroupNumber = new DateTrie();
	private static final DateTrie clientGroupWithRoomNumber = new DateTrie();
	private static final DateTrie clientGroupWithoutRoomNumber = new DateTrie();

	private static final Time time = Time.getInstance();
	private static final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();

	private ClientGroupReportDataCollector() {}

	public static void increaseClientGroupWithRoomCounter() {
		clientGroupWithRoomCounter++;
	}

	public static void collectData(EnumMap<HotelVisitPurpose, Integer> numberOfClientGroups) {
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

	public static DateTrie getAppropriateMap(ClientGroupNumberType clientGroupNumberType) {
		return switch (clientGroupNumberType) {
			case ALL -> allClientGroupNumber;
			case WITH_ROOM -> clientGroupWithRoomNumber;
			case WITHOUT_ROOM -> clientGroupWithoutRoomNumber;
		};
	}
}
