package pl.agh.edu.ui.component.bankOffer;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.left;
import static com.badlogic.gdx.utils.Align.right;
import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinFont.BODY2;
import static pl.agh.edu.ui.utils.SkinFont.H3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.agh.edu.GdxGame;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.data.type.BankData;
import pl.agh.edu.engine.bank.BankAccount;
import pl.agh.edu.ui.component.ClickableTable;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.SkinColor;
import pl.agh.edu.utils.LanguageString;

public class BankOffer extends ClickableTable {

	public final BankData bankData;
	BankAccount bankAccount = ((GdxGame) Gdx.app.getApplicationListener()).engine.bankAccountHandler.account;

	public BankOffer(BankData bankData) {
		super();
		this.bankData = bankData;

		String whiteFont = BODY2.getWhiteVariantName();
		String blackFont = BODY2.getName();
		String titleFont = H3.getWhiteVariantName();
		String valueColor = GRAY.getName(SkinColor.ColorLevel._700);

		LanguageLabel bankName = new LanguageLabel(bankData.name(), titleFont);
		bankName.setBaseColor(GRAY);
		bankName.setAlignment(center, center);
		button.add(bankName).width(500f).colspan(2).spaceBottom(20f).row();

		LanguageLabel creditInterestRate = new LanguageLabel(new LanguageString("bank.credit.interest"), blackFont);
		Label creditInterestRateValue = new Label(bankData.accountDetails().creditInterestRate() + "%", skin, whiteFont, valueColor);
		LanguageLabel bankAccountFee = new LanguageLabel(new LanguageString("bank.fee"), blackFont);
		Label bankAccountFeeValue = new Label(bankData.accountDetails().accountFee() + "$", skin, whiteFont, valueColor);

		button.add(creditInterestRate).padRight(50f).spaceBottom(20f).align(left);
		button.add(creditInterestRateValue).spaceBottom(20f).align(right).row();
		button.add(bankAccountFee).padRight(50f).spaceBottom(20f).align(left);
		button.add(bankAccountFeeValue).spaceBottom(40f).align(right).row();

	}

	protected void changeSize() {
		height(BankOfferStyle.getHeight());
	}

	@Override
	protected void selectAction() {
		bankAccount.setBankData(this.bankData);
	}

	public static class BankOfferStyle {
		public static float getHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 200f;
				case MEDIUM -> 250f;
				case LARGE -> 300f;
			};
		}
	}
}
