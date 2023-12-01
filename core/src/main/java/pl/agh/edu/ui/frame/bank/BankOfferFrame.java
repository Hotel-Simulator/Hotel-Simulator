package pl.agh.edu.ui.frame.bank;

import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinFont.H4;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
		scrollPane.setScrollingDisabled(false, true);
		JSONBankDataLoader.scenarios.forEach(scenario -> {
			BankOffer bankOffer = new BankOffer(scenario, buttonGroup, bankAccount -> {
				bankAccount.setBankData(scenario);
				return null;
			});
			buttonGroup.add(bankOffer.getActor());
			bankOfferList.add(bankOffer);
			contentRows.add(bankOffer).space(10f).pad(5f);
		});
		mainTable.add(scrollPane).grow();
		scrollPane.setFadeScrollBars(false);
		scrollPane.setScrollbarsVisible(true);
		setResolutionChangeHandler(this::resolutionChanged);
		for (BankOffer bankOffer : bankOfferList) {
			if (bankAccount != null && bankOffer.getActor() != null && Objects.equals(bankAccount.bankId, bankOffer.bankData.id())) {
				bankOffer.getActor().setDisabled(true);
				bankOffer.getActor().setChecked(true);
			}
		}

	}

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
