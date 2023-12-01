package pl.agh.edu.engine.client;

import static pl.agh.edu.engine.client.visit_history.VisitResult.STEPPED_OUT_OF_QUEUE;

import java.time.LocalDateTime;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONOpinionDataLoader;
import pl.agh.edu.engine.client.visit_history.ClientGroupVisit;
import pl.agh.edu.engine.client.visit_history.ClientGroupVisitHistoryHandler;
import pl.agh.edu.engine.employee.scheduler.ReceptionScheduler;
import pl.agh.edu.engine.hotel.Hotel;
import pl.agh.edu.engine.opinion.OpinionBuilder;
import pl.agh.edu.engine.opinion.OpinionHandler;
import pl.agh.edu.engine.room.RoomSize;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;

public class ClientGroupArrivalGenerationHandler {
	private final Time time;
	private final TimeCommandExecutor timeCommandExecutor;
	private final OpinionHandler opinionHandler;
	private final Hotel hotel;
	private final ClientGroupGenerationHandler clientGroupGenerationHandler;
	private final ReceptionScheduler receptionScheduler;
	private final ClientGroupVisitHistoryHandler clientGroupVisitHistoryHandler;

	public static void kryoRegister() {
		KryoConfig.kryo.register(ClientGroupArrivalGenerationHandler.class, new Serializer<ClientGroupArrivalGenerationHandler>() {
			@Override
			public void write(Kryo kryo, Output output, ClientGroupArrivalGenerationHandler object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.timeCommandExecutor);
				kryo.writeObject(output, object.opinionHandler);
				kryo.writeObject(output, object.hotel);
				kryo.writeObject(output, object.clientGroupGenerationHandler);
				kryo.writeObject(output, object.receptionScheduler);
				kryo.writeObject(output, object.clientGroupVisitHistoryHandler);

			}

			@Override
			public ClientGroupArrivalGenerationHandler read(Kryo kryo, Input input, Class<? extends ClientGroupArrivalGenerationHandler> type) {
				return new ClientGroupArrivalGenerationHandler(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, TimeCommandExecutor.class),
						kryo.readObject(input, OpinionHandler.class),
						kryo.readObject(input, Hotel.class),
						kryo.readObject(input, ClientGroupGenerationHandler.class),
						kryo.readObject(input, ReceptionScheduler.class),
						kryo.readObject(input, ClientGroupVisitHistoryHandler.class));
			}
		});
	}

	public ClientGroupArrivalGenerationHandler(OpinionHandler opinionHandler,
			Hotel hotel,
			ClientGroupGenerationHandler clientGroupGenerationHandler,
			ReceptionScheduler receptionScheduler,
			ClientGroupVisitHistoryHandler clientGroupVisitHistoryHandler) {
		this.time = Time.getInstance();
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.opinionHandler = opinionHandler;
		this.hotel = hotel;
		this.clientGroupGenerationHandler = clientGroupGenerationHandler;
		this.receptionScheduler = receptionScheduler;
		this.clientGroupVisitHistoryHandler = clientGroupVisitHistoryHandler;
	}

	private ClientGroupArrivalGenerationHandler(
			Time time,
			TimeCommandExecutor timeCommandExecutor,
			OpinionHandler opinionHandler,
			Hotel hotel,
			ClientGroupGenerationHandler clientGroupGenerationHandler,
			ReceptionScheduler receptionScheduler,
			ClientGroupVisitHistoryHandler clientGroupVisitHistoryHandler) {
		this.time = time;
		this.timeCommandExecutor = timeCommandExecutor;
		this.opinionHandler = opinionHandler;
		this.hotel = hotel;
		this.clientGroupGenerationHandler = clientGroupGenerationHandler;
		this.receptionScheduler = receptionScheduler;
		this.clientGroupVisitHistoryHandler = clientGroupVisitHistoryHandler;
	}

	public void dailyUpdate() {
		clientGroupGenerationHandler.getArrivalsForDay(hotel.getCheckInTime())
				.forEach(arrival -> timeCommandExecutor.addCommand(
						createTimeCommandForClientArrival(arrival)));
	}

	private TimeCommand createTimeCommandForClientArrival(Arrival arrival) {
		return new TimeCommand(() -> {
			OpinionBuilder.saveStartWaitingAtQueueData(arrival.clientGroup(), time.getTime());
			receptionScheduler.addEntity(arrival.clientGroup());
			timeCommandExecutor.addCommand(
					new TimeCommand(() -> {
						if (receptionScheduler.removeEntity(arrival.clientGroup())) {
							OpinionBuilder.saveSteppingOutOfQueueData(arrival.clientGroup());
							opinionHandler.addOpinionWithProbability(arrival.clientGroup(), JSONOpinionDataLoader.opinionProbabilityForClientWhoSteppedOutOfQueue);
							clientGroupVisitHistoryHandler.add(
									new ClientGroupVisit(
											time.getTime(),
											arrival.clientGroup().getDesiredRoomRank(),
											RoomSize.getSmallestAvailableRoomSize(arrival.clientGroup().getSize()).orElseThrow(),
											STEPPED_OUT_OF_QUEUE.languageString));
						}
					}, LocalDateTime.of(time.getTime().toLocalDate(), arrival.time()).plus(arrival.clientGroup().getMaxWaitingTime())));
		}, LocalDateTime.of(time.getTime().toLocalDate(), arrival.time()));
	}

}
