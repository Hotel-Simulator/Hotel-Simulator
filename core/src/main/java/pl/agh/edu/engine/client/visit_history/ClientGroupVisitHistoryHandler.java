package pl.agh.edu.engine.client.visit_history;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;

public class ClientGroupVisitHistoryHandler {
	private final Time time;
	private final TimeCommandExecutor timeCommandExecutor;
	private final List<ClientGroupVisit> clientGroupVisitHistory;

	public static void kryoRegister() {
		KryoConfig.kryo.register(ClientGroupVisitHistoryHandler.class, new Serializer<ClientGroupVisitHistoryHandler>() {
			@Override
			public void write(Kryo kryo, Output output, ClientGroupVisitHistoryHandler object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.timeCommandExecutor);
				kryo.writeObject(output, object.clientGroupVisitHistory, KryoConfig.listSerializer(ClientGroupVisit.class));
			}

			@Override
			public ClientGroupVisitHistoryHandler read(Kryo kryo, Input input, Class<? extends ClientGroupVisitHistoryHandler> type) {
				return new ClientGroupVisitHistoryHandler(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, TimeCommandExecutor.class),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(ClientGroupVisit.class)));
			}
		});
	}

	public ClientGroupVisitHistoryHandler() {
		this.time = Time.getInstance();
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.clientGroupVisitHistory = new ArrayList<>();
	}

	private ClientGroupVisitHistoryHandler(Time time,
			TimeCommandExecutor timeCommandExecutor,
			List<ClientGroupVisit> clientGroupVisitHistory) {
		this.time = time;
		this.timeCommandExecutor = timeCommandExecutor;
		this.clientGroupVisitHistory = clientGroupVisitHistory;
	}

	public void add(ClientGroupVisit clientGroupVisit) {
		clientGroupVisitHistory.add(clientGroupVisit);

		timeCommandExecutor.addCommand(
				new TimeCommand(
						() -> clientGroupVisitHistory.remove(clientGroupVisit),
						time.getTime().plusMonths(1)));
	}

	public List<ClientGroupVisit> getClientGroupVisitHistory() {
		return clientGroupVisitHistory;
	}
}
