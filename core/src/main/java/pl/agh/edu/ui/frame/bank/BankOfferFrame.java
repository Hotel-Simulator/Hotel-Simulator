package pl.agh.edu.ui.frame.bank;

import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinFont.H4;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.data.loader.JSONBankDataLoader;
import pl.agh.edu.engine.bank.BankAccount;
import pl.agh.edu.ui.component.CustomScrollPane;
import pl.agh.edu.ui.component.bankOffer.BankOffer;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.utils.LanguageString;

public class BankOfferFrame extends BaseFrame {

	public final ButtonGroup<Button> buttonGroup = new ButtonGroup<>();
	public final List<BankOffer> bankOfferList = new ArrayList<>();
	Table contentRows = new Table();
	ScrollPane scrollPane;
	BankAccount bankAccount = engine.bankAccountHandler.account;

	public BankOfferFrame() {
		super(new LanguageString("navbar.button.offer"));
		String whiteFont = H4.getWhiteVariantName();
		LanguageLabel changeBankLabel = new LanguageLabel(new LanguageString("bank.change"), whiteFont);
		changeBankLabel.setBaseColor(GRAY);
		mainTable.add(changeBankLabel).colspan(2).spaceBottom(BankOfferFrameStyle.subtitleSpace()).row();
		Table contentRows = new Table();
		scrollPane = new CustomScrollPane(contentRows, skin, "transparent");
		JSONBankDataLoader.scenarios.forEach(scenario -> {
							BankOffer bankOffer = new BankOffer(scenario, buttonGroup, bankAccount -> {
								bankAccount.setBankData(scenario);
								return null;
							});
							buttonGroup.add(bankOffer.getActor());
							bankOfferList.add(bankOffer);
						}
		);

		AtomicInteger counter = new AtomicInteger();
		buttonGroup.getButtons().forEach(buttonOffer -> {
			contentRows.add(buttonOffer).space(10f).pad(5f).uniform().growX();
			if(GraphicConfig.getResolution().WIDTH < 1000f)
				contentRows.row();
			else if(GraphicConfig.getResolution().WIDTH < 1500f && GraphicConfig.getResolution().WIDTH >= 1000f){
				if (counter.get() % 2 == 1)
					contentRows.row();
			}

			counter.getAndIncrement();
		});
		mainTable.add(scrollPane).grow();
		scrollPane.setFadeScrollBars(false);
		setResolutionChangeHandler(this::resolutionChanged);
		for (BankOffer bankOffer : bankOfferList) {
			if (bankAccount != null && bankOffer.getActor() != null && Objects.equals(bankAccount.bankId, bankOffer.bankData.id())) {
				bankOffer.getActor().setDisabled(true);
				bankOffer.getActor().setChecked(true);
			}
		}
	}


//	public BankOfferFrame() {
//		super(new LanguageString("navbar.button.offer"));
//		String whiteFont = H4.getWhiteVariantName();
//		LanguageLabel changeBankLabel = new LanguageLabel(new LanguageString("bank.change"), whiteFont);
//		changeBankLabel.setBaseColor(GRAY);
//		mainTable.add(changeBankLabel).colspan(2).spaceBottom(BankOfferFrameStyle.subtitleSpace()).row();
//
//		scrollPane = new CustomScrollPane(contentRows, skin, "transparent");
//		JSONBankDataLoader.scenarios.forEach(scenario -> buttonList.add(new BankOffer(scenario, bankData -> {
//
//			return null;
//		} )));
//		buttonList.forEach(bankOfferButton -> buttonGroup.add(bankOfferButton.getActor()));
//
//		mainTable.add(scrollPane).grow();
//		scrollPane.setFadeScrollBars(false);
//		setResolutionChangeHandler(this::resolutionChanged);
//		for (BankOffer bankOffer : buttonList) {
//			if (bankAccount != null && Objects.equals(bankAccount.bankId, bankOffer.bankData.id())) {
//				bankOffer.getActor().setDisabled(true);
//				bankOffer.getActor().setChecked(true);
//			}
//
//		}
//	}
//
//
//	public void fillContentTable(){
//		AtomicInteger counter = new AtomicInteger();
//		buttonList.forEach(buttonOffer -> {
//			contentRows.add(buttonOffer).space(10f).pad(5f).uniform().growX();
//			if(GraphicConfig.getResolution().WIDTH < 1000f)
//				contentRows.row();
//			else if(GraphicConfig.getResolution().WIDTH < 1500f && GraphicConfig.getResolution().WIDTH >= 1000f){
//				if (counter.get() % 2 == 1)
//					contentRows.row();
//			}
//
//			buttonOffer.setBottonGroup(buttonGroup);
//			counter.getAndIncrement();
//		});
//	}

//	public BankOfferFrame() {
//		super(new LanguageString("navbar.button.offer"));
//		String whiteFont = H4.getWhiteVariantName();
//		LanguageLabel changeBankLabel = new LanguageLabel(new LanguageString("bank.change"), whiteFont);
//		changeBankLabel.setBaseColor(GRAY);
//		mainTable.add(changeBankLabel).colspan(2).spaceBottom(BankOfferFrameStyle.subtitleSpace()).row();
//		VerticalGroup contentRows = new VerticalGroup();
//		scrollPane = new CustomScrollPane(contentRows, skin, "transparent");
//		JSONBankDataLoader.scenarios.forEach(scenario -> buttonList.add(new BankOffer(scenario)));
//		buttonList.forEach(bankOfferButton -> buttonGroup.add(bankOfferButton.getActor()));
//		buttonList.forEach(buttonOffer -> {
//			contentRows.addActor(buttonOffer);
//			buttonOffer.setBottonGroup(buttonGroup);
//		});
//		mainTable.add(scrollPane).grow();
//		scrollPane.setFadeScrollBars(false);
//		setResolutionChangeHandler(this::resolutionChanged);
//		for (BankOffer bankOffer : buttonList) {
//			if (bankAccount != null && Objects.equals(bankAccount.bankId, bankOffer.bankData.id())) {
//				bankOffer.getActor().setDisabled(true);
//				bankOffer.getActor().setChecked(true);
//			}
//
//		}
//	}

	public void resolutionChanged() {
		scrollPane.invalidate();
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
