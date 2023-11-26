package pl.agh.edu.engine.client;

import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.type.AttractivenessConstantsData;
import pl.agh.edu.engine.advertisement.AdvertisementHandler;
import pl.agh.edu.engine.attraction.AttractionHandler;
import pl.agh.edu.engine.client.report.collector.ClientGroupReportDataCollector;
import pl.agh.edu.engine.event.temporary.ClientNumberModificationEventHandler;
import pl.agh.edu.engine.generator.ClientGenerator;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.hotel.dificulty.GameDifficultyManager;
import pl.agh.edu.engine.hotel.scenario.HotelScenariosManager;
import pl.agh.edu.engine.opinion.OpinionHandler;
import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.RandomUtils;

public class ClientGroupGenerationHandler {
	private final OpinionHandler opinionHandler;
	private final ClientGenerator clientGenerator;
	private final ClientNumberModificationEventHandler clientNumberModificationEventHandler;
	private final AdvertisementHandler advertisementHandler;
	private final AttractionHandler attractionHandler;
	private final HotelScenariosManager hotelScenariosManager;
	private final ClientGroupReportDataCollector clientGroupReportDataCollector;

	public static void kryoRegister() {
		KryoConfig.kryo.register(ClientGroupGenerationHandler.class, new Serializer<ClientGroupGenerationHandler>() {
			@Override
			public void write(Kryo kryo, Output output, ClientGroupGenerationHandler object) {
				kryo.writeObject(output, object.opinionHandler);
				kryo.writeObject(output, object.clientGenerator);
				kryo.writeObject(output, object.clientNumberModificationEventHandler);
				kryo.writeObject(output, object.advertisementHandler);
				kryo.writeObject(output, object.attractionHandler);
				kryo.writeObject(output, object.hotelScenariosManager);
				kryo.writeObject(output, object.clientGroupReportDataCollector);

			}

			@Override
			public ClientGroupGenerationHandler read(Kryo kryo, Input input, Class<? extends ClientGroupGenerationHandler> type) {
				return new ClientGroupGenerationHandler(
						kryo.readObject(input, OpinionHandler.class),
						kryo.readObject(input, ClientGenerator.class),
						kryo.readObject(input, ClientNumberModificationEventHandler.class),
						kryo.readObject(input, AdvertisementHandler.class),
						kryo.readObject(input, AttractionHandler.class),
						kryo.readObject(input, HotelScenariosManager.class),
						kryo.readObject(input, ClientGroupReportDataCollector.class));
			}
		});
	}

	public ClientGroupGenerationHandler(
			OpinionHandler opinionHandler,
			ClientNumberModificationEventHandler clientNumberModificationEventHandler,
			AdvertisementHandler advertisementHandler,
			AttractionHandler attractionHandler,
			HotelScenariosManager hotelScenariosManager,
			ClientGroupReportDataCollector clientGroupReportDataCollector,
			GameDifficultyManager gameDifficultyManager) {
		this.opinionHandler = opinionHandler;
		this.clientGenerator = new ClientGenerator(gameDifficultyManager, opinionHandler);
		this.clientNumberModificationEventHandler = clientNumberModificationEventHandler;
		this.advertisementHandler = advertisementHandler;
		this.attractionHandler = attractionHandler;
		this.hotelScenariosManager = hotelScenariosManager;
		this.clientGroupReportDataCollector = clientGroupReportDataCollector;
	}

	private ClientGroupGenerationHandler(OpinionHandler opinionHandler,
			ClientGenerator clientGenerator,
			ClientNumberModificationEventHandler clientNumberModificationEventHandler,
			AdvertisementHandler advertisementHandler,
			AttractionHandler attractionHandler,
			HotelScenariosManager hotelScenariosManager,
			ClientGroupReportDataCollector clientGroupReportDataCollector) {
		this.opinionHandler = opinionHandler;
		this.clientGenerator = clientGenerator;
		this.clientNumberModificationEventHandler = clientNumberModificationEventHandler;
		this.advertisementHandler = advertisementHandler;
		this.attractionHandler = attractionHandler;
		this.hotelScenariosManager = hotelScenariosManager;
		this.clientGroupReportDataCollector = clientGroupReportDataCollector;
	}

	public List<Arrival> getArrivalsForDay(LocalTime checkInMinTime) {
		EnumMap<HotelVisitPurpose, Integer> numberOfClientGroups = getNumberOfClientGroups();
		clientGroupReportDataCollector.collectData(numberOfClientGroups);
		return Stream.of(HotelVisitPurpose.values())
				.flatMap(e -> IntStream.range(0, numberOfClientGroups.get(e))
						.mapToObj(it -> new Arrival(
								RandomUtils.randomLocalTime(checkInMinTime, LocalTime.MIDNIGHT.minusHours(1)),
								clientGenerator.generateClientGroupForGivenHotelVisitPurpose(e))))
				.sorted(Arrival::compareTo)
				.collect(Collectors.toList());
	}

	private EnumMap<HotelVisitPurpose, Integer> getNumberOfClientGroups() {
		BigDecimal numberOfClientGroups = basicNumberOfClientGroups()
				.multiply(randomMultiplier())
				.multiply(opinionHandler.getOpinionModifier());

		return Stream.of(HotelVisitPurpose.values())
				.collect(Collectors.toMap(
						hotelVisitPurpose -> hotelVisitPurpose,
						hotelVisitPurpose -> getNumberOfClientGroupsByHotelVisitPurpose(numberOfClientGroups, hotelVisitPurpose),
						(a, b) -> b,
						() -> new EnumMap<>(HotelVisitPurpose.class)));
	}

	private Integer getNumberOfClientGroupsByHotelVisitPurpose(BigDecimal numberOfClientGroups,
			HotelVisitPurpose hotelVisitPurpose) {
		return numberOfClientGroups
				.multiply(hotelVisitPurposeMultiplier(hotelVisitPurpose))
				.multiply(multiplier(clientNumberModificationEventHandler, hotelVisitPurpose))
				.multiply(multiplier(advertisementHandler, hotelVisitPurpose))
				.multiply(multiplier(attractionHandler, hotelVisitPurpose))
				.setScale(0, HALF_EVEN)
				.intValue();
	}

	private BigDecimal basicNumberOfClientGroups() {
		AttractivenessConstantsData attractivenessConstants = hotelScenariosManager.getAttractivenessConstants();
		return BigDecimal.valueOf(attractivenessConstants.localMarket() + attractivenessConstants.localAttractions())
				.multiply(hotelScenariosManager.getCurrentDayMultiplier());
	}

	private BigDecimal hotelVisitPurposeMultiplier(HotelVisitPurpose hotelVisitPurpose) {
		return hotelScenariosManager.getHotelVisitPurposeProbabilities().get(hotelVisitPurpose);
	}

	private BigDecimal randomMultiplier() {
		return BigDecimal.valueOf(Math.max(0, RandomUtils.randomGaussian(1, 1. / 3)));
	}

	private BigDecimal multiplier(ClientGroupModifierSupplier supplier, HotelVisitPurpose hotelVisitPurpose) {
		return ONE.add(supplier.getCumulatedModifier().get(hotelVisitPurpose));
	}

}
