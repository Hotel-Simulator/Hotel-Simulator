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
	private PriorityQueue<TimeCommand> commands;

	public static void kryoRegister() {
		KryoConfig.kryo.register(TimeCommandExecutor.class, new Serializer<TimeCommandExecutor>() {
			@Override
			public void write(Kryo kryo, Output output, TimeCommandExecutor object) {
				kryo.writeObject(output, object.commands);
			}

			@Override
			public TimeCommandExecutor read(Kryo kryo, Input input, Class<? extends TimeCommandExecutor> type) {
				TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();

				kryo.reference(timeCommandExecutor);

				timeCommandExecutor.commands = kryo.readObject(input, PriorityQueue.class);

				return timeCommandExecutor;
			}
		});
	}

	private TimeCommandExecutor() {
		this.commands = new PriorityQueue<>();
	}

	public static TimeCommandExecutor getInstance() {
		return instance;
	}

	public void addCommand(TimeCommand timeCommand) {
		commands.add(timeCommand);
	}

	public void executeCommands(LocalDateTime dateTime) {
		while (!commands.isEmpty() && !commands.peek().getDueDateTime().isAfter(dateTime)) {
			TimeCommand command = commands.poll();
			command.execute();
		}
	}
}
