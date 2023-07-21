package pl.agh.edu.json.data_extractor;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.io.FileReader;
import java.io.IOException;

public class JSONDataExtractor {

    private JSONDataExtractor(){}

     public static  <T> T extract(JSONFilePath path,String key,Class<T> type){
        try(FileReader fileReader = new FileReader(path.get())){
            return type.cast(((JSONObject)JSONValue.parse(fileReader)).get(key));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
