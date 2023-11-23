package pl.agh.edu.engine.time.command;

import java.time.LocalDateTime;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;

import pl.agh.edu.engine.time.Frequency;
import pl.agh.edu.serialization.KryoConfig;

public class RepeatingTimeCommand extends Command {
	private final Frequency frequency;

	public static void kryoRegister() {
		KryoConfig.kryo.register(RepeatingTimeCommand.class, new Serializer<RepeatingTimeCommand>() {
			@Override
			public void write(Kryo kryo, Output output, RepeatingTimeCommand object) {
				kryo.writeObject(output, object.frequency);
				kryo.writeObject(output, object.toExecute);
				kryo.writeObject(output, object.getDueDateTime());
				kryo.writeObject(output, object.version);
				kryo.writeObject(output, object.isStoped());
			}

			@Override
			public RepeatingTimeCommand read(Kryo kryo, Input input, Class<? extends RepeatingTimeCommand> type) {
				RepeatingTimeCommand repeatingTimeCommand = new RepeatingTimeCommand(
						kryo.readObject(input, Frequency.class),
						(SerializableRunnable) kryo.readObject(input, ClosureSerializer.Closure.class),
						kryo.readObject(input, LocalDateTime.class),
						kryo.readObject(input, Long.class));

				if (kryo.readObject(input, Boolean.class)) {
					repeatingTimeCommand.stop();
				}

				return repeatingTimeCommand;
			}
		});
	}

	public RepeatingTimeCommand(
			Frequency frequency,
			SerializableRunnable toExecute,
			LocalDateTime dueTime) {
		super(toExecute, dueTime);
		this.frequency = frequency;
	}

	private RepeatingTimeCommand(
			Frequency frequency,
			SerializableRunnable toExecute,
			LocalDateTime dueTime,
			Long version) {
		super(toExecute, dueTime, version);
		this.frequency = frequency;
	}

	@Override
	public void execute() {
		if (isStoped())
			return;
		toExecute.run();
		setDueDateTime(frequency.add(getDueDateTime()));
	}

	@Override
	public boolean isRequeueNeeded() {
		return !isStoped();
	}

}
