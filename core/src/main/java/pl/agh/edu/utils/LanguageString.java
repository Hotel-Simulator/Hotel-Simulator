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
	public final List<Pair<String, String>> replacementStringList;
	public final List<Pair<String, LanguageString>> replacementLanguageStringList;

	public static void kryoRegister() {
		KryoConfig.kryo.register(LanguageString.class, new Serializer<LanguageString>() {
			@Override
			public void write(Kryo kryo, Output output, LanguageString object) {
				kryo.writeObject(output, object.path);
				kryo.writeObject(output, object.replacementStringList, KryoConfig.listSerializer(Pair.class));
				kryo.writeObject(output, object.replacementLanguageStringList, KryoConfig.listSerializer(Pair.class));
			}

			@Override
			public LanguageString read(Kryo kryo, Input input, Class<? extends LanguageString> type) {
				return new LanguageString(
						kryo.readObject(input, String.class),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(Pair.class)),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(Pair.class)));
			}
		});
	}

	public LanguageString(String path,
			List<Pair<String, String>> replacementStringList,
			List<Pair<String, LanguageString>> replacementLanguageStringList) {
		this.path = path;
		this.replacementStringList = replacementStringList;
		this.replacementLanguageStringList = replacementLanguageStringList;
	}

	public LanguageString(String path, List<Pair<String, String>> replacementStringList) {
		this(path, replacementStringList, List.of());
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
		return Objects.equals(path, that.path) && Objects.equals(replacementStringList, that.replacementStringList) && Objects.equals(replacementLanguageStringList,
				that.replacementLanguageStringList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(path, replacementStringList, replacementLanguageStringList);
	}
}
