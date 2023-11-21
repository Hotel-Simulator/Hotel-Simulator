package pl.agh.edu.engine.time.command;

import java.time.LocalDateTime;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;

import pl.agh.edu.engine.time.Frequency;
import pl.agh.edu.serialization.KryoConfig;

public class NRepeatingTimeCommand extends RepeatingTimeCommand {
	private final SerializableRunnable toExecuteAfterLastRepetition;
	private long counter;

	public static void kryoRegister() {
		KryoConfig.kryo.register(NRepeatingTimeCommand.class, new Serializer<NRepeatingTimeCommand>() {
			@Override
			public void write(Kryo kryo, Output output, NRepeatingTimeCommand object) {
				kryo.writeObject(output, object.frequency);
				kryo.writeObject(output, object.toExecute);
				kryo.writeObject(output, object.dueDateTime);
				kryo.writeObject(output, object.counter);
				kryo.writeObject(output, object.toExecuteAfterLastRepetition);
				kryo.writeObject(output, KryoConfig.getPrivateFieldValue(object, "version", Long.class));
				kryo.writeObject(output, object.toStop);
			}

			@Override
			public NRepeatingTimeCommand read(Kryo kryo, Input input, Class<? extends NRepeatingTimeCommand> type) {
				NRepeatingTimeCommand nRepeatingTimeCommand = new NRepeatingTimeCommand(
						kryo.readObject(input, Frequency.class),
						(SerializableRunnable) kryo.readObject(input, ClosureSerializer.Closure.class),
						kryo.readObject(input, LocalDateTime.class),
						kryo.readObject(input, Long.class),
						(SerializableRunnable) kryo.readObject(input, ClosureSerializer.Closure.class),
						kryo.readObject(input, Long.class));

				nRepeatingTimeCommand.toStop = kryo.readObject(input, Boolean.class);
				return nRepeatingTimeCommand;
			}
		});
	}

	public NRepeatingTimeCommand(Frequency frequency,
			SerializableRunnable toExecute,
			LocalDateTime dueTime,
			long N,
			SerializableRunnable toExecuteAfterLastRepetition) {
		super(frequency, toExecute, dueTime);
		this.counter = N;
		this.toExecuteAfterLastRepetition = toExecuteAfterLastRepetition;
	}

	public NRepeatingTimeCommand(Frequency frequency, SerializableRunnable toExecute, LocalDateTime dueTime, long N) {
		this(frequency, toExecute, dueTime, N, () -> {});
	}

	private NRepeatingTimeCommand(
			Frequency frequency,
			SerializableRunnable toExecute,
			LocalDateTime dueTime,
			long N,
			SerializableRunnable toExecuteAfterLastRepetition,
			Long version) {
		super(frequency, toExecute, dueTime, version);
		this.counter = N;
		this.toExecuteAfterLastRepetition = toExecuteAfterLastRepetition;
	}

	public long getCounter() {
		return counter;
	}

	@Override
	public void execute(Runnable postAction) {
		super.execute(postAction);
		if (--counter == 0 && !toStop) {
			stop();
			toExecuteAfterLastRepetition.run();
		}
	}
}
