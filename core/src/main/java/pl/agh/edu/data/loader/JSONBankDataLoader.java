package pl.agh.edu.data.loader;

import static pl.agh.edu.data.extractor.JSONFilePath.BANK_CONFIG;

import java.math.BigDecimal;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import pl.agh.edu.data.extractor.JSONDataExtractor;
import pl.agh.edu.data.extractor.JSONValueUtil;
import pl.agh.edu.data.type.BankData;
import pl.agh.edu.engine.bank.BankAccountDetails;

public class JSONBankDataLoader {
	private static final String JSON_FILE_PATH = BANK_CONFIG.get();

	public static List<BankData> scenarios;
	public static int chargeAccountFeeDayOfMonth;
	public static BigDecimal initialBalance;
	public static BigDecimal minCreditValue;
	public static long basicCreditLengthInMonths;

	static {
		load();
	}

	private JSONBankDataLoader() {}

	public static void load() {
		scenarios = JSONValueUtil.getList(
				JSONDataExtractor.extract(JSON_FILE_PATH, "scenarios", JSONArray.class),
				e -> {
					JSONObject data = (JSONObject) e;
					return new BankData(
							data.get("id").toString(),
							(String) data.get("name"),
							new BankAccountDetails(
									BigDecimal.valueOf((Long) data.get("credit_interest_rate")),
									BigDecimal.valueOf((Long) data.get("account_fee"))));
				});
		chargeAccountFeeDayOfMonth = JSONValueUtil.getInt(
				JSONDataExtractor.extract(JSON_FILE_PATH, "charge_account_fee_day_of_month", Long.class));
		initialBalance = JSONValueUtil.getBigDecimal(JSONDataExtractor.extract(JSON_FILE_PATH, "initial_balance", Long.class));
		minCreditValue = JSONValueUtil.getBigDecimal(JSONDataExtractor.extract(JSON_FILE_PATH, "min_credit_value", Long.class));
		basicCreditLengthInMonths = JSONDataExtractor.extract(JSON_FILE_PATH, "basic_credit_length_in_months", Long.class);
	}
}
