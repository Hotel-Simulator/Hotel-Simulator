package pl.agh.edu.ui.component.navbar;

import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.ui.frame.TestFrame;
import pl.agh.edu.utils.LanguageString;

public enum NavbarButtonType {
	BANK("bank", new TestFrame(new LanguageString("frame.title.bank"))),
	HOTEL("hotel", new TestFrame(new LanguageString("frame.title.hotel"))),
	EMPLOYEE("employee", new TestFrame(new LanguageString("frame.title.employee"))),
	TAX("tax", new TestFrame(new LanguageString("frame.title.tax"))),
	AD("ad", new TestFrame(new LanguageString("frame.title.ad"))),
	BOARD("board", new TestFrame(new LanguageString("frame.title.board"))),
	ROOMS("rooms", new TestFrame(new LanguageString("frame.title.rooms"))),
	PLACES("places", new TestFrame(new LanguageString("frame.title.places"))),
	BACK("back", new TestFrame(new LanguageString("frame.title.back"))),
	OFFER("offer", new TestFrame(new LanguageString("frame.title.offer"))),
	ACCOUNT("account", new TestFrame(new LanguageString("frame.title.account"))),
	DEPOSIT("deposit", new TestFrame(new LanguageString("frame.title.deposit"))),
	CREDIT("credit", new TestFrame(new LanguageString("frame.title.credit"))),
	HIRE("hire", new TestFrame(new LanguageString("frame.title.hire"))),
	MANAGE("manage", new TestFrame(new LanguageString("frame.title.manage"))),
	HISTORY("history", new TestFrame(new LanguageString("frame.title.history")));

	private final String styleName;
	private final BaseFrame frame;

	NavbarButtonType(String styleName, BaseFrame frame) {
		this.styleName = styleName;
		this.frame = frame;
	}

	public String getStyleName() {
		return styleName;
	}

	public BaseFrame getFrame() {
		return frame;
	}
}
