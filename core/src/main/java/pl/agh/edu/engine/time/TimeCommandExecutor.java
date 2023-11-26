package pl.agh.edu.engine.time;

import java.time.LocalDateTime;
import java.util.PriorityQueue;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.time.command.Command;
import pl.agh.edu.serialization.KryoConfig;

public class TimeCommandExecutor {
	private static TimeCommandExecutor instance = new TimeCommandExecutor();
	private PriorityQueue<Command> commandQueue;
	private final PriorityQueue<Command> unserializableCommandQueue;

	public static void kryoRegister() {
		KryoConfig.kryo.register(TimeCommandExecutor.class, new Serializer<TimeCommandExecutor>() {
			@Override
			public void write(Kryo kryo, Output output, TimeCommandExecutor object) {
				kryo.writeObject(output, object.commandQueue);
			}

			@Override
			public TimeCommandExecutor read(Kryo kryo, Input input, Class<? extends TimeCommandExecutor> type) {
				TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();

				kryo.reference(timeCommandExecutor);

				timeCommandExecutor.commandQueue = kryo.readObject(input, PriorityQueue.class);

				return timeCommandExecutor;
			}
		});
	}

	private TimeCommandExecutor() {
		this.commandQueue = new PriorityQueue<>();
		this.unserializableCommandQueue = new PriorityQueue<>();
	}

	public static TimeCommandExecutor getInstance() {
		if (instance == null)
			instance = new TimeCommandExecutor();
		return instance;
	}

	public void addCommand(Command command, Boolean isSerializable) {
		if (isSerializable) {
			commandQueue.add(command);
		} else {
			unserializableCommandQueue.add(command);
		}
	}

	public void addCommand(Command command) {
		addCommand(command, true);
	}

	public void executeCommands(LocalDateTime dateTime) {
		executeQueuedCommands(commandQueue, dateTime);
		executeQueuedCommands(unserializableCommandQueue, dateTime);
	}

	public void executeQueuedCommands(PriorityQueue<Command> commandQueue, LocalDateTime dateTime) {
		while (!commandQueue.isEmpty() && !commandQueue.peek().getDueDateTime().isAfter(dateTime)) {
			Command command = commandQueue.poll();
			command.execute();
			if (command.isRequeueNeeded())
				commandQueue.add(command);
		}
	}
}
