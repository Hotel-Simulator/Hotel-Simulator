package pl.agh.edu.utils;

import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.serialization.KryoConfig;

public class LanguageString {
	public final String path;
	public final List<Pair<String, String>> replacementsList;

	static {
		KryoConfig.kryo.register(LanguageString.class, new Serializer<LanguageString>() {
			@Override
			public void write(Kryo kryo, Output output, LanguageString object) {
				kryo.writeObject(output, object.path);
				kryo.writeObject(output, object.replacementsList, KryoConfig.listSerializer(Pair.class));

			}

			@Override
			public LanguageString read(Kryo kryo, Input input, Class<? extends LanguageString> type) {
				return new LanguageString(
						kryo.readObject(input, String.class),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(Pair.class)));
			}
		});
	}

	public LanguageString(String path, List<Pair<String, String>> replacementsList) {
		this.path = path;
		this.replacementsList = replacementsList;
	}

	public LanguageString() {
		this("error.error");
	}

	public LanguageString(String path) {
		this(path, List.of());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LanguageString) {
			LanguageString other = (LanguageString) obj;
			return this.path.equals(other.path) && this.replacementsList.equals(other.replacementsList);
		}
		return false;
	}
}
