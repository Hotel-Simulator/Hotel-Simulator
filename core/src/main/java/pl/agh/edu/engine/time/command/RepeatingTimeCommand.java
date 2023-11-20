package pl.agh.edu.engine.time.command;

import java.time.LocalDateTime;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ClosureSerializer;

import pl.agh.edu.engine.time.Frequency;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.serialization.KryoConfig;

public class RepeatingTimeCommand extends TimeCommand {

	protected final Frequency frequency;
	protected Boolean toStop = false;

	public static void kryoRegister() {
		KryoConfig.kryo.register(RepeatingTimeCommand.class, new Serializer<RepeatingTimeCommand>() {
			@Override
			public void write(Kryo kryo, Output output, RepeatingTimeCommand object) {
				kryo.writeObject(output, object.frequency);
				kryo.writeObject(output, object.toExecute);
				kryo.writeObject(output, object.dueDateTime);
				kryo.writeObject(output, KryoConfig.getPrivateFieldValue(object, "version", Long.class));
				kryo.writeObject(output, object.toStop);
			}

			@Override
			public RepeatingTimeCommand read(Kryo kryo, Input input, Class<? extends RepeatingTimeCommand> type) {
				RepeatingTimeCommand repeatingTimeCommand = new RepeatingTimeCommand(
						kryo.readObject(input, Frequency.class),
						(SerializableRunnable) kryo.readObject(input, ClosureSerializer.Closure.class),
						kryo.readObject(input, LocalDateTime.class),
						kryo.readObject(input, Long.class));

				repeatingTimeCommand.toStop = kryo.readObject(input, Boolean.class);
				return repeatingTimeCommand;
			}
		});
	}

	public RepeatingTimeCommand(Frequency frequency, SerializableRunnable toExecute, LocalDateTime dueTime) {
		this(frequency, toExecute, dueTime, true);
	}

	public RepeatingTimeCommand(Frequency frequency, SerializableRunnable toExecute, LocalDateTime dueTime, Boolean isSerializable) {
		super(toExecute, dueTime, isSerializable);
		this.frequency = frequency;
	}

	protected RepeatingTimeCommand(Frequency frequency, SerializableRunnable toExecute, LocalDateTime dueTime, Long version) {
		super(toExecute, dueTime, version);
		this.frequency = frequency;
	}

	@Override
	public void execute() {
		if (!toStop) {
			toExecute.run();
			updateDueDateTime();
			TimeCommandExecutor.getInstance().addCommand(this);
		}
	}

	private void updateDueDateTime() {
		dueDateTime = frequency.add(dueDateTime);
	}

	public void stop() {
		toStop = true;
	}
}
