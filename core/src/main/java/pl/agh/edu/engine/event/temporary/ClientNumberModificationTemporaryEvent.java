package pl.agh.edu.engine.event.temporary;

import java.time.LocalDate;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.event.ClientNumberModifier;
import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.LanguageString;

public class ClientNumberModificationTemporaryEvent extends TemporaryEvent {
	public final int durationInDays;
	public final ClientNumberModifier modifier;

	static {
		KryoConfig.kryo.register(ClientNumberModificationTemporaryEvent.class, new Serializer<ClientNumberModificationTemporaryEvent>() {
			@Override
			public void write(Kryo kryo, Output output, ClientNumberModificationTemporaryEvent object) {
				kryo.writeObject(output, object.title);
				kryo.writeObject(output, object.eventAppearancePopupDescription);
				kryo.writeObject(output, object.eventStartPopupDescription);
				kryo.writeObject(output, object.calendarDescription);
				kryo.writeObject(output, object.imagePath);
				kryo.writeObject(output, object.appearanceDate);
				kryo.writeObject(output, object.startDate);
				kryo.writeObject(output, object.durationInDays);
				kryo.writeObject(output, object.modifier);
			}

			@Override
			public ClientNumberModificationTemporaryEvent read(Kryo kryo, Input input, Class<? extends ClientNumberModificationTemporaryEvent> type) {
				return new ClientNumberModificationTemporaryEvent(
						kryo.readObject(input, LanguageString.class),
						kryo.readObject(input, LanguageString.class),
						kryo.readObject(input, LanguageString.class),
						kryo.readObject(input, LanguageString.class),
						kryo.readObject(input, String.class),
						kryo.readObject(input, LocalDate.class),
						kryo.readObject(input, LocalDate.class),
						kryo.readObject(input, Integer.class),
						kryo.readObject(input, ClientNumberModifier.class));
			}
		});
	}

	public ClientNumberModificationTemporaryEvent(LanguageString title,
			LanguageString eventAppearancePopupDescription,
			LanguageString eventStartPopupDescription,
			LanguageString calendarDescription,
			String imagePath,
			LocalDate appearanceDate,
			LocalDate startDate,
			int durationInDays,
			ClientNumberModifier modifier) {
		super(title, eventAppearancePopupDescription,
				eventStartPopupDescription,
				calendarDescription,
				imagePath,
				appearanceDate,
				startDate);
		this.durationInDays = durationInDays;
		this.modifier = modifier;
	}
}
