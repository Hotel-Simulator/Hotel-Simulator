package pl.agh.edu.model.advertisement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.json.data.ConstantAdvertisementData;
import pl.agh.edu.json.data.SingleAdvertisementData;
import pl.agh.edu.json.data_loader.JSONAdvertisementDataLoader;
import pl.agh.edu.model.time.Time;

public class AdvertisementHandler {
	private static AdvertisementHandler instance;
	private static final EnumMap<SingleAdvertisementType, SingleAdvertisementData> simpleAdvertisementData = JSONAdvertisementDataLoader.singleAdvertisementData;
	private static final EnumMap<ConstantAdvertisementType, ConstantAdvertisementData> constantAdvertisementData = JSONAdvertisementDataLoader.constantAdvertisementData;
	private final EnumMap<SingleAdvertisementType, List<SingleAdvertisement>> singleAdvertisements;
	private final EnumMap<ConstantAdvertisementType, ConstantAdvertisement> constantAdvertisements;

	private final LinkedList<Advertisement> advertisementHistory;

	private final Time time;

	private AdvertisementHandler() {
		this.time = Time.getInstance();
		this.singleAdvertisements = new EnumMap<>(SingleAdvertisementType.class);
		for (SingleAdvertisementType type : SingleAdvertisementType.values()) {
			this.singleAdvertisements.put(type, new ArrayList<>());
		}
		this.constantAdvertisements = new EnumMap<>(ConstantAdvertisementType.class);
		this.advertisementHistory = new LinkedList<>();
	}

	public static AdvertisementHandler getInstance() {
		if (instance == null)
			instance = new AdvertisementHandler();
		return instance;
	}

	public boolean create(SingleAdvertisementType simpleAdvertisementType, List<LocalDate> emissionDates) {

		if (canCreate(simpleAdvertisementType, emissionDates)) {
			emissionDates.forEach(emissionDate -> singleAdvertisements.get(simpleAdvertisementType).add(new SingleAdvertisement(
					simpleAdvertisementType,
					simpleAdvertisementData.get(simpleAdvertisementType),
					emissionDate)));

			return true;
		} ;
		return false;
	}

	private boolean canCreate(SingleAdvertisementType simpleAdvertisementType, List<LocalDate> emissionDates) {
		return emissionDates.stream().distinct()
				.allMatch(emissionDate -> singleAdvertisements.get(simpleAdvertisementType)
						.stream()
						.noneMatch(it -> it.getEmissionDate().equals(emissionDate)));
	}

	public boolean create(ConstantAdvertisementType constantAdvertisementType) {

		if (canCreate(constantAdvertisementType)) {
			LocalDate startDate = time.getTime().toLocalDate().plusDays(constantAdvertisementData.get(constantAdvertisementType).preparationDays());
			startDate = startDate.minusDays(startDate.getDayOfMonth() - 1).plusMonths(1);
			constantAdvertisements.put(constantAdvertisementType, new ConstantAdvertisement(
					constantAdvertisementType,
					constantAdvertisementData.get(constantAdvertisementType),
					startDate));
			return true;
		}
		return false;
	}

	private boolean canCreate(ConstantAdvertisementType constantAdvertisementType) {
		return !constantAdvertisements.containsKey(constantAdvertisementType);
	}

	public void deleteConstantAdvertisement(ConstantAdvertisement constantAdvertisement) {
		if (constantAdvertisement.getEndDate() == null) {
			constantAdvertisement.setEndDate(time.getTime().toLocalDate().isBefore(constantAdvertisement.getStartDate())
					? constantAdvertisement.getStartDate().plusMonths(1)
					: time.getTime().toLocalDate().minusDays(time.getTime().toLocalDate().getDayOfMonth() - 1).plusMonths(1));
		}
	}

	public EnumMap<HotelVisitPurpose, Double> getCumulatedModifier() {
		return Stream.concat(
				singleAdvertisements.entrySet()
						.stream()
						.map(entry -> entry.getValue()
								.stream()
								.map(singleAdvertisement -> singleAdvertisement.getModifier(time.getTime().toLocalDate()))
								.reduce(
										new EnumMap<>(HotelVisitPurpose.class),
										(resultMap, enumMap) -> {
											for (HotelVisitPurpose key : enumMap.keySet()) {
												Double value = enumMap.get(key);
												resultMap.merge(key, value, (a, b) -> Math.min(a + b, simpleAdvertisementData.get(entry.getKey()).effectiveness().get(key)));
											}
											return resultMap;
										})),
				constantAdvertisements.values()
						.stream()
						.map(constantAdvertisement -> constantAdvertisement.getModifier(time.getTime().toLocalDate()))

		)
				.reduce(
						Stream.of(HotelVisitPurpose.values())
								.collect(Collectors.toMap(
										e -> e,
										e -> 0.,
										(a, b) -> b,
										() -> new EnumMap<>(HotelVisitPurpose.class))),
						(resultMap, enumMap) -> {
							for (HotelVisitPurpose key : enumMap.keySet()) {
								Double value = enumMap.get(key);
								resultMap.merge(key, value, Double::sum);
							}
							return resultMap;
						});

	}

	public void dailyUpdate() {
		constantAdvertisements.keySet().removeIf(type -> {
			if (constantAdvertisements.get(type).getEndDate() != null && constantAdvertisements.get(type).getEndDate().equals(time.getTime().toLocalDate())) {
				advertisementHistory.addFirst(constantAdvertisements.get(type));
				return true;
			}
			return false;
		});
		singleAdvertisements.values().forEach(list -> list.removeIf(it -> {
			if (it.getEndDate().equals(time.getTime().toLocalDate())) {
				advertisementHistory.addFirst(it);
				return true;
			}
			return false;
		}));
	}

	public BigDecimal getCostOfMaintenance() {
		return constantAdvertisements.values()
				.stream()
				.map(ConstantAdvertisement::getCostOfMaintenance)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public List<Advertisement> getAdvertisements() {
		return Stream
				.concat(
						singleAdvertisements.keySet()
								.stream()
								.flatMap(key -> singleAdvertisements.get(key).stream()),
						constantAdvertisements.values()
								.stream())
				.collect(Collectors.toList());
	}

	public List<Advertisement> getFilteredAdvertisements(boolean pastAdvertisements, boolean currentAdvertisements, boolean futureAdvertisements, String name, String type) {
		return Stream
				.concat(getAdvertisements().stream(), advertisementHistory.stream())
				.filter(advertisement -> type == null || type.equals(advertisement.getType()))
				.filter(advertisement -> name == null || name.equals(advertisement.getName()))
				.filter(advertisement -> pastAdvertisements || advertisement.getEndDate() == null || time.getTime().toLocalDate().isBefore(advertisement.getEndDate()))
				.filter(advertisement -> currentAdvertisements || (time.getTime().toLocalDate().isBefore(advertisement.getStartDate()) || (advertisement.getEndDate() != null
						&& !advertisement.getEndDate().isAfter(time.getTime().toLocalDate()))))
				.filter(advertisement -> futureAdvertisements || !advertisement.getStartDate().isAfter(time.getTime().toLocalDate()))
				.sorted(Advertisement::compareTo)
				.collect(Collectors.toList());
	}

	public Map<String, List<String>> getAdvertisementNames() {
		return Map.of(
				"single", Stream.of(SingleAdvertisementType.values()).map(type -> type.name().toLowerCase(Locale.ROOT).replaceAll("_", " ")).collect(Collectors.toList()),
				"constant", Stream.of(ConstantAdvertisementType.values()).map(type -> type.name().toLowerCase(Locale.ROOT).replaceAll("_", " ")).collect(Collectors.toList()));
	}

	public List<String> getAdvertisementTypes() {
		return List.of("single", "constant");
	}
}
