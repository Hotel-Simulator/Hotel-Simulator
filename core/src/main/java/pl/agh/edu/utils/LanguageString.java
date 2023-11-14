package pl.agh.edu.utils;

import java.util.List;
import java.util.Objects;

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
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		LanguageString that = (LanguageString) o;
		return Objects.equals(path, that.path) && Objects.equals(replacementsList, that.replacementsList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(path, replacementsList);
	}
}
