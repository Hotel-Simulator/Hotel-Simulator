package json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import pl.agh.edu.json.data_extractor.JSONDataExtractor;

public class JSONDataExtractorTest {

	@Test
	public void extractStringCorrectValueTest() {
		// given
		String path = "./core/src/test/resources/test.json";
		String key = "string";
		Class<String> type = String.class;
		// when
		String result = JSONDataExtractor.extract(path, key, type);
		// then
		assertEquals("hello", result);
	}

	@Test
	public void extractDoubleCorrectValueTest() {
		// given
		String path = "./core/src/test/resources/test.json";
		String key = "double";
		Class<Double> type = Double.class;
		// when
		Double result = JSONDataExtractor.extract(path, key, type);
		// then
		assertEquals(2.1, result);
	}

	@Test
	public void extractLongCorrectValueTest() {
		// given
		String path = "./core/src/test/resources/test.json";
		String key = "long";
		Class<Long> type = Long.class;
		// when
		Long result = JSONDataExtractor.extract(path, key, type);
		// then
		assertEquals(37L, result);
	}

	@Test
	public void extractJSONObjectCorrectValueTest() {
		// given
		String path = "./core/src/test/resources/test.json";
		String key = "json_object";
		Class<JSONObject> type = JSONObject.class;
		// when
		JSONObject result = JSONDataExtractor.extract(path, key, type);
		Long arg = (Long) result.get("arg");
		// then
		assertEquals(1L, arg);
	}

	@Test
	public void extractJSONArrayCorrectValueTest() {
		// given
		String path = "./core/src/test/resources/test.json";
		String key = "json_array";
		Class<JSONArray> type = JSONArray.class;
		// when
		JSONArray result = JSONDataExtractor.extract(path, key, type);
		Long arg = (Long) result.get(0);
		// then
		assertEquals(1L, arg);
	}

}
