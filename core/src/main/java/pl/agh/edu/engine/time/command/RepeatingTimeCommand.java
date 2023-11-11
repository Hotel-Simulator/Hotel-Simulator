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
	private static final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	protected final Frequency frequency;
	protected Boolean toStop = false;

	static {
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
						kryo.readObject(input, LocalDateTime.class));
				KryoConfig.setPrivateFieldValue(repeatingTimeCommand, "version", kryo.readObject(input, Long.class));
				repeatingTimeCommand.toStop = kryo.readObject(input, Boolean.class);
				return repeatingTimeCommand;
			}
		});
	}

	public RepeatingTimeCommand(Frequency frequency, SerializableRunnable toExecute, LocalDateTime dueTime) {
		super(toExecute, dueTime);
		this.frequency = frequency;
	}

	@Override
	public void execute() {
		if (!toStop) {
			toExecute.run();
			repeat();
		}
	}

	protected void repeat() {
		if (!toStop) {
			updateDueDateTime();
			timeCommandExecutor.addCommand(this);
		}
	}

	private void updateDueDateTime() {
		dueDateTime = frequency.add(dueDateTime);
	}

	public void stop() {
		toStop = true;
	}
}
