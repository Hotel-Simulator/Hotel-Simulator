package pl.agh.edu.ui.component.bankOffer;

import static com.badlogic.gdx.scenes.scene2d.Touchable.enabled;
import static com.badlogic.gdx.utils.Align.left;
import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinFont.BODY_2;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Null;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.data.type.BankData;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.audio.SoundAudio;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.ui.utils.SkinColor;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

public class BankOffer extends WrapperTable {
	BaseFrame baseFrame;
	BankData bankData;

	public BankOffer(BankData bankData, BaseFrame baseFrame) {
		super();
		this.baseFrame = baseFrame;
		this.bankData = bankData;

		Skin skin = GameSkin.getInstance();
		String whiteFont = BODY_2.getWhiteVariantName();
		String blackFont = BODY_2.getName();
		String valueColor = GRAY.getName(SkinColor.ColorLevel._700);

		innerTable.setTouchable(enabled);
		setBackground("bank-offer-background");
		WrapperTable buttonContainer = new WrapperTable() {
			@Override
			public void drawDebugBounds(ShapeRenderer shapes) {
				super.drawDebugBounds(shapes);
			}
		};
		LanguageLabel bankNameLabel = new LanguageLabel(bankData.name(), whiteFont);
		bankNameLabel.setBaseColor(GRAY);

		buttonContainer.setBackground("text-button-medium-up");
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

		setResolutionChangeHandler(this::changeSize);
		onResolutionChange();

		innerTable.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				setBackground("bank-offer-background-down");
				SoundAudio.CLICK_2.playAudio();
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				SoundAudio.BUTTON_2.playAudio();
				setBackground("bank-offer-background-over");
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
				if (pointer == -1)
					setBackground("bank-offer-background-over");
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				if (pointer == -1) {
					if (baseFrame.game != null && baseFrame.game.engine.hotelHandler.bankAccount.bankId == bankData.id()) {
						setBackground("bank-offer-background-selected");
					} else {
						setBackground("bank-offer-background");
					}
				}
			}
		});

	}

	@Override
	public void layout() {
		super.layout();
		if (baseFrame.game != null && baseFrame.game.engine.hotelHandler.bankAccount.bankId == bankData.id()) {
			setBackground("bank-offer-background-selected");
		}
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
