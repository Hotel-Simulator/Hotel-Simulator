package pl.agh.edu.ui.frame.bank;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.data.loader.JSONBankDataLoader;
import pl.agh.edu.ui.component.CustomScrollPane;
import pl.agh.edu.ui.component.bankOffer.BankOffer;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.utils.LanguageString;

public class BankOfferFrame extends BaseFrame {

	public final List<BankOffer> buttonList = new ArrayList<>();
	public final ButtonGroup<Button> buttonGroup = new ButtonGroup<>();

	public BankOfferFrame() {
		super(new LanguageString("navbar.button.offer"));
		Table contentRows = new Table();
		ScrollPane scrollPane = new CustomScrollPane(contentRows, skin, "transparent");
		JSONBankDataLoader.scenarios.forEach(scenario -> buttonList.add(new BankOffer(scenario)));
		buttonList.forEach(bankOfferButton -> buttonGroup.add(bankOfferButton.getActor()));
		buttonList.forEach(buttonOffer -> {
			contentRows.add(buttonOffer).space(20f).pad(20f).growX().row();
		});
		mainTable.add(scrollPane).grow();
		scrollPane.setFadeScrollBars(false);
	}
}
