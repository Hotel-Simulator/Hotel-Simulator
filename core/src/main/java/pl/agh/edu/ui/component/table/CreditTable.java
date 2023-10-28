package pl.agh.edu.ui.component.table;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.bank.Credit;
import pl.agh.edu.ui.utils.FontType;
import pl.agh.edu.ui.utils.LinkLabel;

public class CreditTable extends BaseTable {

	private final BankAccountHandler bankAccountHandler;

	public CreditTable(BankAccountHandler bankAccountHandler) {
		super();
		this.bankAccountHandler = bankAccountHandler;
		for (Credit credit : bankAccountHandler.getCurrentCredits().keySet()) {
			BaseRow row = new CreditBaseRow(credit);
			addRow(row, contentRows);
		}
	}

	@Override
	protected BaseRow createHeader() {
		return new CreditBaseRow();
	}

	public void layout() {
		super.layout();
		System.out.println("scrollPane.getScrollBarWidth() = " + scrollPane.getScrollBarWidth());
		innerTable.setWidth(getWidth() - scrollPane.getScrollBarWidth());
	}

	public class CreditBaseRow extends BaseRow {

		public CreditBaseRow() {
			super(List.of("creditTable.column.date", "creditTable.column.monthly", "creditTable.column.payall"));
		}

		public CreditBaseRow(Credit credit) {
			super();

			Label date = new Label(bankAccountHandler.getLastPaymentDate(credit).toString(), skin, BaseTableStyle.getFont().getName());
			Label monthly = new Label(credit.monthlyPayment.toString(), skin, BaseTableStyle.getFont().getName());
			LinkLabel payAllButton = new LinkLabel("linkLabel.payall", FontType.BUTTON_1.getWhiteVariantName(), () -> {
				bankAccountHandler.payEntireCredit(credit);
				deleteRow(this);
			});
			insertActorsToRow(List.of(date, monthly, payAllButton));
		}
	}

	public static class CreditTableStyle extends BaseTableStyle {
		public static FontType getTableFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> FontType.BODY_2;
				case MEDIUM -> FontType.BODY_1;
				case LARGE -> FontType.H4;
			};
		}

	}

}
