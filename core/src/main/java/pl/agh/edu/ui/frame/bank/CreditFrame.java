package pl.agh.edu.ui.frame.bank;

import static pl.agh.edu.ui.utils.SkinFont.BUTTON1;

import com.badlogic.gdx.scenes.scene2d.Actor;

import pl.agh.edu.engine.bank.Credit;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.component.table.CustomTable;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.utils.LanguageString;

public class CreditFrame extends BaseFrame {

	public CreditFrame() {

		super(new LanguageString("navbar.button.credit"));
		CustomTable<Credit> creditTable = new CustomTable.CustomTableBuilder<Credit>()
				.addColumn(new LanguageString("creditTable.column.date"), this::createDate, 4)
				.addColumn(new LanguageString("creditTable.column.monthly"), this::createMonthly, 4)
				.addColumn(new LanguageString("creditTable.column.payall"), this::createPayAll, 4)
				.build();

		engine.hotelHandler.bankAccountHandler.getCurrentCredits().keySet().forEach(credit -> creditTable.addRowWithRemove(credit, () -> {
			engine.hotelHandler.bankAccountHandler.payEntireCredit(credit);

		}));

		mainTable.add(creditTable).grow();
	}

	private Actor createPayAll(Credit credit) {
		CustomLabel cl = new CustomLabel(BUTTON1.getWhiteVariantName());
		cl.makeItLink(() -> {
			System.out.println("test");
		});
		return cl;
	}

	private Actor createMonthly(Credit credit) {
		return new CustomLabel(BUTTON1.getWhiteVariantName());
	}

	private Actor createDate(Credit credit) {
		return new CustomLabel(BUTTON1.getWhiteVariantName());
	}

}
