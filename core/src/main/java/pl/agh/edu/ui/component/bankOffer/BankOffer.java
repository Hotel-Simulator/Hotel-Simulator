package pl.agh.edu.ui.component.bankOffer;

import static com.badlogic.gdx.utils.Align.left;
import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinFont.BODY2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import pl.agh.edu.GdxGame;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.data.type.BankData;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.ClickableTable;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.SkinColor;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

import java.util.Objects;

public class BankOffer extends ClickableTable {

	BankData bankData;

	public BankOffer(BankData bankData) {
		super();
		this.bankData = bankData;

		Skin skin = GameSkin.getInstance();
		String whiteFont = BODY2.getWhiteVariantName();
		String blackFont = BODY2.getName();
		String valueColor = GRAY.getName(SkinColor.ColorLevel._700);

		WrapperTable buttonContainer = new ButtonContainer();
		LanguageLabel bankNameLabel = new LanguageLabel(bankData.name(), whiteFont);
		bankNameLabel.setBaseColor(GRAY);

		buttonContainer.innerTable.add(bankNameLabel).padRight(20f).padLeft(20f);

		innerTable.add(buttonContainer).colspan(2).spaceBottom(50f).row();
		LanguageLabel creditInterestRate = new LanguageLabel(new LanguageString("bank.credit.interest"), blackFont);
		Label creditInterestRateValue = new Label(bankData.accountDetails().creditInterestRate() + "%", skin, whiteFont, valueColor);
		LanguageLabel bankAccountFee = new LanguageLabel(new LanguageString("bank.fee"), blackFont);
		Label bankAccountFeeValue = new Label(bankData.accountDetails().accountFee() + "$", skin, whiteFont, valueColor);

		innerTable.add(creditInterestRate).padRight(50f).spaceBottom(20f).align(left);
		innerTable.add(creditInterestRateValue).spaceBottom(20f).row();
		innerTable.add(bankAccountFee).padRight(50f).spaceBottom(20f).align(left);
		innerTable.add(bankAccountFeeValue).spaceBottom(20f).row();

	}

	@Override
	protected boolean selectedCondition() {
		return Gdx.app.getApplicationListener() != null && Objects.equals(((GdxGame) Gdx.app.getApplicationListener()).engine.hotelHandler.bankAccount.bankId, bankData.id());
	}

	public static class BankOfferStyle extends ClickableTableStyle {
		public static float getWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 600f;
				case MEDIUM -> 650f;
				case LARGE -> 700f;
			};
		}

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
