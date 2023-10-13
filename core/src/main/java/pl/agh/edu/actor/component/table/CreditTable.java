package pl.agh.edu.actor.component.table;

import static pl.agh.edu.actor.component.table.BaseTable.BaseTableStyle.getFont;
import static pl.agh.edu.actor.component.table.BaseTable.BaseTableStyle.getRowSpacing;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.button.LabeledButton;
import pl.agh.edu.actor.utils.Size;
import pl.agh.edu.model.bank.BankAccount;
import pl.agh.edu.model.bank.Credit;

public class CreditTable extends BaseTable {

	public CreditTable(BankAccount bankAccount) {
		super(List.of("creditTable.column.date", "creditTable.column.monthly", "creditTable.column.payall"));

		for (Credit credit : bankAccount.getCredits()) {
			LabeledButton payAllButton = new LabeledButton(Size.SMALL, "labeledButton.credit.payall", () -> {
				// TODO: 09.10.2023 payAll(credit);
			});

			BaseRow row = createRow(new Label(credit.getTakeOutDate().toString(), HotelSkin.getInstance(), getFont()),
					new Label(credit.getMonthlyPayment().toString(), HotelSkin.getInstance(), getFont()),
					payAllButton);
			row.align(Align.bottomLeft);
			innerTable.add(row).space(getRowSpacing()).growX();
		}
	}

}
