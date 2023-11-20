package pl.agh.edu.serialization;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;

import pl.agh.edu.data.type.BankData;
import pl.agh.edu.engine.Engine;
import pl.agh.edu.engine.advertisement.AdvertisementCampaign;
import pl.agh.edu.engine.advertisement.AdvertisementHandler;
import pl.agh.edu.engine.advertisement.AdvertisementType;
import pl.agh.edu.engine.attraction.Attraction;
import pl.agh.edu.engine.attraction.AttractionHandler;
import pl.agh.edu.engine.attraction.AttractionSize;
import pl.agh.edu.engine.attraction.AttractionState;
import pl.agh.edu.engine.attraction.AttractionType;
import pl.agh.edu.engine.bank.BankAccount;
import pl.agh.edu.engine.bank.BankAccountDetails;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.bank.Credit;
import pl.agh.edu.engine.bank.Transaction;
import pl.agh.edu.engine.bank.TransactionType;
import pl.agh.edu.engine.building_cost.BuildingCostMultiplierHandler;
import pl.agh.edu.engine.building_cost.BuildingCostSupplier;
import pl.agh.edu.engine.calendar.Calendar;
import pl.agh.edu.engine.calendar.CalendarEvent;
import pl.agh.edu.engine.client.Arrival;
import pl.agh.edu.engine.client.Client;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.client.ClientGroupGenerationHandler;
import pl.agh.edu.engine.client.Gender;
import pl.agh.edu.engine.client.report.collector.ClientGroupReportDataCollector;
import pl.agh.edu.engine.client.report.generator.ClientGroupDailyReportGenerator;
import pl.agh.edu.engine.client.report.generator.ClientGroupMonthlyReportGenerator;
import pl.agh.edu.engine.client.report.generator.ClientGroupYearlyReportGenerator;
import pl.agh.edu.engine.client.report.util.DateTrie;
import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.EmployeeHandler;
import pl.agh.edu.engine.employee.EmployeeSalaryHandler;
import pl.agh.edu.engine.employee.EmployeeStatus;
import pl.agh.edu.engine.employee.EmploymentPreferences;
import pl.agh.edu.engine.employee.PossibleEmployee;
import pl.agh.edu.engine.employee.PossibleEmployeeHandler;
import pl.agh.edu.engine.employee.Profession;
import pl.agh.edu.engine.employee.Shift;
import pl.agh.edu.engine.employee.contract.Offer;
import pl.agh.edu.engine.employee.contract.TypeOfContract;
import pl.agh.edu.engine.employee.scheduler.CleaningScheduler;
import pl.agh.edu.engine.employee.scheduler.ReceptionScheduler;
import pl.agh.edu.engine.employee.scheduler.RepairScheduler;
import pl.agh.edu.engine.employee.scheduler.RoomComparator;
import pl.agh.edu.engine.event.ClientNumberModifier;
import pl.agh.edu.engine.event.EventHandler;
import pl.agh.edu.engine.event.EventModalData;
import pl.agh.edu.engine.event.permanent.BuildingCostModificationPermanentEvent;
import pl.agh.edu.engine.event.temporary.ClientNumberModificationEventHandler;
import pl.agh.edu.engine.event.temporary.ClientNumberModificationTemporaryEvent;
import pl.agh.edu.engine.event.temporary.TemporaryEvent;
import pl.agh.edu.engine.generator.ClientGenerator;
import pl.agh.edu.engine.generator.EventGenerator;
import pl.agh.edu.engine.hotel.Hotel;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;
import pl.agh.edu.engine.hotel.dificulty.GameDifficultyManager;
import pl.agh.edu.engine.hotel.scenario.HotelScenariosManager;
import pl.agh.edu.engine.opinion.Opinion;
import pl.agh.edu.engine.opinion.OpinionData;
import pl.agh.edu.engine.opinion.OpinionHandler;
import pl.agh.edu.engine.opinion.OpinionStars;
import pl.agh.edu.engine.opinion.bucket.EmployeesSatisfactionOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.QueueWaitingOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.RoomBreakingOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.RoomCleaningOpinionBucket;
import pl.agh.edu.engine.opinion.bucket.RoomPriceOpinionBucket;
import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.room.RoomManager;
import pl.agh.edu.engine.room.RoomPricePerNight;
import pl.agh.edu.engine.room.RoomRank;
import pl.agh.edu.engine.room.RoomSize;
import pl.agh.edu.engine.room.RoomState;
import pl.agh.edu.engine.time.Frequency;
import pl.agh.edu.engine.time.PartOfDay;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.NRepeatingTimeCommand;
import pl.agh.edu.engine.time.command.RepeatingTimeCommand;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.utils.LanguageString;
import pl.agh.edu.utils.Pair;

public class KryoConfig {

	public static final Kryo kryo = new Kryo();
	static {
		kryo.setReferences(true);

		kryo.register(BigDecimal.class);
		kryo.register(LocalDate.class);
		kryo.register(LocalDateTime.class);
		kryo.register(YearMonth.class);
		kryo.register(MonthDay.class);
		kryo.register(Year.class);
		kryo.register(Duration.class);
		kryo.register(LocalTime.class);
		kryo.register(ArrayList.class);
		kryo.register(LinkedList.class);
		kryo.register(PriorityQueue.class);
		kryo.register(EnumMap.class);
		kryo.register(Comparator.class);

		kryo.register(Object[].class);
		kryo.register(Class.class);
		kryo.register(ClosureSerializer.Closure.class, new ClosureSerializer());

		BankData.kryoRegister();

		AdvertisementCampaign.kryoRegister();
		AdvertisementHandler.kryoRegister();
		AdvertisementType.kryoRegister();
		Attraction.kryoRegister();
		AttractionHandler.kryoRegister();
		AttractionSize.kryoRegister();
		AttractionState.kryoRegister();
		AttractionType.kryoRegister();
		BankAccount.kryoRegister();
		BankAccountDetails.kryoRegister();
		BankAccountHandler.kryoRegister();
		Credit.kryoRegister();
		Transaction.kryoRegister();
		TransactionType.kryoRegister();
		BuildingCostMultiplierHandler.kryoRegister();
		BuildingCostSupplier.kryoRegister();
		Calendar.kryoRegister();
		CalendarEvent.kryoRegister();
		ClientGroupReportDataCollector.kryoRegister();
		ClientGroupDailyReportGenerator.kryoRegister();
		ClientGroupMonthlyReportGenerator.kryoRegister();
		ClientGroupYearlyReportGenerator.kryoRegister();
		DateTrie.kryoRegister();
		Arrival.kryoRegister();
		Client.kryoRegister();
		ClientGroup.kryoRegister();
		ClientGroupGenerationHandler.kryoRegister();
		Gender.kryoRegister();
		Offer.kryoRegister();
		TypeOfContract.kryoRegister();
		CleaningScheduler.kryoRegister();
		ReceptionScheduler.kryoRegister();
		RepairScheduler.kryoRegister();
		RoomComparator.kryoRegister();
		Employee.kryoRegister();
		EmployeeHandler.kryoRegister();
		EmployeeSalaryHandler.kryoRegister();
		EmployeeStatus.kryoRegister();
		EmploymentPreferences.kryoRegister();
		PossibleEmployee.kryoRegister();
		PossibleEmployeeHandler.kryoRegister();
		Profession.kryoRegister();
		Shift.kryoRegister();
		BuildingCostModificationPermanentEvent.kryoRegister();
		ClientNumberModificationEventHandler.kryoRegister();
		ClientNumberModificationTemporaryEvent.kryoRegister();
		TemporaryEvent.kryoRegister();
		ClientNumberModifier.kryoRegister();
		EventHandler.kryoRegister();
		EventModalData.kryoRegister();
		ClientGenerator.kryoRegister();
		EventGenerator.kryoRegister();
		DifficultyLevel.kryoRegister();
		GameDifficultyManager.kryoRegister();
		HotelScenariosManager.kryoRegister();
		Hotel.kryoRegister();
		HotelType.kryoRegister();
		HotelVisitPurpose.kryoRegister();
		EmployeesSatisfactionOpinionBucket.kryoRegister();
		QueueWaitingOpinionBucket.kryoRegister();
		RoomBreakingOpinionBucket.kryoRegister();
		RoomCleaningOpinionBucket.kryoRegister();
		RoomPriceOpinionBucket.kryoRegister();
		Opinion.kryoRegister();
		OpinionData.kryoRegister();
		OpinionHandler.kryoRegister();
		OpinionStars.kryoRegister();
		Room.kryoRegister();
		RoomManager.kryoRegister();
		RoomPricePerNight.kryoRegister();
		RoomRank.kryoRegister();
		RoomSize.kryoRegister();
		RoomState.kryoRegister();
		NRepeatingTimeCommand.kryoRegister();
		RepeatingTimeCommand.kryoRegister();
		TimeCommand.kryoRegister();
		Frequency.kryoRegister();
		PartOfDay.kryoRegister();
		Time.kryoRegister();
		TimeCommandExecutor.kryoRegister();
		Engine.kryoRegister();
		LanguageString.kryoRegister();
		Pair.kryoRegister();

	}

	public static <T> Serializer<List<T>> listSerializer(Class<T> clazz) {
		return new Serializer<>() {

			@Override
			public void write(Kryo kryo, Output output, List<T> object) {
				kryo.writeObject(output, object.size());
				object.forEach(e -> kryo.writeObject(output, e));
			}

			@Override
			public List<T> read(Kryo kryo, Input input, Class<? extends List<T>> type) {
				int size = kryo.readObject(input, Integer.class);
				return IntStream.range(0, size).mapToObj(i -> kryo.readObject(input, clazz)).collect(Collectors.toList());
			}
		};
	}

	public static <T> Serializer<PriorityQueue<T>> priorityQueueSerializer(Class<T> clazz) {
		return new Serializer<>() {

			@Override
			public void write(Kryo kryo, Output output, PriorityQueue<T> object) {
				kryo.writeObject(output, object.comparator());
				kryo.writeObject(output, object.size());
				object.forEach(e -> kryo.writeObject(output, e));
			}

			@Override
			public PriorityQueue<T> read(Kryo kryo, Input input, Class<? extends PriorityQueue<T>> type) {
				Comparator<T> comparator = kryo.readObject(input, Comparator.class);
				int size = kryo.readObject(input, Integer.class);
				PriorityQueue<T> priorityQueue = new PriorityQueue<>(comparator);
				IntStream.range(0, size).mapToObj(i -> kryo.readObject(input, clazz)).forEach(priorityQueue::add);
				return priorityQueue;
			}
		};
	}

	public static <T> Serializer<Set<T>> setSerializer(Class<T> clazz) {
		return new Serializer<>() {

			@Override
			public void write(Kryo kryo, Output output, Set<T> object) {
				kryo.writeObject(output, object.size());
				object.forEach(e -> kryo.writeObject(output, e));
			}

			@Override
			public Set<T> read(Kryo kryo, Input input, Class<? extends Set<T>> type) {
				int size = kryo.readObject(input, Integer.class);
				return IntStream.range(0, size).mapToObj(i -> kryo.readObject(input, clazz)).collect(Collectors.toSet());
			}
		};
	}

	public static <T, K> Serializer<Map<T, K>> mapSerializer(Class<T> clazzT, Class<K> clazzK) {
		return new Serializer<>() {
			@Override
			public void write(Kryo kryo, Output output, Map<T, K> object) {
				kryo.writeObject(output, object.size());
				object.forEach((key, value) -> {
					kryo.writeObject(output, key);
					kryo.writeObjectOrNull(output, value, clazzK);
				});
			}

			@Override
			public Map<T, K> read(Kryo kryo, Input input, Class<? extends Map<T, K>> type) {
				int size = kryo.readObject(input, Integer.class);
				return IntStream.range(0, size)
						.mapToObj(i -> new AbstractMap.SimpleEntry<>(
								kryo.readObject(input, clazzT),
								kryo.readObjectOrNull(input, clazzK)))
						.collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), HashMap::putAll);
			}
		};
	}

	public static <T, K> Serializer<Map<T, List<K>>> mapOfListSerializer(Class<T> clazzT, Class<K> clazzK) {
		return new Serializer<>() {
			@Override
			public void write(Kryo kryo, Output output, Map<T, List<K>> object) {
				kryo.writeObject(output, object.size());
				object.forEach((key, value) -> {
					kryo.writeObject(output, key);
					kryo.writeObjectOrNull(output, value, listSerializer(clazzK));
				});
			}

			@Override
			public Map<T, List<K>> read(Kryo kryo, Input input, Class<? extends Map<T, List<K>>> type) {
				int size = kryo.readObject(input, Integer.class);
				return IntStream.range(0, size)
						.mapToObj(i -> new AbstractMap.SimpleEntry<>(
								kryo.readObject(input, clazzT),
								kryo.readObjectOrNull(input, List.class, listSerializer(clazzK))))
						.collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), HashMap::putAll);
			}
		};
	}

	public static <T> T getPrivateFieldValue(Object object, String fieldName, Class<T> clazz) {
		Field field;
		field = getFieldRecursively(object.getClass(), fieldName);

		assert field != null;
		field.setAccessible(true);
		try {
			return clazz.cast(field.get(object));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private static Field getFieldRecursively(Class<?> clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			if (clazz.getSuperclass() != null) {
				return getFieldRecursively(clazz.getSuperclass(), fieldName);
			}
		}
		return null;
	}
}
