package pl.agh.edu.ui.frame.bank;

import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinFont.H4;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.data.loader.JSONBankDataLoader;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.component.table.button_table.BankOfferTable;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.utils.LanguageString;

public class BankOfferFrame extends BaseFrame {
	public BankOfferFrame() {
		super(new LanguageString("navbar.button.offer"));

		LanguageLabel changeBankLabel = new LanguageLabel(new LanguageString("bank.change"), H4.getWhiteVariantName());
		changeBankLabel.setBaseColor(GRAY);

		mainTable.add(changeBankLabel).colspan(2).spaceBottom(BankOfferFrameStyle.subtitleSpace()).row();

		BankOfferTable bankOfferTable = new BankOfferTable(JSONBankDataLoader.scenarios);

		mainTable.add(bankOfferTable).grow();
	}
	private static class BankOfferFrameStyle {
		public static float subtitleSpace() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 5f;
				case MEDIUM -> 10f;
				case LARGE -> 20f;
			};
		}
	}
}
