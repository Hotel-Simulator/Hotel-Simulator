package pl.agh.edu.engine.time;

import java.time.LocalDateTime;
import java.util.PriorityQueue;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;

public class TimeCommandExecutor {
	private static final TimeCommandExecutor instance = new TimeCommandExecutor();
	private final PriorityQueue<TimeCommand> commandQueue;
	private final PriorityQueue<TimeCommand> unserializableCommandQueue;

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

				PriorityQueue<TimeCommand> commandQueue = kryo.readObject(input, PriorityQueue.class);
				commandQueue.forEach(timeCommandExecutor::addCommand);

				return timeCommandExecutor;
			}
		});
	}

	private TimeCommandExecutor() {
		this.commandQueue = new PriorityQueue<>();
		this.unserializableCommandQueue = new PriorityQueue<>();
	}

	private TimeCommandExecutor(PriorityQueue<TimeCommand> commandQueue) {
		this.commandQueue = commandQueue;
		this.unserializableCommandQueue = new PriorityQueue<>();
	}

	public static TimeCommandExecutor getInstance() {
		return instance;
	}

	public void addCommand(TimeCommand timeCommand) {
		this.addCommand(timeCommand, true);
	}

	public void addCommand(TimeCommand timeCommand, boolean isSerializable) {
		if (isSerializable) {
			commandQueue.add(timeCommand);
		} else {
			unserializableCommandQueue.add(timeCommand);
		}
	}

	public void executeCommands(LocalDateTime dateTime) {
		executeQueuedCommands(commandQueue, dateTime);
		executeQueuedCommands(unserializableCommandQueue, dateTime);
	}

	public void executeQueuedCommands(PriorityQueue<TimeCommand> commandQueue, LocalDateTime dateTime) {
		while (!commandQueue.isEmpty() && !commandQueue.peek().getDueDateTime().isAfter(dateTime)) {
			TimeCommand command = commandQueue.poll();
			command.execute();
		}
	}
}
