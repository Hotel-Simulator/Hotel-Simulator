package pl.agh.edu.engine.time.command;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;

import pl.agh.edu.serialization.KryoConfig;

public class TimeCommand implements Comparable<TimeCommand> {
	private static final AtomicLong creationVersion = new AtomicLong(1L);
	protected final SerializableRunnable toExecute;
	private final Long version;
	protected LocalDateTime dueDateTime;

	public static void kryoRegister() {
		KryoConfig.kryo.register(TimeCommand.class, new Serializer<TimeCommand>() {

			@Override
			public void write(Kryo kryo, Output output, TimeCommand object) {
				kryo.writeObject(output, object.toExecute);
				kryo.writeObject(output, object.dueDateTime);
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

	public TimeCommand(SerializableRunnable toExecute, LocalDateTime dueDateTime) {
		this.toExecute = toExecute;
		this.dueDateTime = dueDateTime;
		this.version = creationVersion.getAndIncrement();
	}

	protected TimeCommand(SerializableRunnable toExecute, LocalDateTime dueDateTime, Long version) {
		this.toExecute = toExecute;
		this.dueDateTime = dueDateTime;
		this.version = version;
	}

	public boolean execute() {
		if (toExecute != null)
			toExecute.run();
		return false;
	}

	public LocalDateTime getDueDateTime() {
		return dueDateTime;
	}

	@Override
	public int compareTo(TimeCommand other) {
		int dueDateTimeComparison = getDueDateTime().compareTo(other.getDueDateTime());
		if (dueDateTimeComparison != 0) {
			return dueDateTimeComparison;
		}

		return version.compareTo(other.version);
	}
}
