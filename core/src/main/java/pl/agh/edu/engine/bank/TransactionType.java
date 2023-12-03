package pl.agh.edu.engine.bank;

import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.LanguageString;

public enum TransactionType {
	RECEIVED_CREDIT(true, "transaction.type.receivedCredit"),
	ROOM_RENTING_INCOME(true, "transaction.type.roomRentingIncome"),
	ROOM_DOWNGRADE_INCOME(true, "transaction.type.roomDowngradeIncome"),
	ATTRACTION_DOWNGRADE_INCOME(true, "transaction.type.attractionDowngradeIncome"),
	DAILY_ATTRACTION_INCOME(true, "transaction.type.dailyAttractionIncome"),

	ACCOUNT_FEE_CHARGE(false, "transaction.type.accountFeeCharge"),
	ADVERTISEMENT_CAMPAIGN_COSTS(false, "transaction.type.advertisementCampaignCost"),
	ATTRACTION_BUILDING_COSTS(false, "transaction.type.attractionBuildingCost"),
	ATTRACTION_UPGRADE_EXPENSE(false, "transaction.type.attractionUpgradeExpense"),
	DAILY_ATTRACTION_EXPENSES(false, "transaction.type.dailyAttractionExpense"),
	CREDIT_PAYMENT(false, "transaction.type.creditPayment"),
	EMPLOYEES_SALARY(false, "transaction.type.employeesSalary"),
	EMPLOYEE_BONUS(false, "transaction.type.employeeBonus"),
	ROOM_BUILDING_COSTS(false, "transaction.type.roomBuildingCosts"),
	ROOM_UPGRADE_EXPENSE(false, "transaction.type.roomUpgradeExpense");

	public final boolean isIncome;
	public final LanguageString languageString;

	TransactionType(boolean isIncome, String string) {
		this.isIncome = isIncome;
		this.languageString = new LanguageString(string);
	}

	public static void kryoRegister() {
		KryoConfig.kryo.register(TransactionType.class);
	}
}
