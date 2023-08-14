package pl.agh.edu.json.data_extractor;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JSONDataExtractor {

	private JSONDataExtractor() {}

	public static <T> T extract(String path, String key, Class<T> type) {
		try (FileReader fileReader = new FileReader(path)) {
			return type.cast(((JSONObject) JSONValue.parse(fileReader)).get(key));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
