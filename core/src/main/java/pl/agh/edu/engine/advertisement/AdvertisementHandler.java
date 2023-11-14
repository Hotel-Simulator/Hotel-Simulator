package pl.agh.edu.engine.advertisement;

import static java.math.BigDecimal.ONE;
import static java.time.LocalTime.MIDNIGHT;
import static java.time.temporal.ChronoUnit.DAYS;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONAdvertisementDataLoader;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.client.ClientGroupModifierSupplier;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;

public class AdvertisementHandler extends ClientGroupModifierSupplier {
	private final Time time;
	private final TimeCommandExecutor timeCommandExecutor;
	private final BankAccountHandler bankAccountHandler;
	private final List<AdvertisementCampaign> advertisementCampaigns;

	static {
		KryoConfig.kryo.register(AdvertisementHandler.class, new Serializer<AdvertisementHandler>() {
			@Override
			public void write(Kryo kryo, Output output, AdvertisementHandler object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.timeCommandExecutor);
				kryo.writeObject(output, object.bankAccountHandler);
				kryo.writeObject(output, object.advertisementCampaigns, KryoConfig.listSerializer(AdvertisementCampaign.class));
			}

			@Override
			public AdvertisementHandler read(Kryo kryo, Input input, Class<? extends AdvertisementHandler> type) {
				return new AdvertisementHandler(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, TimeCommandExecutor.class),
						kryo.readObject(input, BankAccountHandler.class),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(AdvertisementCampaign.class)));
			}
		});
	}

	public AdvertisementHandler(BankAccountHandler bankAccountHandler) {
		this.time = Time.getInstance();
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.bankAccountHandler = bankAccountHandler;
		this.advertisementCampaigns = new ArrayList<>();
	}

	private AdvertisementHandler(Time time,
			TimeCommandExecutor timeCommandExecutor,
			BankAccountHandler bankAccountHandler,
			List<AdvertisementCampaign> advertisementCampaigns) {
		this.time = time;
		this.timeCommandExecutor = timeCommandExecutor;
		this.bankAccountHandler = bankAccountHandler;
		this.advertisementCampaigns = advertisementCampaigns;
	}

	public static BigDecimal getCampaignFullCost(AdvertisementType type, long noDays) {
		return JSONAdvertisementDataLoader.advertisementData.get(type).costPerDay()
				.multiply(BigDecimal.valueOf(noDays))
				.multiply(getModifier(noDays));
	}

	private static BigDecimal getModifier(long noDays) {
		if (noDays >= 28) {
			return new BigDecimal("0.8");
		}
		if (noDays >= 14) {
			return new BigDecimal("0.9");
		}
		return ONE;
	}

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
		bankAccountHandler.registerExpense(getCampaignFullCost(type, DAYS.between(startDate, endDate)));
		timeCommandExecutor.addCommand(new TimeCommand(() -> advertisementCampaigns.remove(advertisementCampaign),
				endDate.atTime(MIDNIGHT).minusMinutes(1)));

		advertisementCampaigns.add(advertisementCampaign);
	}

	@Override
	public EnumMap<HotelVisitPurpose, BigDecimal> getCumulatedModifier() {
		return advertisementCampaigns.stream()
				.filter(this::isCurrentlyEmitted)
				.map(advertisementCampaigns -> advertisementCampaigns.advertisementData().effectiveness())
				.reduce(getIdentity(), getAccumulator());
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
