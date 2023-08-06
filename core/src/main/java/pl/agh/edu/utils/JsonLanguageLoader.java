package pl.agh.edu.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class JsonLanguageLoader {

    public static String loadLanguageData(String fileName, String category, String itemName) throws IllegalArgumentException {
        String itemText;

        String jsonFile = Gdx.files.internal(fileName).readString();
        JsonValue jsonValue = new JsonReader().parse(jsonFile);

        Json json = new Json();
        json.setIgnoreUnknownFields(true);

        JsonValue categoryJson = jsonValue.get(category);
        if (categoryJson != null && categoryJson.isObject()) {
            JsonValue itemJson = categoryJson.get(itemName);
            if (itemJson != null) {
                itemText = itemJson.getString("text", null);
                if (itemText != null) {
                    return itemText;
                }
            }
        }

        throw new IllegalArgumentException("Item not found for name: " + itemName);
    }
}
