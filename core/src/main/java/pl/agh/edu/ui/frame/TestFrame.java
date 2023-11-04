package pl.agh.edu.ui.frame;

import static com.badlogic.gdx.utils.Align.left;

import pl.agh.edu.data.loader.JSONBankDataLoader;
import pl.agh.edu.ui.component.bankOffer.BankOffer;
import pl.agh.edu.utils.LanguageString;

public class TestFrame extends BaseFrame {
	public TestFrame(LanguageString languageString) {
		super(languageString);

		BankOffer bankOffer = new BankOffer(JSONBankDataLoader.scenarios.get(0), this);
		bankOffer.align(left);
		mainTable.add(bankOffer).space(20f).row();

	}
}
