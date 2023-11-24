package pl.agh.edu.engine.time.command;

import java.time.LocalDateTime;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;

import pl.agh.edu.engine.time.Frequency;
import pl.agh.edu.serialization.KryoConfig;

public class NRepeatingTimeCommand extends Command {
	private final SerializableRunnable toExecuteAfterLastRepetition;
	private final Frequency frequency;
	private Long counter;

	public static void kryoRegister() {
		KryoConfig.kryo.register(NRepeatingTimeCommand.class, new Serializer<NRepeatingTimeCommand>() {
			@Override
			public void write(Kryo kryo, Output output, NRepeatingTimeCommand object) {
				kryo.writeObject(output, object.frequency);
				kryo.writeObject(output, object.toExecute);
				kryo.writeObject(output, object.getDueDateTime());
				kryo.writeObject(output, object.counter);
				kryo.writeObject(output, object.toExecuteAfterLastRepetition);
				kryo.writeObject(output, KryoConfig.getPrivateFieldValue(object, "version", Long.class));
				kryo.writeObject(output, object.isStopped());
			}

			@Override
			public NRepeatingTimeCommand read(Kryo kryo, Input input, Class<? extends NRepeatingTimeCommand> type) {

				return new NRepeatingTimeCommand(
						kryo.readObject(input, Frequency.class),
						(SerializableRunnable) kryo.readObject(input, ClosureSerializer.Closure.class),
						kryo.readObject(input, LocalDateTime.class),
						kryo.readObject(input, Long.class),
						(SerializableRunnable) kryo.readObject(input, ClosureSerializer.Closure.class),
						kryo.readObject(input, Long.class),
						kryo.readObject(input, Boolean.class)
				);
			}
		});
	}

	public NRepeatingTimeCommand(
			Frequency frequency,
			SerializableRunnable toExecute,
			LocalDateTime dueTime,
			Long N,
			SerializableRunnable toExecuteAfterLastRepetition) {
		super(toExecute, dueTime);
		this.frequency = frequency;
		this.counter = N;
		this.toExecuteAfterLastRepetition = toExecuteAfterLastRepetition;
	}

	public NRepeatingTimeCommand(
			Frequency frequency,
			SerializableRunnable toExecute,
			LocalDateTime dueTime,
			Long N) {
		this(frequency, toExecute, dueTime, N, () -> {});
	}

	private NRepeatingTimeCommand(
			Frequency frequency,
			SerializableRunnable toExecute,
			LocalDateTime dueTime,
			Long N,
			SerializableRunnable toExecuteAfterLastRepetition,
			Long version,
			boolean toStop) {
		super(toExecute, dueTime, version, toStop);
		this.frequency = frequency;
		this.counter = N;
		this.toExecuteAfterLastRepetition = toExecuteAfterLastRepetition;
	}

	@Override
	public void execute() {
		if (isStopped() || counter <= 0)
			return;
		toExecute.run();
		counter -= 1;
		if (counter == 0) {
			toExecuteAfterLastRepetition.run();
			return;
		}
		setDueDateTime(frequency.add(getDueDateTime()));
	}

	@Override
	public boolean isRequeueNeeded() {
		return !isStopped() || counter > 0;
	}

	public long getCounter() {
		return counter;
	}
}
