package pl.agh.edu.ui.frame.bank;

import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinFont.H4;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.data.loader.JSONBankDataLoader;
import pl.agh.edu.ui.component.CustomScrollPane;
import pl.agh.edu.ui.component.bankOffer.BankOffer;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.utils.LanguageString;

public class BankOfferFrame extends BaseFrame {

	public final List<BankOffer> buttonList = new ArrayList<>();
	public final ButtonGroup<Button> buttonGroup = new ButtonGroup<>();
	Table contentRows;
	ScrollPane scrollPane;

	public BankOfferFrame() {
		super(new LanguageString("navbar.button.offer"));
		String whiteFont = H4.getWhiteVariantName();
		LanguageLabel changeBankLabel = new LanguageLabel(new LanguageString("bank.change"), whiteFont);
		changeBankLabel.setBaseColor(GRAY);
		mainTable.add(changeBankLabel).colspan(2).spaceBottom(50f).row();
		contentRows = new Table();
		scrollPane = new CustomScrollPane(contentRows, skin, "transparent");
		scrollPane.debugAll();
		JSONBankDataLoader.scenarios.forEach(scenario -> buttonList.add(new BankOffer(scenario)));
		buttonList.forEach(bankOfferButton -> buttonGroup.add(bankOfferButton.getActor()));
		buttonList.forEach(buttonOffer -> {
			contentRows.add(buttonOffer).growX().row();
			buttonOffer.setBottonGroup(buttonGroup);
		});
		buttonList.get(0).getActor().setChecked(true);
		buttonList.get(0).getActor().setDisabled(true);
		mainTable.add(scrollPane).grow();
		scrollPane.setFadeScrollBars(false);
		setResolutionChangeHandler(this::resolutionChanged);
	}

	public void resolutionChanged() {
		scrollPane.invalidate();
	}
}
