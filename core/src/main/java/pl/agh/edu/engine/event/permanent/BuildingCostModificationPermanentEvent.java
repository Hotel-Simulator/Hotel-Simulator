package pl.agh.edu.engine.event.permanent;

import java.time.LocalDate;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.LanguageString;

public class BuildingCostModificationPermanentEvent {
	public final LanguageString title;
	public final LanguageString eventAppearancePopupDescription;
	public final LocalDate appearanceDate;
	public final int modifierValueInPercent;
	public final String imagePath;

	static {
		KryoConfig.kryo.register(BuildingCostModificationPermanentEvent.class, new Serializer<BuildingCostModificationPermanentEvent>() {
			@Override
			public void write(Kryo kryo, Output output, BuildingCostModificationPermanentEvent object) {
				kryo.writeObject(output, object.title);
				kryo.writeObject(output, object.eventAppearancePopupDescription);
				kryo.writeObject(output, object.appearanceDate);
				kryo.writeObject(output, object.modifierValueInPercent);
				kryo.writeObject(output, object.imagePath);
			}

			@Override
			public BuildingCostModificationPermanentEvent read(Kryo kryo, Input input, Class<? extends BuildingCostModificationPermanentEvent> type) {
				return new BuildingCostModificationPermanentEvent(
						kryo.readObject(input, LanguageString.class),
						kryo.readObject(input, LanguageString.class),
						kryo.readObject(input, LocalDate.class),
						kryo.readObject(input, Integer.class),
						kryo.readObject(input, String.class));
			}
		});
	}

	public BuildingCostModificationPermanentEvent(LanguageString title,
			LanguageString eventAppearancePopupDescription,
			LocalDate appearanceDate,
			int modifierValueInPercent,
			String imagePath) {
		this.title = title;
		this.eventAppearancePopupDescription = eventAppearancePopupDescription;
		this.appearanceDate = appearanceDate;
		this.modifierValueInPercent = modifierValueInPercent;
		this.imagePath = imagePath;
	}
}
