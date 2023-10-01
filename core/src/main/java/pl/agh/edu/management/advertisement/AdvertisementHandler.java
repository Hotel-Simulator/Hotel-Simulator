package pl.agh.edu.management.advertisement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.json.data_loader.JSONAdvertisementDataLoader;
import pl.agh.edu.model.advertisement.*;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class AdvertisementHandler {
	private static AdvertisementHandler instance;

	private final List<AdvertisementCampaign> advertisementCampaigns = new ArrayList<>();
	private final Time time = Time.getInstance();
	private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();

	public boolean canBuyAdvertisementCampaign(AdvertisementType type, LocalDate startDate, LocalDate endDate) {
		return advertisementCampaigns.stream()
				.filter(campaign -> campaign.advertisementData().type() == type)
				.noneMatch(campaign -> collide(campaign, startDate, endDate));
	}

	private boolean collide(AdvertisementCampaign campaign, LocalDate startDate, LocalDate endDate) {
		return campaign.endDate().isAfter(startDate) && endDate.isAfter(campaign.startDate());
	}

	public void buyAdvertisementCampaign(AdvertisementType type, LocalDate startDate, LocalDate endDate) {
		AdvertisementCampaign advertisementCampaign = new AdvertisementCampaign(JSONAdvertisementDataLoader.advertisementData.get(type), startDate, endDate);

		timeCommandExecutor.addCommand(new TimeCommand(
				() -> advertisementCampaigns.remove(advertisementCampaign),
				endDate.atTime(LocalTime.MIDNIGHT).minusMinutes(1)));

		advertisementCampaigns.add(advertisementCampaign);
	}

	public EnumMap<HotelVisitPurpose, BigDecimal> getCumulatedModifier() {
		return advertisementCampaigns.stream()
				.filter(this::isCurrentlyEmitted)
				.map(advertisementCampaigns -> advertisementCampaigns.advertisementData().effectiveness())
				.reduce(
						Stream.of(HotelVisitPurpose.values())
								.collect(Collectors.toMap(
										e -> e,
										e -> BigDecimal.ZERO,
										(a, b) -> b,
										() -> new EnumMap<>(HotelVisitPurpose.class))),
						(resultMap, enumMap) -> {
							enumMap.keySet().forEach(key -> {
								BigDecimal value = enumMap.get(key);
								resultMap.merge(key, value, BigDecimal::add);
							});
							return resultMap;
						});

	}

	private boolean isCurrentlyEmitted(AdvertisementCampaign campaign) {
		return !time.getTime().toLocalDate().isBefore(campaign.startDate()) && time.getTime().toLocalDate().isBefore(campaign.endDate());
	}

	public List<AdvertisementCampaign> getAdvertisementCampaigns() {
		return advertisementCampaigns;
	}

	public List<AdvertisementCampaign> getFilteredAdvertisementCampaigns(boolean currentAdvertisements, boolean futureAdvertisements, AdvertisementType type) {
		return advertisementCampaigns.stream()
				.filter(advertisement -> type == null || type.equals(advertisement.advertisementData().type()))
				.filter(advertisement -> currentAdvertisements || advertisement.startDate().isAfter(time.getTime().toLocalDate()))
				.filter(advertisement -> futureAdvertisements || !advertisement.startDate().isAfter(time.getTime().toLocalDate()))
				.sorted()
				.collect(Collectors.toList());
	}

}
