package pl.agh.edu.engine.event.temporary;

import java.time.LocalDate;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.LanguageString;

public class TemporaryEvent {
	public final LanguageString title;
	public final LanguageString eventAppearancePopupDescription;
	public final LanguageString eventStartPopupDescription;
	public final LanguageString calendarDescription;
	public final String imagePath;
	public final LocalDate appearanceDate;
	public final LocalDate startDate;

	static {
		KryoConfig.kryo.register(TemporaryEvent.class, new Serializer<TemporaryEvent>() {
			@Override
			public void write(Kryo kryo, Output output, TemporaryEvent object) {
				kryo.writeObject(output, object.title);
				kryo.writeObject(output, object.eventAppearancePopupDescription);
				kryo.writeObject(output, object.eventStartPopupDescription);
				kryo.writeObject(output, object.calendarDescription);
				kryo.writeObject(output, object.imagePath);
				kryo.writeObject(output, object.appearanceDate);
				kryo.writeObject(output, object.startDate);
			}

			@Override
			public TemporaryEvent read(Kryo kryo, Input input, Class<? extends TemporaryEvent> type) {
				return new TemporaryEvent(
						kryo.readObject(input, LanguageString.class),
						kryo.readObject(input, LanguageString.class),
						kryo.readObject(input, LanguageString.class),
						kryo.readObject(input, LanguageString.class),
						kryo.readObject(input, String.class),
						kryo.readObject(input, LocalDate.class),
						kryo.readObject(input, LocalDate.class));
			}
		});
	}

	public TemporaryEvent(LanguageString title,
			LanguageString eventAppearancePopupDescription,
			LanguageString eventStartPopupDescription,
			LanguageString calendarDescription,
			String imagePath,
			LocalDate appearanceDate,
			LocalDate startDate) {
		this.title = title;
		this.eventAppearancePopupDescription = eventAppearancePopupDescription;
		this.eventStartPopupDescription = eventStartPopupDescription;
		this.calendarDescription = calendarDescription;
		this.imagePath = imagePath;
		this.appearanceDate = appearanceDate;
		this.startDate = startDate;
	}
}
