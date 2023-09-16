package pl.agh.edu.json.data_loader;

import java.math.BigDecimal;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pl.agh.edu.json.data.BankData;
import pl.agh.edu.json.data_extractor.JSONDataExtractor;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_extractor.JSONValueUtil;

public class JSONBankDataLoader {
	private static final String JSON_FILE_PATH = JSONFilePath.BANK_CONFIG.get();

	public static List<BankData> scenarios;
	public static int chargeAccountFeeDayOfMonth;

	private JSONBankDataLoader() {}

	static {
		load();
	}

	public static void load() {
		scenarios = JSONValueUtil.getList(
				JSONDataExtractor.extract(JSON_FILE_PATH, "scenarios", JSONArray.class),
				e -> {
					JSONObject data = (JSONObject) e;
					return new BankData(
							(String) data.get("name"),
							Math.round((Long) data.get("credit_interest_rate")),
							Math.round((Long) data.get("deposit_interest_rate")),
							BigDecimal.valueOf((Long) data.get("account_fee")));
				});
		chargeAccountFeeDayOfMonth = JSONValueUtil.getInt(
				JSONDataExtractor.extract(JSON_FILE_PATH, "charge_account_fee_day_of_month", Long.class));
	}
}
