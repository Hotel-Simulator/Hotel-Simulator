package pl.agh.edu.json.data_loader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;
import pl.agh.edu.json.data.BankData;

import java.math.BigDecimal;
import java.util.List;

public class JSONBankDataLoader {
    private static final JSONFilePath JSON_FILE_PATH = JSONFilePath.BANK_CONFIG;

    public static List<BankData> scenarios;

    private JSONBankDataLoader(){}

    static{
        load();
    }

    public static void load(){
        scenarios = JSONValueUtil.getList(
                JSONDataExtractor.extract(JSON_FILE_PATH,"scenarios", JSONArray.class),
                e -> {
                    JSONObject data = (JSONObject) e;
                    return new BankData(
                            (String) data.get("name"),
                            Math.round((Long) data.get("credit_interest_rate")),
                            Math.round((Long) data.get("deposit_interest_rate")),
                            BigDecimal.valueOf((Long) data.get("account_fee"))
                    );
                }
        );
    }
}
