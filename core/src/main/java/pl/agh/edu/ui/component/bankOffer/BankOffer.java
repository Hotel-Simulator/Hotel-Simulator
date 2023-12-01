package pl.agh.edu.ui.component.bankOffer;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.left;
import static com.badlogic.gdx.utils.Align.right;
import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinFont.BODY2;
import static pl.agh.edu.ui.utils.SkinFont.H3;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.badlogic.gdx.utils.Null;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.data.type.BankData;
import pl.agh.edu.engine.bank.BankAccount;
import pl.agh.edu.ui.component.ClickableTable;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.SkinColor;
import pl.agh.edu.utils.LanguageString;

import java.util.function.Function;

public class BankOffer extends ClickableTable {

	public final BankData bankData;
	BankAccount bankAccount = engine.bankAccountHandler.account;
	Function<BankAccount, Void> function;

	public BankOffer(BankData bankData, ButtonGroup<Button> buttonGroup, Function<BankAccount, Void> function) {
		super(buttonGroup);

		this.bankData = bankData;
		this.function = function;

		LanguageLabel bankName = new LanguageLabel(bankData.name(), BankOfferStyle.getTitleFont());
		bankName.setBaseColor(GRAY);
		bankName.setAlignment(center, center);
		button.add(bankName).width(500f).colspan(2).spaceBottom(20f).row();

		LanguageLabel creditInterestRate = new LanguageLabel(new LanguageString("bank.credit.interest"), BankOfferStyle.getBlackFont());
		Label creditInterestRateValue = new Label(bankData.accountDetails().creditInterestRate() + "%", skin, BankOfferStyle.getWhiteFont() , BankOfferStyle.getValueColor());
		LanguageLabel bankAccountFee = new LanguageLabel(new LanguageString("bank.fee"), BankOfferStyle.getBlackFont());
		Label bankAccountFeeValue = new Label(bankData.accountDetails().accountFee() + "$", skin, BankOfferStyle.getWhiteFont(), BankOfferStyle.getValueColor());

		button.add(creditInterestRate).padRight(BankOfferStyle.getPadRight()).spaceBottom(BankOfferStyle.getRowSpace()).align(left);
		button.add(creditInterestRateValue).spaceBottom(BankOfferStyle.getRowSpace()).align(right).row();
		button.add(bankAccountFee).padRight(BankOfferStyle.getPadRight()).spaceBottom(BankOfferStyle.getRowSpace()).align(left);
		button.add(bankAccountFeeValue).spaceBottom(BankOfferStyle.getSpaceBottom()).align(right).row();

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
		public static String getWhiteFont() {
			return BODY2.getWhiteVariantName();
		}
		public static String getBlackFont() {
			return BODY2.getName();
		}
		public static String getTitleFont() {
			return H3.getWhiteVariantName();
		}
		public static String getValueColor() {
			return GRAY.getName(SkinColor.ColorLevel._700);
		}
		public static float getPadRight() {
			return 50f;
		}
		public static float getRowSpace() {
			return 20f;
		}
		public static float getSpaceBottom(){
			return 50f;
		}
	}
}
