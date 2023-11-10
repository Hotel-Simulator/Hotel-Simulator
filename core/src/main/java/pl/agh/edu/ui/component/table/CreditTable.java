package pl.agh.edu.ui.component.table;

import static pl.agh.edu.ui.utils.SkinFont.BUTTON_1;

import java.util.List;
import java.util.stream.Stream;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.bank.Credit;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.utils.CustomBigDecimal;
import pl.agh.edu.utils.LanguageString;

//public class CreditTable extends CustomTable {
//
//	private final BankAccountHandler bankAccountHandler;
//
//	private static final List<LanguageString> creditTableColumnNames = Stream.of("creditTable.column.date", "creditTable.column.monthly", "creditTable.column.payall").map(
//			LanguageString::new).toList();
//
//	public CreditTable(BankAccountHandler bankAccountHandler) {
//		super(creditTableColumnNames);
//		this.bankAccountHandler = bankAccountHandler;
//		for (Credit credit : bankAccountHandler.getCurrentCredits().keySet()) {
//			addRow(new CreditBaseRow(credit));
//		}
//	}
//
//	public class CreditBaseRow extends BaseRow {
//		public CreditBaseRow(Credit credit) {
//			super();
//
//			Label date = new Label(bankAccountHandler.getLastPaymentDate(credit).toString(), skin, CreditTableStyle.getFont());
//			Label monthly = new Label("%s $".formatted(new CustomBigDecimal(credit.monthlyPayment.toString())), skin, CreditTableStyle.getFont());
//			CustomLabel payAllButton = new CustomLabel(BUTTON_1.getWhiteVariantName());
//			payAllButton.setText("%s $".formatted(new CustomBigDecimal(bankAccountHandler.getValueLeftToPay(credit))));
//			payAllButton.makeItLink(() -> {
//				bankAccountHandler.payEntireCredit(credit);
//				deleteRow(this);
//			});
//			insertActorsToRow(List.of(date, monthly, payAllButton));
//		}
//	}
//
//	public static class CreditTableStyle extends BaseTableStyle {
//
//	}
//
//}
