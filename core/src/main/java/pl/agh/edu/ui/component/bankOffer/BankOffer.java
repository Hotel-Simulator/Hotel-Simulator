package pl.agh.edu.ui.component.bankOffer;

import static com.badlogic.gdx.utils.Align.left;
import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinFont.BODY2;
import static pl.agh.edu.ui.utils.SkinFont.H2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.agh.edu.GdxGame;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.data.type.BankData;
import pl.agh.edu.engine.bank.BankAccount;
import pl.agh.edu.ui.component.ClickableTable;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.SkinColor;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

public class BankOffer extends ClickableTable {

	BankData bankData;
	BankAccount bankAccount = ((GdxGame) Gdx.app.getApplicationListener()).engine.hotelHandler.bankAccount;

	public BankOffer(BankData bankData) {
		super();
		this.bankData = bankData;

		String whiteFont = BODY2.getWhiteVariantName();
		String blackFont = BODY2.getName();
		String titleFont = H2.getWhiteVariantName();
		String valueColor = GRAY.getName(SkinColor.ColorLevel._700);

		LanguageLabel bankName = new LanguageLabel(bankData.name(), titleFont);
		bankName.setBaseColor(GRAY);
		button.add(bankName).colspan(2).spaceBottom(20f).row();

		LanguageLabel creditInterestRate = new LanguageLabel(new LanguageString("bank.credit.interest"), blackFont);
		Label creditInterestRateValue = new Label(bankData.accountDetails().creditInterestRate() + "%", skin, whiteFont, valueColor);
		LanguageLabel bankAccountFee = new LanguageLabel(new LanguageString("bank.fee"), blackFont);
		Label bankAccountFeeValue = new Label(bankData.accountDetails().accountFee() + "$", skin, whiteFont, valueColor);

		button.add(creditInterestRate).padRight(50f).spaceBottom(20f).align(left);
		button.add(creditInterestRateValue).spaceBottom(20f).row();
		button.add(bankAccountFee).padRight(50f).spaceBottom(20f).align(left);
		button.add(bankAccountFeeValue).spaceBottom(40f).row();

		WrapperTable buttonContainer2 = new ButtonContainer();

		LanguageLabel changeBankLabel = new LanguageLabel(new LanguageString("bank.change"), whiteFont);
		changeBankLabel.setBaseColor(GRAY);

		buttonContainer2.innerTable.add(changeBankLabel).padRight(20f).padLeft(20f);
		button.add(buttonContainer2).colspan(2).spaceBottom(50f).row();
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
				case SMALL -> 300f;
				case MEDIUM -> 350f;
				case LARGE -> 400f;
			};
		}
	}

	static class ButtonContainer extends WrapperTable {
		public ButtonContainer() {
			super();
			setBackground("text-button-medium-up");
		}
	}
}
