package pl.agh.edu.ui.component.bankOffer;

import static com.badlogic.gdx.utils.Align.left;
import static pl.agh.edu.ui.utils.FontType.BODY_2;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.data.type.BankData;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.ui.utils.SkinColor;
import pl.agh.edu.ui.utils.wrapper.ButtonTable;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

public class BankOffer extends ButtonTable {
	public BankOffer(BankData bankData, BaseFrame baseFrame) {
		super();
		// innerButton.debugAll();
		innerTable.setBackground("table-row-background");
		String whiteFont = BODY_2.getWhiteVariantName();
		String blackFont = BODY_2.getName();
		String valueColor = SkinColor.GRAY.getName(SkinColor.ColorLevel._700);

		Skin skin = GameSkin.getInstance();
		// LabeledButton bankName = new LabeledButton(Size.LARGE,bankData.name());
		WrapperTable buttonContainer = new WrapperTable() {
			@Override
			public void drawDebugBounds(ShapeRenderer shapes) {
				super.drawDebugBounds(shapes);
			}
		};
		LanguageLabel bankNameLabel = new LanguageLabel(bankData.name(), whiteFont);
		bankNameLabel.setBaseColor(SkinColor.GRAY);

		buttonContainer.setBackground("text-button-medium-up");
		buttonContainer.innerTable.add(bankNameLabel).padRight(20f).padLeft(20f);

		// podkreślenie, cały obszar klikalny, jakaś obwódka
		innerTable.add(buttonContainer).colspan(2).spaceBottom(50f).row();
		LanguageLabel creditInterestRate = new LanguageLabel("bank.credit.interest", blackFont);
		Label creditInterestRateValue = new Label(bankData.accountDetails().creditInterestRate() + "%", skin, whiteFont, valueColor);
		LanguageLabel bankAccountFee = new LanguageLabel("bank.fee", blackFont);
		Label bankAccountFeeValue = new Label(bankData.accountDetails().accountFee() + "$", skin, whiteFont, valueColor);

		innerTable.add(creditInterestRate).padRight(50f).spaceBottom(20f).align(left);
		innerTable.add(creditInterestRateValue).spaceBottom(20f).row();
		innerTable.add(bankAccountFee).padRight(50f).spaceBottom(20f).align(left);
		innerTable.add(bankAccountFeeValue).spaceBottom(20f).row();

		setResolutionChangeHandler(this::changeSize);
		onResolutionChange();

		innerButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				baseFrame.game.engine.hotelHandler.bankAccount.setAccountDetails(bankData.accountDetails());
				System.out.println(baseFrame.game.engine.hotelHandler.bankAccount.getAccountFee());
				System.out.println("clicked");
			}
		});

		innerButton.addListener(new InputListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
				System.out.println("enter");
			}
		});

	}

	public void changeSize() {
		size(BankOfferStyle.getWidth(), BankOfferStyle.getHeight());
		setSize(BankOfferStyle.getWidth(), BankOfferStyle.getHeight());
	}

	public static class BankOfferStyle {

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
}
