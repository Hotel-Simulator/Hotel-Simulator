package pl.agh.edu.engine.time.command;

import java.time.LocalDateTime;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;

import pl.agh.edu.serialization.KryoConfig;

public class TimeCommand extends Command {
	public static void kryoRegister() {
		KryoConfig.kryo.register(TimeCommand.class, new Serializer<TimeCommand>() {

			@Override
			public void write(Kryo kryo, Output output, TimeCommand object) {
				kryo.writeObject(output, object.toExecute);
				kryo.writeObject(output, object.getDueDateTime());
				kryo.writeObject(output, object.version);
			}

			@Override
			public TimeCommand read(Kryo kryo, Input input, Class<? extends TimeCommand> type) {

				return new TimeCommand(
						(SerializableRunnable) kryo.readObject(input, ClosureSerializer.Closure.class),
						kryo.readObject(input, LocalDateTime.class),
						kryo.readObject(input, Long.class));
			}
		});
	}

	public TimeCommand(
			SerializableRunnable toExecute,
			LocalDateTime dueDateTime) {
		super(toExecute, dueDateTime);
	}

	private TimeCommand(
			SerializableRunnable toExecute,
			LocalDateTime dueDateTime,
			Long version) {
		super(toExecute, dueDateTime, version);
	}

	@Override
	public void execute() {
		if (isStoped())
			return;
		toExecute.run();
	}

	@Override
	public boolean isRequeueNeeded() {
		return false;
	}
}
