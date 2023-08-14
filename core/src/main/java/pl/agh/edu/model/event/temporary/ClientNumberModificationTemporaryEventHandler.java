package pl.agh.edu.model.event.temporary;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.agh.edu.enums.HotelVisitPurpose;

public class ClientNumberModificationTemporaryEventHandler {
	private static ClientNumberModificationTemporaryEventHandler instance;
	private final PriorityQueue<ClientNumberModificationTemporaryEvent> currentClientNumberModificationTemporaryEvents;
	private final PriorityQueue<ClientNumberModificationTemporaryEvent> upcomingClientNumberModificationTemporaryEvents;

	private ClientNumberModificationTemporaryEventHandler() {
		this.currentClientNumberModificationTemporaryEvents = new PriorityQueue<>(Comparator.comparing(TemporaryEvent::getEndDate));
		this.upcomingClientNumberModificationTemporaryEvents = new PriorityQueue<>(Comparator.comparing(TemporaryEvent::getStartDate));
	}

	public static ClientNumberModificationTemporaryEventHandler getInstance() {
		if (instance == null)
			instance = new ClientNumberModificationTemporaryEventHandler();
		return instance;
	}

	public void update(LocalDate currentDate) {
		ClientNumberModificationTemporaryEvent event = currentClientNumberModificationTemporaryEvents.peek();
		while (event != null && event.getEndDate().isEqual(currentDate)) {
			currentClientNumberModificationTemporaryEvents.poll();
			event = currentClientNumberModificationTemporaryEvents.peek();
		}
		event = upcomingClientNumberModificationTemporaryEvents.peek();
		while (event != null && event.getStartDate().isEqual(currentDate)) {
			currentClientNumberModificationTemporaryEvents.add(upcomingClientNumberModificationTemporaryEvents.poll());
			event = upcomingClientNumberModificationTemporaryEvents.peek();

		}
	}

	public boolean add(ClientNumberModificationTemporaryEvent event) {
		return upcomingClientNumberModificationTemporaryEvents.add(event);
	}

	public EnumMap<HotelVisitPurpose, Double> getClientNumberModifier() {
		return currentClientNumberModificationTemporaryEvents.stream()
				.map(ClientNumberModificationTemporaryEvent::getModifier)
				.reduce(
						Stream.of(HotelVisitPurpose.values()).collect(Collectors.toMap(
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
}
